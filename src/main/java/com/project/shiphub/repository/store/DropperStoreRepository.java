package com.project.shiphub.repository.store;

import com.project.shiphub.model.store.DropperStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DropperStoreRepository extends JpaRepository<DropperStore, Long> {

    Optional<DropperStore> findBySlug(String slug);

    Optional<DropperStore> findByDropperProfileId(Long dropperProfileId);

    Optional<DropperStore> findByDropperProfileUserId(Long userId);

    boolean existsBySlug(String slug);

    @Query("SELECT s FROM DropperStore s WHERE s.slug = :slug AND s.active = true")
    Optional<DropperStore> findActiveBySlug(@Param("slug") String slug);
}