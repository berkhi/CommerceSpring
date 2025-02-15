package com.CommerceSpring.repository;

import com.CommerceSpring.entity.Role;
import com.CommerceSpring.views.GetAllRoleView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllByIdIn(List<Long> ids);

    @Query("select new com.CommerceSpring.views.GetAllRoleView(r.id,r.roleName,r.status,r.roleDescription) from Role r where (:searchWord is null or lower(r.roleName) like lower(concat('%', :searchWord, '%')) ) and r.roleName <> 'ADMIN' order by r.roleName ASC ")
    Page<GetAllRoleView> getAllRolesWithSearch(@Param("searchWord") String searchWord, Pageable pageable);


    Boolean existsByRoleNameIgnoreCase(String roleName);

    Optional<Role> findByRoleNameIgnoreCase(String roleName);
}