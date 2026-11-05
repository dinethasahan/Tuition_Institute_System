package com.tuition.institute.messaging;

import com.tuition.institute.academic.ClassEntity;
import com.tuition.institute.academic.ClassRepository;
import com.tuition.institute.common.ApiResponse;
import com.tuition.institute.common.ResourceNotFoundException;
import com.tuition.institute.user.User;
import com.tuition.institute.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;
    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final ProfanityFilter profanityFilter;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(
            ChatMessageRepository chatMessageRepository,
            ClassRepository classRepository,
            UserRepository userRepository,
            ProfanityFilter profanityFilter,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.chatMessageRepository = chatMessageRepository;
        this.classRepository = classRepository;
        this.userRepository = userRepository;
        this.profanityFilter = profanityFilter;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/api/v1/classes/{classId}/chat")
    public ResponseEntity<ApiResponse<List<ChatMessageDTO>>> getClassChatHistory(@PathVariable Long classId) {
        List<ChatMessageDTO> messages = chatMessageRepository.findByClassEntityIdOrderBySentAtAsc(classId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(messages));
    }

    @PostMapping("/api/v1/classes/{classId}/chat")
    public ResponseEntity<ApiResponse<ChatMessageDTO>> sendChatMessage(
            @PathVariable Long classId,
            @RequestBody ChatMessageDTO request,
            Authentication auth
    ) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElse(classRepository.findAll().isEmpty() ? null : classRepository.findAll().get(0));

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isFlagged = profanityFilter.containsProfanity(request.getContent());

        ChatMessage msg = new ChatMessage(
                classEntity,
                user,
                request.getContent(),
                request.getAttachmentName(),
                isFlagged
        );
        ChatMessage saved = chatMessageRepository.save(msg);
        ChatMessageDTO dto = mapToDTO(saved);

        // Broadcast to WebSocket subscribers on /topic/class.{classId}
        messagingTemplate.convertAndSend("/topic/class." + classId, dto);

        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @MessageMapping("/chat.sendMessage.{classId}")
    @SendTo("/topic/class.{classId}")
    public ChatMessageDTO handleWebSocketMessage(@DestinationVariable Long classId, @Payload ChatMessageDTO message) {
        return message;
    }

    public ChatMessageDTO mapToDTO(ChatMessage m) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId("msg-" + m.getId());
        dto.setSenderId(String.valueOf(m.getSender().getId()));
        dto.setSenderName(m.getSender().getFullName());
        dto.setSenderRole(m.getSender().getRole().name().toLowerCase());
        dto.setSenderAvatar(m.getSender().getAvatarUrl() != null ? m.getSender().getAvatarUrl() : "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150");
        dto.setContent(m.getBody());
        dto.setTimestamp(m.getSentAt().format(DateTimeFormatter.ofPattern("hh:mm a")));
        dto.setChannelId(m.getClassEntity() != null ? "ch-" + m.getClassEntity().getId() : "ch-1");
        dto.setAttachmentName(m.getAttachmentName());
        return dto;
    }
}
