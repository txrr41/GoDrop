// ─── AdminUserDTO.java ────────────────────────────────────────────────────────
package com.project.shiphub.dto.admin;

import com.project.shiphub.model.auth.AdminProfile;
import com.project.shiphub.model.auth.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdminUserDTO {
    private Long id;
    private String name;
    private String email;
    private String type;
    private List<String> permissions;
    private Boolean suspended;
    private String suspensionReason;

    public AdminUserDTO(User user, AdminProfile profile) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.type = user.getRole().name().toLowerCase();
        this.suspended = profile != null ? profile.getSuspended() : false;
        this.suspensionReason = profile != null ? profile.getSuspensionReason() : null;

        if (profile != null) {
            this.permissions = profile.getPermissionList();
        } else {
            this.permissions = new ArrayList<>();
        }
    }
}