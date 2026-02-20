package com.project.shiphub.dto.admin;

import lombok.Data;
import java.util.List;

@Data
public class AdminUserRequest {
    private String name;
    private String email;
    private String password;
    private List<String> permissions;
}