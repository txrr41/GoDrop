package com.project.shiphub.dto.offer;

import com.project.shiphub.model.offer.OfferType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OfferRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    private String description;

    @NotNull(message = "Tipo de desconto é obrigatório")
    private OfferType type;

    @NotNull(message = "Valor do desconto é obrigatório")
    @Positive(message = "Valor do desconto deve ser positivo")
    private BigDecimal discountValue;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDateTime startDate;

    @NotNull(message = "Data de fim é obrigatória")
    private LocalDateTime endDate;

    private Boolean active = true;

    private String category;

    private Integer stockLimit;

    private Integer priority = 0;

    private List<Long> productIds;
}