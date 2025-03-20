package com.example.crud.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findAllByActiveTrue();

    List<Product>findByCategory(String category);

    @Query(value = "SELECT p FROM product p WHERE p.id = :id")
    Product findOne(@Param("id") String id);

    @Query(value = "SELECT p FROM product p WHERE p.active = true ORDER BY p.category, p.price asc")
    List<Product> findAllGroupByCategoryOrderByPrice();

}