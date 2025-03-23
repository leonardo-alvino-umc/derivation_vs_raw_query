package com.example.crud.controllers;

import com.example.crud.domain.product.Product;
import com.example.crud.domain.product.ProductRepository;
import com.example.crud.domain.product.RequestCategory;
import com.example.crud.domain.product.RequestProduct;
import com.example.crud.services.FilterProductsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private FilterProductsService filterProducts;

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

        List<Product> productsFiltered = this.filterProducts.perPrice(productList, 3);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productsFiltered);
    }

    @GetMapping("/price-category-bd")
    public ResponseEntity findAllGroupedByCategoryAndOrderedByPriceFromBd() {
        List<Product> productList = repository.findAllGroupedByCategoryAndOrderedByPrice();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productList);
    }

    @GetMapping("/price-category-app")
    public ResponseEntity findAllGroupedByCategoryAndOrderedByPriceFromApp() {
        List<Product> productList = repository.findAll();

        Map<String, List<Product>> filteredProducts = filterProducts.groupedByCategoryAndOrderedByPrice(productList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(filteredProducts);
    }

    @GetMapping("/price-category-app2")
    public ResponseEntity findAllGroupedByCategoryAndOrderedByPriceFromApp2() {
        List<Product> productList = repository.findAll();

        List<Product> filteredProducts = filterProducts.groupedByCategoryAndOrderedByPrice2(productList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(filteredProducts);
    }
}
