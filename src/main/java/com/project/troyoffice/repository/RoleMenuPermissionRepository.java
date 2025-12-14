package com.project.troyoffice.repository;

import com.project.troyoffice.model.Role;
import com.project.troyoffice.model.RoleMenuPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RoleMenuPermissionRepository extends JpaRepository<RoleMenuPermission, UUID> {
    List<RoleMenuPermission> findByRole(Role role);

    @Modifying
    @Query("DELETE FROM RoleMenuPermission rmp WHERE rmp.role.id = :roleId")
    void deleteByRoleId(UUID roleId);

}
