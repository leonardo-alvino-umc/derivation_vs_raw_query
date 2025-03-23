package com.example.crud.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findAllByActiveTrue();
    List<Product> findAllByCategory(String category);
    @Query(value = "SELECT * FROM product WHERE id = ? ", nativeQuery = true)
    Optional<Product> findByIdRawQuery(String id);

    @Query(value = """
        SELECT p.*
        FROM product p
        ORDER BY p.category, p.price
    """, nativeQuery = true)
    List<Product> findAllGroupedByCategoryAndOrderedByPrice();
}