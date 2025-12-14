package com.project.troyoffice.service;

import com.project.troyoffice.dto.UserRequestDTO;
import com.project.troyoffice.dto.UserResponseDTO;
import com.project.troyoffice.mapper.UserMapper;
import com.project.troyoffice.model.Role;
import com.project.troyoffice.model.User;
import com.project.troyoffice.repository.RoleRepository;
import com.project.troyoffice.repository.UserRepository;
import com.project.troyoffice.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        final List<User> userList = userRepository.findByActiveTrue(Sort.by("id"));
        return userList.stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO get(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(UserRequestDTO dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username sudah digunakan.");
        }

        User entity = userMapper.toEntity(dto);
        entity.setActive(false);

        Role role = roleRepository.findById(dto.getRoleId()).orElseThrow(() -> new RuntimeException("Role not found"));
        entity.setRole(role);

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        entity.setPassword(encodedPassword);
        return userRepository.save(entity).getId();
    }

    public void update(UUID id, UserRequestDTO dto) {
        User entity = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        userMapper.toUpdate(entity, dto);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            entity.setPassword(encodedPassword);
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        entity.setRole(role);

        userRepository.save(entity);
    }

    public void delete(UUID id) {
        User entity = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        entity.setActive(false);
        userRepository.save(entity);
    }

    public void updateLastLogin(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        userOptional.ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        });
    }

}
