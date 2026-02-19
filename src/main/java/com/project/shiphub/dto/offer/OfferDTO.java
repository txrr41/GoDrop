package com.project.shiphub.dto.offer;

import com.project.shiphub.model.offer.Offer;
import com.project.shiphub.model.offer.OfferStatus;
import com.project.shiphub.model.offer.OfferType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OfferDTO {
    private Long id;
    private String name;
    private String description;
    private OfferType type;
    private BigDecimal discountValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private OfferStatus status;
    private Boolean active;
    private String category;
    private Integer stockLimit;
    private Integer usedStock;
    private Integer priority;
    private LocalDateTime createdAt;
    private List<Long> productIds;
    private List<ProductSummary> products;
    private Boolean isCurrentlyActive;

    public OfferDTO(Offer offer) {
        this.id = offer.getId();
        this.name = offer.getName();
        this.description = offer.getDescription();
        this.type = offer.getType();
        this.discountValue = offer.getDiscountValue();
        this.startDate = offer.getStartDate();
        this.endDate = offer.getEndDate();
        this.status = offer.getStatus();
        this.active = offer.getActive();
        this.category = offer.getCategory();
        this.stockLimit = offer.getStockLimit();
        this.usedStock = offer.getUsedStock();
        this.priority = offer.getPriority();
        this.createdAt = offer.getCreatedAt();
        this.isCurrentlyActive = offer.isActive();

        if (offer.getProducts() != null) {
            this.productIds = offer.getProducts().stream()
                    .map(p -> p.getId())
                    .collect(Collectors.toList());

            this.products = offer.getProducts().stream()
                    .map(p -> new ProductSummary(p.getId(), p.getNome(), p.getPreco(), p.getImagem()))
                    .collect(Collectors.toList());
        }
    }

    @Data
    public static class ProductSummary {
        private Long id;
        private String nome;
        private BigDecimal preco;
        private String imagem;

        public ProductSummary(Long id, String nome, BigDecimal preco, String imagem) {
            this.id = id;
            this.nome = nome;
            this.preco = preco;
            this.imagem = imagem;
        }
    }
}