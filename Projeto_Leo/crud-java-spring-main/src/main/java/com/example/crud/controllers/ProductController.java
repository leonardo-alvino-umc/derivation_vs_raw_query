package com.example.crud.controllers;

import com.example.crud.domain.product.Product;
import com.example.crud.domain.product.ProductRepository;
import com.example.crud.domain.product.RequestProduct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")

public class ProductController {
    @Autowired
    private ProductRepository repository;
    @GetMapping
    public ResponseEntity getAllProducts(){
        var allProducts = repository.findAllByActiveTrue();
        return ResponseEntity.ok(allProducts);
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

    @RequestMapping("/category")
    @GetMapping
    public ResponseEntity getAllProductsByCategory(@RequestParam String category){
        var allProducts = repository.findAllByCategory(category);
        return ResponseEntity.ok(allProducts);
    }

    @RequestMapping("/id")
    @GetMapping
    public ResponseEntity getAllProductsById(@RequestParam String id){
        var allProducts = repository.findAllById(id);
        return ResponseEntity.ok(allProducts);
    }
    @GetMapping("/top3")
    public List<Product> getTop3Products(){
        return repository.findAll().stream().
                sorted(Comparator.comparing(Product::getPrice).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    @GetMapping("/grouped")
    public List<Product> getGroupedProducts() {
        return repository.findAllOrderByCategoryAndPrice();
    }
}
