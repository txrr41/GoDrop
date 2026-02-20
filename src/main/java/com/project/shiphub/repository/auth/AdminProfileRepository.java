package com.project.shiphub.repository.auth;

import com.project.shiphub.model.auth.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminProfileRepository extends JpaRepository<AdminProfile, Long> {

    Optional<AdminProfile> findByUserId(Long userId);

    @Query("SELECT ap FROM AdminProfile ap WHERE ap.user.email = :email")
    Optional<AdminProfile> findByUserEmail(@Param("email") String email);

    boolean existsByUserId(Long userId);
}