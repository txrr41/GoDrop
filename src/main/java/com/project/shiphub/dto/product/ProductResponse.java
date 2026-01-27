package com.project.shiphub.dto.product;

import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductResponse {

    private Long id;
    private String nome;
    private BigDecimal preco;
    private String descricao;
    private Integer estoque;
    private LocalDateTime createdAt;

}
