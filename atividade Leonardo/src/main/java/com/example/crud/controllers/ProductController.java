package com.example.crud.controllers;

import com.example.crud.domain.product.Product;
import com.example.crud.domain.product.ProductRepository;
import com.example.crud.domain.product.RequestCategory;
import com.example.crud.domain.product.RequestProduct;
import com.example.crud.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produto")
public class ProductController {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private ProductService productService;


    @GetMapping
    public ResponseEntity getAllProducts(){
        var allProducts = repository.findAllByActiveTrue();
        return ResponseEntity.ok(allProducts);
    }

    // Endpoint 1 chamando o findByCategory no repository
    @GetMapping("/category")
    public ResponseEntity getProductsByCategory(@RequestParam String category) {
        var products = repository.findByCategory(category);
        return ResponseEntity.ok(products);
    }

    // Endpoint 2 adicionando o PathVariable puxando o Id do produto
    @GetMapping("/{id}")
    public ResponseEntity getProductById(@PathVariable String id) {
        var product = repository.findById(id);
        return ResponseEntity.ok(product);
    }

    // Endpoint 3 Retornar os 3 produtos mais caros chamando a logica da classe ProductService no package service
    @GetMapping("/top3produtos")
    public ResponseEntity getTop3Products() {
        List<Product> top3Products = productService.getTop3Products();
        return ResponseEntity.ok(top3Products);
    }

    @PostMapping
    public ResponseEntity registerProduct(@RequestBody @Valid RequestProduct data){
        Product newProduct = new Product(data);
        repository.save(newProduct);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity updateProduct(@RequestBody @Valid RequestProduct data){
        Optional<Product> optionalProduct = repository.findById(data.id());
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(data.name());
            product.setPrice(data.price());
            return ResponseEntity.ok(product);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteProduct(@PathVariable String id){
        Optional<Product> optionalProduct = repository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setActive(false);
            return ResponseEntity.noContent().build();
        } else {
            throw new EntityNotFoundException();
        }
    }

}
