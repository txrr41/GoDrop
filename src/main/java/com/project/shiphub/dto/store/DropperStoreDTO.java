package com.project.shiphub.dto.store;

import com.project.shiphub.model.store.DropperStore;
import com.project.shiphub.model.product.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class DropperStoreDTO {

    private Long id;
    private String slug;
    private String storeName;
    private String description;
    private String logoUrl;
    private String bannerUrl;
    private String primaryColor;
    private String secondaryColor;
    private String backgroundColor;
    private String slogan;
    private String theme;
    private Boolean active;
    private String dropperName;
    private String dropperLevel;
    private LocalDateTime createdAt;
    private List<StoreProductDTO> products;

    public DropperStoreDTO(DropperStore store) {
        this.id = store.getId();
        this.slug = store.getSlug();
        this.storeName = store.getStoreName();
        this.description = store.getDescription();
        this.logoUrl = store.getLogoUrl();
        this.bannerUrl = store.getBannerUrl();
        this.primaryColor = store.getPrimaryColor();
        this.secondaryColor = store.getSecondaryColor();
        this.backgroundColor = store.getBackgroundColor();
        this.slogan = store.getSlogan();
        this.theme = store.getTheme();
        this.active = store.getActive();
        this.createdAt = store.getCreatedAt();
        this.dropperName = store.getDropperProfile().getUser().getName();
        this.dropperLevel = store.getDropperProfile().getLevel().name();

        this.products = store.getProducts().stream()
                .map(StoreProductDTO::new)
                .collect(Collectors.toList());
    }

    @Data
    public static class StoreProductDTO {
        private Long id;
        private String nome;
        private BigDecimal preco;
        private String descricao;
        private String imagem;
        private String categoria;
        private Integer estoque;

        public StoreProductDTO(Product product) {
            this.id = product.getId();
            this.nome = product.getNome();
            this.preco = product.getPreco();
            this.descricao = product.getDescricao();
            this.imagem = product.getImagem();
            this.categoria = product.getCategoria();
            this.estoque = product.getEstoque();
        }
    }
}