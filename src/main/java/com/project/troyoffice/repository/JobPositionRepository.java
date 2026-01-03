package com.project.troyoffice.repository;

import com.project.troyoffice.model.JobPosition;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JobPositionRepository extends JpaRepository<JobPosition, UUID> {

    // 1. Standar (Client tidak diload, Lazy)
    // Cocok untuk dropdown list yang cuma butuh ID dan Nama Jabatan
    List<JobPosition> findByClient_Id(UUID clientId);

    // 2. Optimized (Client di-load sekaligus dalam 1 query)
    // Cocok untuk halaman Detail Jabatan yang butuh info lengkap Client
    // attributePaths = "client" artinya field 'client' diubah jadi EAGER khusus query ini
    @EntityGraph(attributePaths = "client")
    List<JobPosition> findAll();

    // Atau pakai JPQL manual:
    @Query("SELECT j FROM JobPosition j JOIN FETCH j.client WHERE j.isActive = true")
    List<JobPosition> findAllWithClient();
}
