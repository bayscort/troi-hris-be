package com.project.troyoffice.repository;

import com.project.troyoffice.model.ShiftPattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShiftPatternRepository extends JpaRepository<ShiftPattern, UUID> {

}
