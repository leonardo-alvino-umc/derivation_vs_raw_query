package com.example.crud.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findAllByActiveTrue();
    List<Product> findByCategoryAndActiveTrue(String category);

    @Query("SELECT p FROM com.example.crud.domain.product.Product p WHERE p.active = true ORDER BY p.category, p.price ASC")
    List<Product> findAllGroupedByCategoryOrderedByPrice();

}