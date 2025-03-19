package com.example.crud;

import com.example.crud.domain.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilterProducts {
    private List<Product> products;
    private int limit;

    public FilterProducts() {
    }

    public FilterProducts(List<Product> products, int limit) {
        this.products = products;
        this.limit = limit;
    }

    public List<Product> perPrice() {
        return products
                .stream()
                .sorted((o1, o2) -> o2.getPrice().compareTo(o1.getPrice()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
