package com.project.troyoffice.repository;

import com.project.troyoffice.model.ClientSite;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClientSiteRepository extends JpaRepository<ClientSite, UUID> {

    List<ClientSite> findByActiveTrue(Sort sort);

    List<ClientSite> findByClientIdAndActiveTrue(UUID clientId, Sort sort);
}
