package com.tuition.institute.messaging;

import com.tuition.institute.common.ApiResponse;
import com.tuition.institute.common.ResourceNotFoundException;
import com.tuition.institute.user.User;
import com.tuition.institute.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class AnnouncementController {

    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;

    public AnnouncementController(AnnouncementRepository announcementRepository, UserRepository userRepository) {
        this.announcementRepository = announcementRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/announcements")
    public ResponseEntity<ApiResponse<List<AnnouncementDTO>>> getAnnouncements() {
        List<AnnouncementDTO> list = announcementRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping({"/admin/announcements", "/teacher/announcements"})
    public ResponseEntity<ApiResponse<AnnouncementDTO>> createAnnouncement(
            @RequestBody AnnouncementDTO request,
            Authentication auth
    ) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Announcement ann = new Announcement(
                null,
                user,
                request.getTitle(),
                request.getContent(),
                request.getPriority() != null ? request.getPriority().toUpperCase() : "NORMAL",
                request.getTargetAudience() != null ? request.getTargetAudience().toUpperCase() : "ALL",
                request.getPinned() != null && request.getPinned()
        );
        Announcement saved = announcementRepository.save(ann);

        return ResponseEntity.ok(ApiResponse.ok("Announcement published", mapToDTO(saved)));
    }

    public AnnouncementDTO mapToDTO(Announcement a) {
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setId("ann-" + a.getId());
        dto.setTitle(a.getTitle());
        dto.setContent(a.getBody());
        dto.setAuthor(a.getCreatedBy() != null ? a.getCreatedBy().getFullName() : "Apex Administration");
        dto.setAuthorRole(a.getCreatedBy() != null ? a.getCreatedBy().getRole().name().toLowerCase() : "institute");
        dto.setTargetAudience(a.getAudience().substring(0, 1).toUpperCase() + a.getAudience().substring(1).toLowerCase());
        dto.setDate(a.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dto.setPriority(a.getPriority().substring(0, 1).toUpperCase() + a.getPriority().substring(1).toLowerCase());
        dto.setPinned(a.isPinned());
        return dto;
    }
}
