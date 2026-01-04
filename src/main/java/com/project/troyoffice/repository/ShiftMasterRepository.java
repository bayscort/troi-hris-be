package com.project.troyoffice.repository;

import com.project.troyoffice.model.ShiftMaster;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShiftMasterRepository extends JpaRepository<ShiftMaster, UUID> {
    List<ShiftMaster> findByClientId(UUID clientId, Sort sort);

}
