package com.tuition.institute.notification;

import com.tuition.institute.user.Student;
import com.tuition.institute.user.StudentParent;
import com.tuition.institute.user.StudentParentRepository;
import com.tuition.institute.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final StudentParentRepository studentParentRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            StudentParentRepository studentParentRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.studentParentRepository = studentParentRepository;
    }

    @Async
    @Transactional
    public void notifyParentsOfAttendance(Student student, String className, String status) {
        List<StudentParent> links = studentParentRepository.findByStudentId(student.getId());
        for (StudentParent link : links) {
            User parentUser = link.getParent().getUser();
            String title = "Attendance Alert: " + student.getUser().getFullName();
            String body = String.format("Dear Parent, %s was marked %s for %s.",
                    student.getUser().getFullName(), status.toUpperCase(), className);

            Notification notification = new Notification(
                    parentUser,
                    "ATTENDANCE",
                    title,
                    body,
                    "WEB_PUSH",
                    "SENT"
            );
            notificationRepository.save(notification);
            log.info("Dispatched attendance notification to parent: {}", parentUser.getEmail());
        }
    }

    @Async
    @Transactional
    public void notifyParentsOfFeeInvoice(Student student, String invoiceNumber, String amount, String dueDate) {
        List<StudentParent> links = studentParentRepository.findByStudentId(student.getId());
        for (StudentParent link : links) {
            User parentUser = link.getParent().getUser();
            String title = "Fee Invoice Issued: " + invoiceNumber;
            String body = String.format("Dear Parent, tuition fee invoice %s of Rs. %s for %s is due by %s.",
                    invoiceNumber, amount, student.getUser().getFullName(), dueDate);

            Notification notification = new Notification(
                    parentUser,
                    "FEE",
                    title,
                    body,
                    "SMS",
                    "SENT"
            );
            notificationRepository.save(notification);
            log.info("Dispatched fee notification to parent: {}", parentUser.getEmail());
        }
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getUserNotifications(String userEmail) {
        return notificationRepository.findByUserEmailOrderByCreatedAtDesc(userEmail).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setStatus("READ");
            notificationRepository.save(n);
        });
    }

    private NotificationDTO mapToDTO(Notification n) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId("notif-" + n.getId());
        dto.setType(n.getType());
        dto.setTitle(n.getTitle());
        dto.setBody(n.getBody());
        dto.setChannel(n.getChannel());
        dto.setStatus(n.getStatus());
        dto.setTimestamp(n.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")));
        return dto;
    }
}
