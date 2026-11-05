package com.tuition.institute.user;

import com.tuition.institute.academic.*;
import com.tuition.institute.attendance.Attendance;
import com.tuition.institute.attendance.AttendanceRepository;
import com.tuition.institute.exam.Exam;
import com.tuition.institute.exam.ExamRepository;
import com.tuition.institute.exam.ExamResult;
import com.tuition.institute.exam.ExamResultRepository;
import com.tuition.institute.fee.FeeInvoice;
import com.tuition.institute.fee.FeeInvoiceRepository;
import com.tuition.institute.fee.Payment;
import com.tuition.institute.fee.PaymentRepository;
import com.tuition.institute.material.Material;
import com.tuition.institute.material.MaterialRepository;
import com.tuition.institute.messaging.Announcement;
import com.tuition.institute.messaging.AnnouncementRepository;
import com.tuition.institute.messaging.ChatMessage;
import com.tuition.institute.messaging.ChatMessageRepository;
import com.tuition.institute.settings.AuditLog;
import com.tuition.institute.settings.AuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final StudentParentRepository studentParentRepository;
    private final SubjectRepository subjectRepository;
    private final ClassRepository classRepository;
    private final ClassScheduleRepository classScheduleRepository;
    private final ClassSessionRepository classSessionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final ExamRepository examRepository;
    private final ExamResultRepository examResultRepository;
    private final FeeInvoiceRepository feeInvoiceRepository;
    private final PaymentRepository paymentRepository;
    private final MaterialRepository materialRepository;
    private final AnnouncementRepository announcementRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(
            UserRepository userRepository,
            TeacherRepository teacherRepository,
            StudentRepository studentRepository,
            ParentRepository parentRepository,
            StudentParentRepository studentParentRepository,
            SubjectRepository subjectRepository,
            ClassRepository classRepository,
            ClassScheduleRepository classScheduleRepository,
            ClassSessionRepository classSessionRepository,
            EnrollmentRepository enrollmentRepository,
            AttendanceRepository attendanceRepository,
            ExamRepository examRepository,
            ExamResultRepository examResultRepository,
            FeeInvoiceRepository feeInvoiceRepository,
            PaymentRepository paymentRepository,
            MaterialRepository materialRepository,
            AnnouncementRepository announcementRepository,
            ChatMessageRepository chatMessageRepository,
            AuditLogRepository auditLogRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.studentParentRepository = studentParentRepository;
        this.subjectRepository = subjectRepository;
        this.classRepository = classRepository;
        this.classScheduleRepository = classScheduleRepository;
        this.classSessionRepository = classSessionRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.attendanceRepository = attendanceRepository;
        this.examRepository = examRepository;
        this.examResultRepository = examResultRepository;
        this.feeInvoiceRepository = feeInvoiceRepository;
        this.paymentRepository = paymentRepository;
        this.materialRepository = materialRepository;
        this.announcementRepository = announcementRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already contains users. Skipping demo data seeding.");
            return;
        }

        log.info("Seeding initial demo data for Tuition Institute Management System...");

        // 1. Seed Demo Users matching frontend personas
        // Password for all demo accounts: "Password@123"
        String defaultHash = passwordEncoder.encode("Password@123");

        User adminUser = new User(
                "admin@apex-institute.edu",
                "+94 77 123 4567",
                defaultHash,
                Role.ADMIN,
                "Dr. Alistair Vance",
                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80"
        );
        userRepository.save(adminUser);

        User teacherUser = new User(
                "teacher@apex-institute.edu",
                "+94 71 482 9012",
                defaultHash,
                Role.TEACHER,
                "Prof. Sarah Jenkins",
                "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=150&auto=format&fit=crop&q=80"
        );
        userRepository.save(teacherUser);

        User studentUser = new User(
                "student@apex-institute.edu",
                "+94 76 892 1209",
                defaultHash,
                Role.STUDENT,
                "Alex Morgan",
                "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150&auto=format&fit=crop&q=80"
        );
        userRepository.save(studentUser);

        User parentUser = new User(
                "parent@apex-institute.edu",
                "+94 70 902 3318",
                defaultHash,
                Role.PARENT,
                "David Morgan",
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80"
        );
        userRepository.save(parentUser);

        // 2. Profiles
        Teacher teacher = new Teacher(
                teacherUser,
                "Ph.D. in Theoretical Physics",
                "Physics & Mathematics",
                LocalDate.of(2021, 5, 1)
        );
        teacherRepository.save(teacher);

        String sampleQrToken = "3f2a8c1e-9b47-4d0e-8c55-0a7e1d2b6f90";
        Student student = new Student(
                studentUser,
                "APEX-2026-4412",
                LocalDate.of(2008, 4, 15),
                "Grade 12 - Advanced STEM",
                "Royal College, Colombo",
                sampleQrToken
        );
        studentRepository.save(student);

        Parent parent = new Parent(
                parentUser,
                "No. 45, Flower Road, Colombo 07",
                "Chartered Engineer"
        );
        parentRepository.save(parent);

        // Link student and parent
        StudentParent link = new StudentParent(student, parent, "FATHER", true);
        studentParentRepository.save(link);

        // 3. Subjects & Classes
        Subject physics = subjectRepository.findByName("Quantum Physics")
                .orElseGet(() -> subjectRepository.save(new Subject("Quantum Physics", "EN")));

        ClassEntity classEntity = new ClassEntity(
                physics,
                teacher,
                "Grade 12 Quantum Physics - Batch Alpha",
                "Grade 12",
                "Batch Alpha (2026)",
                45,
                new BigDecimal("15000.00"),
                "Hall B-201"
        );
        classRepository.save(classEntity);

        // Schedule & Session
        ClassSchedule schedule = new ClassSchedule(
                classEntity,
                1, // Monday
                LocalTime.of(9, 0),
                LocalTime.of(11, 0)
        );
        classScheduleRepository.save(schedule);

        ClassSession session = new ClassSession(
                classEntity,
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalTime.of(11, 0),
                "SCHEDULED"
        );
        classSessionRepository.save(session);

        // Enrolment
        Enrollment enrollment = new Enrollment(
                student,
                classEntity,
                LocalDate.of(2026, 9, 1),
                "ACTIVE"
        );
        enrollmentRepository.save(enrollment);

        // Attendance
        Attendance attendance = new Attendance(
                session,
                student,
                "Present",
                "QR",
                teacherUser
        );
        attendanceRepository.save(attendance);

        // Exam & Result
        Exam exam = new Exam(
                classEntity,
                "Term 1 Quantum Mechanics Evaluation",
                LocalDate.of(2026, 11, 12),
                LocalTime.of(9, 0),
                120,
                new BigDecimal("100.00"),
                new BigDecimal("40.00"),
                "Hall B-201",
                teacherUser
        );
        exam.setPublished(true);
        examRepository.save(exam);

        ExamResult result = new ExamResult(
                exam,
                student,
                new BigDecimal("94.50"),
                "A+",
                "Outstanding theoretical application"
        );
        examResultRepository.save(result);

        // Invoices & Payments
        FeeInvoice inv1 = new FeeInvoice(
                "INV-2026-089",
                student,
                classEntity,
                "Term 3 Tuition & Advanced STEM Lab Fee",
                LocalDate.of(2026, 10, 1),
                new BigDecimal("15000.00"),
                LocalDate.of(2026, 10, 15),
                "PAID"
        );
        feeInvoiceRepository.save(inv1);

        Payment pay1 = new Payment(
                inv1,
                new BigDecimal("15000.00"),
                "CARD",
                "TXN-948102948",
                adminUser
        );
        paymentRepository.save(pay1);

        FeeInvoice inv2 = new FeeInvoice(
                "INV-2026-090",
                student,
                classEntity,
                "Term 4 Tuition Fee & Practical Session Fee",
                LocalDate.of(2026, 11, 1),
                new BigDecimal("15000.00"),
                LocalDate.of(2026, 11, 15),
                "PENDING"
        );
        feeInvoiceRepository.save(inv2);

        // Study Materials
        Material mat1 = new Material(
                classEntity,
                teacherUser,
                "Quantum Harmonic Oscillator Lecture Slides & Formula Book",
                "PDF",
                "/api/v1/files/download/sample_quantum.pdf",
                "3.4 MB"
        );
        materialRepository.save(mat1);

        // Announcements
        Announcement ann1 = new Announcement(
                null,
                adminUser,
                "Mid-Term Revision Seminar & Hall Allocation",
                "All Grade 12 students are requested to be seated by 8:30 AM in Hall B-201 for the physical physics practical demonstration.",
                "HIGH",
                "ALL",
                true
        );
        announcementRepository.save(ann1);

        // Chat Message
        ChatMessage chat1 = new ChatMessage(
                classEntity,
                teacherUser,
                "Welcome everyone to the Batch Alpha Quantum Physics discussion group! Please post your assignment questions here.",
                null,
                false
        );
        chatMessageRepository.save(chat1);

        // Audit Log
        AuditLog log1 = new AuditLog(
                adminUser,
                "Institute Director",
                "System Initialized & Seed Accounts Created",
                "Security",
                "users",
                adminUser.getId(),
                "127.0.0.1",
                "Success"
        );
        auditLogRepository.save(log1);

        log.info("Demo data seeding completed successfully!");
    }
}
