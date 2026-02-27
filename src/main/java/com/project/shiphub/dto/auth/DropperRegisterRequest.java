package com.project.shiphub.dto.auth;

import lombok.Data;

@Data
public class DropperRegisterRequest {
    private String cnpj;
    private String storeName;
    private String whatsapp;
}
