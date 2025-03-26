package com.example.crud.controllers;

import com.example.crud.domain.product.Product;
import com.example.crud.domain.product.ProductRepository;
import com.example.crud.domain.product.RequestCategory;
import com.example.crud.domain.product.RequestProduct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductRepository repository;

    @GetMapping //endpoint1
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required = false) String category){
        if (category != null) {
            return ResponseEntity.ok(repository.findByCategoryAndActiveTrue(category));
        }
        return ResponseEntity.ok(repository.findAllByActiveTrue());
    }

    @GetMapping("/{id}") //endpoint2
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> optionalProduct = repository.findById(id);
        return optionalProduct.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/top3-expensive") //endpoint3
    public ResponseEntity<List<Product>> getTop3ExpensiveProducts() {
        List<Product> top3 = repository.findAllByActiveTrue()//pegar todos os produtos ativos
                .stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed()) //ordena do mais caro pro mais barto
                .limit(3)
                .collect(Collectors.toList());
        return ResponseEntity.ok(top3);
    }

    @GetMapping("/grouped-sql")
    public ResponseEntity<List<Product>> getProductsGroupedByCategorySQL() {
        return ResponseEntity.ok(repository.findAllGroupedByCategoryOrderedByPrice());
    }
    @GetMapping("/grouped-java")
    public ResponseEntity<Map<String, List<Product>>> getProductsGroupedByCategoryJava() {
        List<Product> allProducts = repository.findAllByActiveTrue();

        Map<String, List<Product>> grouped = allProducts.stream()
                .sorted(Comparator.comparing(Product::getPrice)) // ordena por preço
                .collect(Collectors.groupingBy(Product::getCategory)); // agrupa por categoria

        return ResponseEntity.ok(grouped);
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
