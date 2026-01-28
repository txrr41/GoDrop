package com.project.shiphub.dto.product;

import com.project.shiphub.model.product.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductResponse {

    private Long id;
    private String nome;
    private BigDecimal preco;
    private String descricao;
    private Integer estoque;
    private String imagem;
    private Boolean ativo;
    private LocalDateTime createdAt;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.nome = product.getNome();
        this.preco = product.getPreco();
        this.descricao = product.getDescricao();
        this.estoque = product.getEstoque();
        this.ativo = product.getAtivo();
        this.imagem = product.getImagem();
    }

}
