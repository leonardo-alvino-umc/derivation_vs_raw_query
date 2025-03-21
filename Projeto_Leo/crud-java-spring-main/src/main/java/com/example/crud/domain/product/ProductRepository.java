package com.example.crud.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findAllByActiveTrue();

    List<Product> findAllByCategory(String category);

    @Query(value = "SELECT * FROM product WHERE id = :id", nativeQuery = true)
    List<Product> findAllById(@Param("id") String id);

    @Query("SELECT p FROM Product p ORDER BY p.category.name, p.price")
    List<Product> findAllOrderByCategoryAndPrice();
}
