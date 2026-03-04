package com.project.shiphub.dto.store;

import lombok.Data;

@Data
public class AiThemeResponse {
    private String storeName;
    private String slogan;
    private String primaryColor;
    private String secondaryColor;
    private String backgroundColor;
    private String theme;
    private String description;
    private String reasoning;
}