package com.project.shiphub.dto.auth;

import com.project.shiphub.model.auth.AdminProfile;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.model.auth.UserRole;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AuthResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String role;
    private final List<String> permissions;
    private final Boolean suspended;

    public AuthResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.permissions = new ArrayList<>();
        this.suspended = false;
    }

    public AuthResponse(User user, AdminProfile adminProfile) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.suspended = adminProfile != null ? adminProfile.getSuspended() : false;

        if (user.getRole() == UserRole.OWNER) {
            this.permissions = List.of("catalog", "orders", "customers");
        } else if (adminProfile != null) {
            this.permissions = adminProfile.getPermissionList();
        } else {
            this.permissions = new ArrayList<>();
        }
    }
}