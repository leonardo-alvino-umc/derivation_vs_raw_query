package com.example.crud.controllers;

import com.example.crud.FilterProducts;
import com.example.crud.domain.product.Product;
import com.example.crud.domain.product.ProductRepository;
import com.example.crud.domain.product.RequestCategory;
import com.example.crud.domain.product.RequestProduct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductRepository repository;
    private FilterProducts filterProducts;

    @GetMapping
    public ResponseEntity getAllProducts(){
        List<Product> allProducts = repository.findAll();
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

//    NEW
    @GetMapping("/category")
    public ResponseEntity findProductByCategory(@RequestParam RequestCategory category) {
        List<Product> productList = this.repository.findAllByCategory(category.category());

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(productList);
    }

    @GetMapping("{id}")
    public ResponseEntity findProductByTitle(@PathVariable String id) {
        Product product = repository.findByIdRawQuery(id).orElseThrow(EntityNotFoundException::new);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(product);
    }

    @GetMapping("/price")
    public ResponseEntity findThreeMostExpensiveProduct() {
        List<Product> productList = this.repository.findAll();
        this.filterProducts.setProducts(productList);
        this.filterProducts.setLimit(3);

        List<Product> productsFiltered = this.filterProducts.perPrice();

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(productsFiltered);
    }
}
