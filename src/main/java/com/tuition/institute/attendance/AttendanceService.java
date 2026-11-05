package com.tuition.institute.attendance;

import com.tuition.institute.academic.ClassSession;
import com.tuition.institute.academic.ClassSessionRepository;
import com.tuition.institute.academic.EnrollmentRepository;
import com.tuition.institute.common.ResourceNotFoundException;
import com.tuition.institute.notification.NotificationService;
import com.tuition.institute.user.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ClassSessionRepository classSessionRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final StudentParentRepository studentParentRepository;
    private final NotificationService notificationService;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            ClassSessionRepository classSessionRepository,
            StudentRepository studentRepository,
            EnrollmentRepository enrollmentRepository,
            UserRepository userRepository,
            StudentParentRepository studentParentRepository,
            NotificationService notificationService
    ) {
        this.attendanceRepository = attendanceRepository;
        this.classSessionRepository = classSessionRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.studentParentRepository = studentParentRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public AttendanceRecordDTO recordQrScan(Long sessionId, String qrToken, String teacherEmail) {
        ClassSession session = classSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with ID: " + sessionId));

        User teacherUser = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        Student student = studentRepository.findByQrToken(qrToken)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or unrecognized student QR token"));

        // Check active enrollment
        boolean isEnrolled = enrollmentRepository.existsByStudentIdAndClassEntityIdAndStatus(
                student.getId(), session.getClassEntity().getId(), "ACTIVE");
        if (!isEnrolled) {
            throw new IllegalArgumentException("Student " + student.getUser().getFullName() + " is not actively enrolled in " + session.getClassEntity().getName());
        }

        // Idempotency check: if already marked, return existing
        Optional<Attendance> existing = attendanceRepository.findBySessionIdAndStudentId(sessionId, student.getId());
        if (existing.isPresent()) {
            return mapToDTO(existing.get());
        }

        // Compute status: LATE if > 15 mins after session start time
        LocalTime now = LocalTime.now();
        String status = "Present";
        if (session.getStartTime() != null && now.isAfter(session.getStartTime().plusMinutes(15))) {
            status = "Late";
        }

        Attendance attendance = new Attendance(session, student, status, "QR", teacherUser);
        Attendance saved = attendanceRepository.save(attendance);

        // Async notify linked parents
        notificationService.notifyParentsOfAttendance(student, session.getClassEntity().getName(), status);

        return mapToDTO(saved);
    }

    @Transactional
    public int saveBatchAttendance(Long sessionId, List<AttendanceRecordDTO> records, String markerEmail) {
        ClassSession session = classSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        User marker = userRepository.findByEmail(markerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        int count = 0;
        for (AttendanceRecordDTO rec : records) {
            Long studentId = Long.parseLong(rec.getStudentId());
            Student student = studentRepository.findById(studentId).orElse(null);
            if (student != null) {
                Attendance att = attendanceRepository.findBySessionIdAndStudentId(sessionId, studentId)
                        .orElse(new Attendance(session, student, rec.getStatus(), "MANUAL", marker));
                att.setStatus(rec.getStatus());
                att.setMethod("MANUAL");
                att.setMarkedBy(marker);
                attendanceRepository.save(att);
                count++;
            }
        }
        return count;
    }

    @Transactional(readOnly = true)
    public List<AttendanceRecordDTO> getAttendanceForStudent(String studentEmail) {
        return attendanceRepository.findByStudentUserEmail(studentEmail).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AttendanceRecordDTO> getAttendanceForParentChild(String parentEmail, Long studentId) {
        User parentUser = userRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));

        // Record-level security check
        return attendanceRepository.findByStudentId(studentId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AttendanceRecordDTO mapToDTO(Attendance att) {
        AttendanceRecordDTO dto = new AttendanceRecordDTO();
        dto.setId("att-" + att.getId());
        dto.setStudentId(String.valueOf(att.getStudent().getId()));
        dto.setStudentName(att.getStudent().getUser().getFullName());
        dto.setBatch(att.getSession().getClassEntity().getBatchName() != null ? att.getSession().getClassEntity().getBatchName() : "Batch 2026");
        dto.setSubject(att.getSession().getClassEntity().getSubject().getName());
        dto.setDate(att.getSession().getSessionDate().toString());
        dto.setStatus(att.getStatus());
        dto.setMarkedBy(att.getMarkedBy() != null ? att.getMarkedBy().getFullName() : "Faculty");
        dto.setTime(att.getMarkedAt().format(DateTimeFormatter.ofPattern("hh:mm a")));
        return dto;
    }
}
