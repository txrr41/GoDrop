package com.project.shiphub.dto.auth;

import com.project.shiphub.model.auth.User;
import lombok.Getter;

@Getter
public class AuthResponse {

    private final Long id;
    private final String name;
    private final String email;


    public AuthResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

}