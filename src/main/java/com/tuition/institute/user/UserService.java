package com.tuition.institute.user;

import com.tuition.institute.common.ResourceNotFoundException;
import com.tuition.institute.security.CustomUserDetails;
import com.tuition.institute.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final StudentParentRepository studentParentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${app.jwt.refresh-expiration-days:7}")
    private long refreshExpirationDays;

    public UserService(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            TeacherRepository teacherRepository,
            StudentRepository studentRepository,
            ParentRepository parentRepository,
            StudentParentRepository studentParentRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.studentParentRepository = studentParentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public LoginResponse authenticate(LoginRequest request) {
        User user = userRepository.findByEmailOrPhone(request.getIdentifier(), request.getIdentifier())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("userId", user.getId());

        String accessToken = jwtService.generateToken(userDetails, extraClaims);
        String refreshTokenStr = createOrRotateRefreshToken(user);

        UserDTO userDTO = mapToUserDTO(user);
        return new LoginResponse(accessToken, refreshTokenStr, 15 * 60, userDTO);
    }

    @Transactional
    public String createOrRotateRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
        String tokenStr = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(
                user,
                tokenStr,
                LocalDateTime.now().plusDays(refreshExpirationDays)
        );
        refreshTokenRepository.save(refreshToken);
        return tokenStr;
    }

    @Transactional
    public LoginResponse refreshToken(String tokenStr) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new BadCredentialsException("Invalid or expired refresh token"));

        if (token.isRevoked() || token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("Refresh token is expired or revoked");
        }

        User user = token.getUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("userId", user.getId());

        String newAccessToken = jwtService.generateToken(userDetails, extraClaims);
        String newRefreshTokenStr = createOrRotateRefreshToken(user);
        UserDTO userDTO = mapToUserDTO(user);

        return new LoginResponse(newAccessToken, newRefreshTokenStr, 15 * 60, userDTO);
    }

    @Transactional
    public void logout(String tokenStr) {
        if (tokenStr != null) {
            refreshTokenRepository.findByToken(tokenStr).ifPresent(t -> {
                t.setRevoked(true);
                refreshTokenRepository.save(t);
            });
        }
    }

    public UserDTO getCurrentUserDTO(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return mapToUserDTO(user);
    }

    public UserDTO mapToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(String.valueOf(user.getId()));
        dto.setName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAvatar(user.getAvatarUrl() != null ? user.getAvatarUrl() : "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150");

        // Map backend roles to match frontend expectations
        if (user.getRole() == Role.ADMIN || user.getRole() == Role.STAFF) {
            dto.setRole("institute");
            dto.setInstituteName("Apex Tuition & Science Institute");
            dto.setDepartment("Administration");
        } else if (user.getRole() == Role.TEACHER) {
            dto.setRole("teacher");
            teacherRepository.findByUserId(user.getId()).ifPresent(t -> {
                dto.setDepartment(t.getDepartment() != null ? t.getDepartment() : "Science & Mathematics");
            });
        } else if (user.getRole() == Role.STUDENT) {
            dto.setRole("student");
            studentRepository.findByUserId(user.getId()).ifPresent(s -> {
                dto.setStudentId(s.getStudentCode());
                dto.setGrade(s.getGrade());
            });
        } else if (user.getRole() == Role.PARENT) {
            dto.setRole("parent");
            parentRepository.findByUserId(user.getId()).ifPresent(p -> {
                List<String> childIds = studentParentRepository.findByParentId(p.getId())
                        .stream()
                        .map(sp -> String.valueOf(sp.getStudent().getId()))
                        .collect(Collectors.toList());
                dto.setParentOf(childIds);
            });
        }

        return dto;
    }

    public List<StaffDTO> getAllStaff() {
        List<User> staffUsers = new ArrayList<>();
        staffUsers.addAll(userRepository.findByRole(Role.ADMIN));
        staffUsers.addAll(userRepository.findByRole(Role.STAFF));
        staffUsers.addAll(userRepository.findByRole(Role.TEACHER));

        return staffUsers.stream().map(u -> {
            StaffDTO dto = new StaffDTO();
            dto.setId("staff-" + u.getId());
            dto.setName(u.getFullName());
            dto.setEmail(u.getEmail());
            dto.setPhone(u.getPhone());
            dto.setStatus(u.getStatus().equalsIgnoreCase("ACTIVE") ? "Active" : "Inactive");
            
            if (u.getRole() == Role.ADMIN) {
                dto.setRole("Institute Director & Lead Math Faculty");
                dto.setDepartment("Mathematics & Administration");
                dto.setPermissions(List.of("All Access", "Billing", "Staff Management", "Academic Overhaul"));
            } else if (u.getRole() == Role.STAFF) {
                dto.setRole("Registrar & Student Affairs Coordinator");
                dto.setDepartment("Administration");
                dto.setPermissions(List.of("Student Admissions", "Fee Collection", "SMS Broadcasts"));
            } else {
                dto.setRole("Lecturer / Faculty");
                dto.setDepartment("Physics & Mathematics");
                dto.setPermissions(List.of("Mark Attendance", "Create Exams", "Grade Students", "Upload Materials"));
            }
            return dto;
        }).collect(Collectors.toList());
    }
}
