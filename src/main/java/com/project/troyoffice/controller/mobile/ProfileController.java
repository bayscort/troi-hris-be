package com.project.troyoffice.controller.mobile;

import com.project.troyoffice.dto.ProfileDetailResponseDTO;
import com.project.troyoffice.dto.ProfileRequestDTO;
import com.project.troyoffice.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/detail")
    public ResponseEntity<ProfileDetailResponseDTO> getDetail() {
        return ResponseEntity.ok(profileService.getDetail());
    }

    @PostMapping("/create")
    public ResponseEntity<ProfileDetailResponseDTO> create(@RequestBody ProfileRequestDTO requestDTO) throws BadRequestException {
        log.info("Request received to create new profile for user: {}", requestDTO.getUser().getUsername());
        ProfileDetailResponseDTO createdProfile = profileService.create(requestDTO);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

}
