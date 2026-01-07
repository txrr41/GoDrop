package com.project.shiphub.login.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String mensagem;
    private Long userId;
    private String email;
    private String token;
}
