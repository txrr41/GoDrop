package com.project.shiphub.model.auth;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admin_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "permissions", columnDefinition = "TEXT")
    private String permissions = "";

    @Column(nullable = false)
    private Boolean suspended = false;

    @Column(name = "suspension_reason")
    private String suspensionReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Helpers
    public List<String> getPermissionList() {
        if (permissions == null || permissions.isBlank()) return new ArrayList<>();
        return new ArrayList<>(List.of(permissions.split(",")));
    }

    public void setPermissionList(List<String> perms) {
        this.permissions = perms == null ? "" : String.join(",", perms);
    }

    public boolean hasPermission(String perm) {
        return getPermissionList().contains(perm);
    }
}