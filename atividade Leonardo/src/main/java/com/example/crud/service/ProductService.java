package com.example.crud.service;

import com.example.crud.domain.product.Product;
import com.example.crud.domain.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;


    public List<Product> getTop3Products() {
        List<Product> allProducts = repository.findAllByActiveTrue();
        return allProducts.stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }
}
