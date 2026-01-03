package com.project.troyoffice.repository;

import com.project.troyoffice.model.JobReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobReferenceRepository extends JpaRepository<JobReference, UUID> {
}
