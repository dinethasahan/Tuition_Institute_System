package com.tuition.institute.academic;

import com.tuition.institute.common.ResourceNotFoundException;
import com.tuition.institute.user.Student;
import com.tuition.institute.user.StudentParentRepository;
import com.tuition.institute.user.StudentRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassService {

    private final ClassRepository classRepository;
    private final ClassSessionRepository classSessionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final StudentParentRepository studentParentRepository;

    public ClassService(
            ClassRepository classRepository,
            ClassSessionRepository classSessionRepository,
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            StudentParentRepository studentParentRepository
    ) {
        this.classRepository = classRepository;
        this.classSessionRepository = classSessionRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.studentParentRepository = studentParentRepository;
    }

    @Transactional(readOnly = true)
    public List<ClassDTO> getAllClasses() {
        return classRepository.findAll().stream().map(this::mapToClassDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClassDTO> getClassesForTeacher(String teacherEmail) {
        return classRepository.findByTeacherUserEmail(teacherEmail).stream()
                .map(this::mapToClassDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClassSessionDTO> getTodaySessionsForTeacher(String teacherEmail) {
        LocalDate today = LocalDate.now();
        List<ClassSession> sessions = classSessionRepository.findByClassEntityTeacherUserEmailAndSessionDate(teacherEmail, today);
        if (sessions.isEmpty()) {
            sessions = classSessionRepository.findByClassEntityTeacherUserEmail(teacherEmail);
        }
        return sessions.stream().map(this::mapToSessionDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClassSessionDTO> getScheduleForStudent(String studentEmail) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentUserEmail(studentEmail);
        List<Long> classIds = enrollments.stream().map(e -> e.getClassEntity().getId()).collect(Collectors.toList());
        return classSessionRepository.findAll().stream()
                .filter(s -> classIds.contains(s.getClassEntity().getId()))
                .map(this::mapToSessionDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClassSessionDTO> getScheduleForParentChild(String parentEmail, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (!studentParentRepository.existsByParentIdAndStudentId(student.getId(), studentId)) {
            // Note: studentParent check
        }

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        List<Long> classIds = enrollments.stream().map(e -> e.getClassEntity().getId()).collect(Collectors.toList());
        return classSessionRepository.findAll().stream()
                .filter(s -> classIds.contains(s.getClassEntity().getId()))
                .map(this::mapToSessionDTO)
                .collect(Collectors.toList());
    }

    public ClassDTO mapToClassDTO(ClassEntity entity) {
        ClassDTO dto = new ClassDTO();
        dto.setId(String.valueOf(entity.getId()));
        dto.setName(entity.getName());
        dto.setSubject(entity.getSubject().getName());
        dto.setTeacherName(entity.getTeacher().getUser().getFullName());
        dto.setGrade(entity.getGrade());
        dto.setBatch(entity.getBatchName() != null ? entity.getBatchName() : "Batch 2026");
        dto.setCapacity(entity.getCapacity());
        dto.setEnrolledCount(enrollmentRepository.findByClassEntityId(entity.getId()).size());
        dto.setMonthlyFee(entity.getMonthlyFee());
        dto.setHall(entity.getHall() != null ? entity.getHall() : "Main Hall");
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public ClassSessionDTO mapToSessionDTO(ClassSession session) {
        ClassSessionDTO dto = new ClassSessionDTO();
        dto.setId(String.valueOf(session.getId()));
        dto.setTitle(session.getClassEntity().getName());
        dto.setSubject(session.getClassEntity().getSubject().getName());
        dto.setTeacherName(session.getClassEntity().getTeacher().getUser().getFullName());
        dto.setTeacherId(String.valueOf(session.getClassEntity().getTeacher().getId()));
        dto.setBatch(session.getClassEntity().getBatchName() != null ? session.getClassEntity().getBatchName() : "Batch Alpha");
        dto.setDayOfWeek(session.getSessionDate().getDayOfWeek().name());
        dto.setStartTime(session.getStartTime().format(DateTimeFormatter.ofPattern("hh:mm a")));
        dto.setEndTime(session.getEndTime().format(DateTimeFormatter.ofPattern("hh:mm a")));
        dto.setRoom(session.getClassEntity().getHall() != null ? session.getClassEntity().getHall() : "Hall B-201");
        dto.setStatus(session.getStatus().equalsIgnoreCase("HELD") ? "Completed" : "Upcoming");
        dto.setColor("bg-indigo-500");
        return dto;
    }
}
