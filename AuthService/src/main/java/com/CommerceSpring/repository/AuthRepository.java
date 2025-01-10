package com.CommerceSpring.repository;

import com.CommerceSpring.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthRepository extends JpaRepository<Auth, UUID> {
    boolean existsByEmail(String email);

    Optional<Auth> findOptionalByEmail(String email);

    @Query("SELECT a.email FROM Auth a WHERE a.id = :authId")
    String findEmailById(@Param("authId") UUID authId);

    Boolean existsByEmailIgnoreCase(String email);
}

