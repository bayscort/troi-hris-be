package com.project.troyoffice.repository;

import com.project.troyoffice.model.ShiftPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ShiftPatternRepository extends JpaRepository<ShiftPattern, UUID> {

    @Query("SELECT p FROM ShiftPattern p " +
            "LEFT JOIN FETCH p.items i " +
            "LEFT JOIN FETCH i.shiftMaster sm " +
            "WHERE p.client.id = :clientId " +
            "ORDER BY p.name ASC, i.daySequence ASC")
    List<ShiftPattern> findAllByClientId(@Param("clientId") UUID clientId);

}
