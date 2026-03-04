// ─── DropperStoreRequest.java ─────────────────────────────────────────────────
package com.project.shiphub.dto.store;

import lombok.Data;
import java.util.List;

@Data
public class DropperStoreRequest {
    private String storeName;
    private String description;
    private String logoUrl;
    private String bannerUrl;
    private String primaryColor;
    private String secondaryColor;
    private String backgroundColor;
    private String slogan;
    private String theme;
    private List<Long> productIds;
    private String aiPrompt;
}