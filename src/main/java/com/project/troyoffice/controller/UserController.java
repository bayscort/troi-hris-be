package com.project.troyoffice.controller;

import com.project.troyoffice.dto.UserRequestDTO;
import com.project.troyoffice.dto.UserResponseDTO;
import com.project.troyoffice.model.User;
import com.project.troyoffice.service.UserService;
import com.project.troyoffice.util.UserContext;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserContext userContext;


    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> create(@RequestBody UserRequestDTO dto) {
        final UUID createdId = userService.create(dto);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> update(@PathVariable(name = "id") UUID id,
                                       @RequestBody UserRequestDTO dto) {
        userService.update(id, dto);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserInfo() {
        User user = userContext.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/id")
    public ResponseEntity<?> getCurrentUserId() {
        UUID userId = userContext.getUserId();
        return ResponseEntity.ok(userId);
    }

}
