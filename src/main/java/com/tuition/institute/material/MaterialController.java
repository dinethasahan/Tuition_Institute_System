package com.tuition.institute.material;

import com.tuition.institute.academic.ClassEntity;
import com.tuition.institute.academic.ClassRepository;
import com.tuition.institute.common.ApiResponse;
import com.tuition.institute.common.ResourceNotFoundException;
import com.tuition.institute.user.User;
import com.tuition.institute.user.UserRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class MaterialController {

    private final MaterialRepository materialRepository;
    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public MaterialController(
            MaterialRepository materialRepository,
            ClassRepository classRepository,
            UserRepository userRepository,
            FileStorageService fileStorageService
    ) {
        this.materialRepository = materialRepository;
        this.classRepository = classRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/teacher/materials")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<MaterialDTO>>> getTeacherMaterials(Authentication auth) {
        List<MaterialDTO> list = materialRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping("/teacher/materials")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<MaterialDTO>> uploadMaterial(
            @RequestParam("title") String title,
            @RequestParam("subject") String subject,
            @RequestParam("batch") String batch,
            @RequestParam(value = "fileType", defaultValue = "pdf") String fileType,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Authentication auth
    ) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<ClassEntity> classes = classRepository.findAll();
        ClassEntity classEntity = classes.isEmpty() ? null : classes.get(0);

        String storedFilename = file != null && !file.isEmpty()
                ? fileStorageService.storeFile(file)
                : "default_document.pdf";

        String fileSize = file != null ? (file.getSize() / 1024) + " KB" : "2.5 MB";

        Material material = new Material(
                classEntity,
                user,
                title,
                fileType.toUpperCase(),
                "/api/v1/files/download/" + storedFilename,
                fileSize
        );
        Material saved = materialRepository.save(material);

        return ResponseEntity.ok(ApiResponse.ok("Material uploaded successfully", mapToDTO(saved)));
    }

    @DeleteMapping("/teacher/materials/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<String>> deleteMaterial(@PathVariable Long id) {
        materialRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok("Material deleted successfully"));
    }

    @GetMapping("/student/materials")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<MaterialDTO>>> getStudentMaterials(Authentication auth) {
        List<MaterialDTO> list = materialRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/files/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = fileStorageService.loadFileAsPath(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    public MaterialDTO mapToDTO(Material m) {
        MaterialDTO dto = new MaterialDTO();
        dto.setId("mat-" + m.getId());
        dto.setTitle(m.getTitle());
        dto.setSubject(m.getClassEntity() != null ? m.getClassEntity().getSubject().getName() : "General Science");
        dto.setBatch(m.getClassEntity() != null && m.getClassEntity().getBatchName() != null ? m.getClassEntity().getBatchName() : "Batch Alpha (2026)");
        dto.setUploadedBy(m.getUploadedBy() != null ? m.getUploadedBy().getFullName() : "Faculty");
        dto.setUploadDate(m.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dto.setFileSize(m.getFileSize() != null ? m.getFileSize() : "3.2 MB");
        dto.setFileType(m.getType().toLowerCase());
        dto.setDownloadUrl(m.getFileUrl() != null ? m.getFileUrl() : "#");
        return dto;
    }
}
