package com.tuition.institute.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Notification> findByUserEmailOrderByCreatedAtDesc(String email);
    long countByUserIdAndStatusNot(Long userId, String status);
}
