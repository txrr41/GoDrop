package com.project.shiphub.dto.store;

import lombok.Data;
import java.math.BigDecimal;
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
    private String aiPrompt;

    private List<StoreProductItem> products;
    @Data
    public static class StoreProductItem {
        private Long productId;
        private BigDecimal customPrice;
    }
}