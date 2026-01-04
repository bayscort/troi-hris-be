package com.project.troyoffice.service;

import com.project.troyoffice.dto.JobReferenceDTO;
import com.project.troyoffice.mapper.JobReferenceMapper;
import com.project.troyoffice.model.JobReference;
import com.project.troyoffice.repository.JobReferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class JobReferenceService {

    private final JobReferenceRepository jobReferenceRepository;
    private final JobReferenceMapper jobReferenceMapper;

    public UUID create(JobReferenceDTO req) {

        JobReference jr = JobReference.builder()
                .code(req.getCode())
                .name(req.getName())
                .description(req.getDescription())
                .active(true)
                .build();
        return jobReferenceRepository.save(jr).getId();
    }

    @Transactional(readOnly = true)
    public List<JobReferenceDTO> findAll() {
        final List<JobReference> jobReferenceList = jobReferenceRepository.findAll(Sort.by("id"));
        return jobReferenceList.stream()
                .map(jobReferenceMapper::toDTO)
                .toList();
    }

}
