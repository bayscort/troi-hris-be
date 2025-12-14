package com.project.troyoffice.repository;

import com.project.troyoffice.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findByActiveTrue(Sort sort);

    List<User> findByRoleId(UUID roleId);

    List<User> findByRoleIdIn(List<UUID> roleIds);


}
