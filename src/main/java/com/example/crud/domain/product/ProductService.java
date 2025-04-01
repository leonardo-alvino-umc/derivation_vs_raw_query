package com.example.crud.domain.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class ProductService {

    @Autowired

    private ProductRepository repository;

    public List<Product> getTop3MostExpensiveProducts() {

        List<Product> allProducts = repository.findAllByActiveTrue();


        return allProducts.stream()

                .sorted((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()))

                .limit(3)

                .collect(Collectors.toList());

    }

}

