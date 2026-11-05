package com.tuition.institute.exam;

import com.tuition.institute.academic.ClassEntity;
import com.tuition.institute.academic.ClassRepository;
import com.tuition.institute.common.ResourceNotFoundException;
import com.tuition.institute.user.Student;
import com.tuition.institute.user.StudentRepository;
import com.tuition.institute.user.User;
import com.tuition.institute.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamResultRepository examResultRepository;
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public ExamService(
            ExamRepository examRepository,
            ExamResultRepository examResultRepository,
            ClassRepository classRepository,
            StudentRepository studentRepository,
            UserRepository userRepository
    ) {
        this.examRepository = examRepository;
        this.examResultRepository = examResultRepository;
        this.classRepository = classRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<ExamDTO> getExamsForTeacher(String teacherEmail) {
        return examRepository.findByClassEntityTeacherUserEmail(teacherEmail).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExamDTO createExam(ExamDTO dto, String creatorEmail) {
        User user = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<ClassEntity> classes = classRepository.findByTeacherUserEmail(creatorEmail);
        ClassEntity classEntity = classes.isEmpty() ? classRepository.findAll().get(0) : classes.get(0);

        Exam exam = new Exam(
                classEntity,
                dto.getTitle(),
                LocalDate.parse(dto.getDate()),
                LocalTime.parse("09:00"),
                dto.getDurationMinutes() != null ? dto.getDurationMinutes() : 120,
                BigDecimal.valueOf(dto.getTotalMarks() != null ? dto.getTotalMarks() : 100),
                BigDecimal.valueOf(dto.getPassingMarks() != null ? dto.getPassingMarks() : 40),
                dto.getRoom() != null ? dto.getRoom() : "Hall B-201",
                user
        );
        Exam saved = examRepository.save(exam);
        return mapToDTO(saved);
    }

    @Transactional
    public ExamResultDTO recordStudentMark(Long examId, Long studentId, Double marks, String feedback) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        BigDecimal marksBd = BigDecimal.valueOf(marks);
        String grade = computeGrade(marks, exam.getMaxMarks().doubleValue());

        ExamResult result = examResultRepository.findByExamIdAndStudentId(examId, studentId)
                .orElse(new ExamResult(exam, student, marksBd, grade, feedback));

        result.setMarks(marksBd);
        result.setGrade(grade);
        result.setFeedback(feedback);

        ExamResult saved = examResultRepository.save(result);
        return mapToResultDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ExamResultDTO> getResultsForStudent(String studentEmail) {
        return examResultRepository.findByStudentUserEmail(studentEmail).stream()
                .map(this::mapToResultDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExamResultDTO> getResultsForParentChild(Long studentId) {
        return examResultRepository.findByStudentId(studentId).stream()
                .map(this::mapToResultDTO)
                .collect(Collectors.toList());
    }

    public String computeGrade(Double marks, Double total) {
        if (marks == null || total == null || total == 0) return "W";
        double percentage = (marks / total) * 100.0;
        if (percentage >= 90.0) return "A+";
        if (percentage >= 80.0) return "A";
        if (percentage >= 75.0) return "B+";
        if (percentage >= 70.0) return "B";
        if (percentage >= 60.0) return "C";
        if (percentage >= 40.0) return "S";
        return "F";
    }

    public ExamDTO mapToDTO(Exam exam) {
        ExamDTO dto = new ExamDTO();
        dto.setId(String.valueOf(exam.getId()));
        dto.setTitle(exam.getTitle());
        dto.setSubject(exam.getClassEntity().getSubject().getName());
        dto.setBatch(exam.getClassEntity().getBatchName() != null ? exam.getClassEntity().getBatchName() : "Batch Alpha");
        dto.setDate(exam.getExamDate().toString());
        dto.setStartTime(exam.getStartTime() != null ? exam.getStartTime().format(DateTimeFormatter.ofPattern("hh:mm a")) : "09:00 AM");
        dto.setDurationMinutes(exam.getDurationMinutes());
        dto.setTotalMarks(exam.getMaxMarks().doubleValue());
        dto.setPassingMarks(exam.getPassingMarks().doubleValue());
        dto.setRoom(exam.getRoom());
        dto.setStatus(exam.isPublished() ? "Graded" : "Upcoming");
        return dto;
    }

    public ExamResultDTO mapToResultDTO(ExamResult res) {
        ExamResultDTO dto = new ExamResultDTO();
        dto.setId("res-" + res.getId());
        dto.setExamId(String.valueOf(res.getExam().getId()));
        dto.setExamTitle(res.getExam().getTitle());
        dto.setStudentId(String.valueOf(res.getStudent().getId()));
        dto.setStudentName(res.getStudent().getUser().getFullName());
        dto.setSubject(res.getExam().getClassEntity().getSubject().getName());
        dto.setMarksObtained(res.getMarks() != null ? res.getMarks().doubleValue() : 0.0);
        dto.setTotalMarks(res.getExam().getMaxMarks().doubleValue());
        dto.setGrade(res.getGrade());
        double pct = (dto.getMarksObtained() / dto.getTotalMarks()) * 100.0;
        dto.setPercentage(Math.round(pct * 10.0) / 10.0);
        dto.setRank(1);
        dto.setFeedback(res.getFeedback());
        dto.setDate(res.getExam().getExamDate().toString());
        return dto;
    }
}
