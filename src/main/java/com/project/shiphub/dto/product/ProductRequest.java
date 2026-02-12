package com.project.shiphub.dto.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String nome;
    private BigDecimal preco;
    private String descricao;
    private Integer estoque;
    private String imagem;
    private String categoria;
    private Boolean destaque;
    private Boolean ativo;

}
