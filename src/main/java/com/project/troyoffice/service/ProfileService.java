package com.project.troyoffice.service;

import com.project.troyoffice.dto.EmployeeResponseDTO;
import com.project.troyoffice.dto.ProfileDetailResponseDTO;
import com.project.troyoffice.dto.ProfileRequestDTO;
import com.project.troyoffice.dto.UserResponseDTO;
import com.project.troyoffice.mapper.EmployeeMapper;
import com.project.troyoffice.mapper.UserMapper;
import com.project.troyoffice.model.Employee;
import com.project.troyoffice.model.Role;
import com.project.troyoffice.model.User;
import com.project.troyoffice.repository.EmployeeRepository;
import com.project.troyoffice.repository.RoleRepository;
import com.project.troyoffice.repository.UserRepository;
import com.project.troyoffice.util.NotFoundException;
import com.project.troyoffice.util.UserContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    private final UserContext userContext;

    private final UserMapper userMapper;
    private final EmployeeMapper  employeeMapper;

    private final PasswordEncoder passwordEncoder;

    public ProfileDetailResponseDTO getDetail() {

        User user = userContext.getCurrentUser();

        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Employee tidak ditemukan untuk User: " + user.getName()));

        UserResponseDTO userResponse = userMapper.toDTO(user);
        EmployeeResponseDTO employeeResponse = employeeMapper.toDTO(employee);

        ProfileDetailResponseDTO responseDTO = new ProfileDetailResponseDTO();
        responseDTO.setUser(userResponse);
        responseDTO.setEmployee(employeeResponse);

        return responseDTO;
    }

    @Transactional
    public ProfileDetailResponseDTO create(ProfileRequestDTO requestDTO) throws BadRequestException {
        if (employeeRepository.existsByEmployeeNumber(requestDTO.getEmployee().getEmployeeNumber())) {
            throw new BadRequestException("Employee Number sudah terdaftar.");
        }

        if (userRepository.existsByUsername(requestDTO.getUser().getUsername())) {
            throw new BadRequestException("Username sudah terdaftar.");
        }

        User user = userMapper.toEntity(requestDTO.getUser());

        Role role = roleRepository.findById(requestDTO.getUser().getRoleId())
                .orElseThrow(() -> new NotFoundException("Role tidak ditemukan: " + requestDTO.getUser().getRoleId()));

        user.setRole(role);
        String encodedPassword = passwordEncoder.encode(requestDTO.getUser().getPassword());
        user.setPassword(encodedPassword);

        user = userRepository.save(user);

        Employee employee = employeeMapper.toEntity(requestDTO.getEmployee());
        employee.setUser(user);

        employee = employeeRepository.save(employee);

        UserResponseDTO userResponse = userMapper.toDTO(user);
        EmployeeResponseDTO employeeResponse = employeeMapper.toDTO(employee);

        ProfileDetailResponseDTO responseDTO = new ProfileDetailResponseDTO();
        responseDTO.setUser(userResponse);
        responseDTO.setEmployee(employeeResponse);

        return responseDTO;
    }

}
