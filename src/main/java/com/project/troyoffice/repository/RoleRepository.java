package com.project.troyoffice.repository;

import com.project.troyoffice.model.Role;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    List<Role> findByActiveTrue(Sort sort);


    Optional<Role> findByName(String name);
}
