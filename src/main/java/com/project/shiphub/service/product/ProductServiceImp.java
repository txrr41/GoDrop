package com.project.shiphub.service.product;

import com.project.shiphub.dto.product.ProductRequest;
import com.project.shiphub.dto.product.ProductResponse;
import com.project.shiphub.model.product.Product;
import com.project.shiphub.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setNome(request.getNome());
        product.setPreco(request.getPreco());
        product.setDescricao(request.getDescricao());
        product.setEstoque(request.getEstoque());
        product.setAtivo(request.getAtivo());
        Product savedProduct = productRepository.save(product);
        return new ProductResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAllByAtivoTrue();
        return products.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id){
        Product product = productRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return new ProductResponse(product);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        product.setNome(request.getNome());
        product.setPreco(request.getPreco());
        product.setDescricao(request.getDescricao());
        product.setEstoque(request.getEstoque());
        Product updatedProduct = productRepository.save(product);
        return new ProductResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if(productRepository.existsById(id) && productRepository.findByIdAndAtivoTrue(id).isPresent()) {
            Product product = productRepository.findById(id).get();
            product.setAtivo(false);
            productRepository.save(product);
        } else {
            throw new RuntimeException("Produto não encontrado");

        }
    }

}
