package com.school.userservice.service;

import com.school.userservice.dto.LoginRequestDTO;
import com.school.userservice.dto.LoginResponseDTO;
import com.school.userservice.dto.UserRegistrationDTO;
import com.school.userservice.entity.User;
import com.school.userservice.entity.UserRole;
import com.school.userservice.repository.UserRepository;
import com.school.userservice.repository.UserRoleRepository;
import com.school.common.exception.ResourceNotFoundException;
import com.school.common.exception.DuplicateResourceException;
import com.school.common.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO register(UserRegistrationDTO registrationDTO) {
        log.info("Registering new user with email: {}", registrationDTO.getEmail());

        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new DuplicateResourceException("User", "email", registrationDTO.getEmail());
        }

        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new DuplicateResourceException("User", "username", registrationDTO.getUsername());
        }

        User user = User.builder()
                .username(registrationDTO.getUsername())
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .phoneNumber(registrationDTO.getPhoneNumber())
                .isActive(true)
                .build();

        user = userRepository.save(user);
        log.info("User registered successfully with id: {}", user.getId());

        // Assign role
        UserRole userRole = UserRole.builder()
                .userId(user.getId())
                .role(registrationDTO.getRole())
                .build();
        userRoleRepository.save(userRole);
        log.info("Role {} assigned to user id: {}", registrationDTO.getRole(), user.getId());

        // Generate token
        List<String> roles = userRoleRepository.findByUserId(user.getId())
                .stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getId(), roles);

        return LoginResponseDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .token(token)
                .roles(roles)
                .build();
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        log.info("Attempting login for email: {}", loginRequest.getEmail());

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", loginRequest.getEmail()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("Invalid password for user: {}", loginRequest.getEmail());
            throw new RuntimeException("Invalid email or password");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("User account is deactivated");
        }

        List<String> roles = userRoleRepository.findByUserId(user.getId())
                .stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getId(), roles);
        log.info("User logged in successfully: {}", user.getId());

        return LoginResponseDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .token(token)
                .roles(roles)
                .build();
    }
}
