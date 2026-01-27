package com.project.shiphub.repository.product;

import com.project.shiphub.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findAllByAtivoTrue();
    Optional<Product> findByIdAndAtivoTrue(Long id);

}
