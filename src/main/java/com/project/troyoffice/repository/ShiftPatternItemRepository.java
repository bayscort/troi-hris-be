package com.project.troyoffice.repository;

import com.project.troyoffice.model.ShiftPatternItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShiftPatternItemRepository extends JpaRepository<ShiftPatternItem, UUID> {

}
