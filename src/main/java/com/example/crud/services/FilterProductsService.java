package com.example.crud.services;

import com.example.crud.domain.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilterProductsService {

    public List<Product> perPrice(List<Product> products, int limit) {
        return products
                .stream()
                .sorted((o1, o2) -> o2.getPrice().compareTo(o1.getPrice()))
                .limit(limit)
                .collect(Collectors.toList());
    }

//    guilherme solutions
    public Map<String, List<Product>> groupedByCategoryAndOrderedByPrice(List<Product> products) {
        return products
                .stream()
                .sorted((o1, o2) -> o2.getPrice().compareTo(o1.getPrice()))
                .collect(Collectors.groupingBy(Product::getCategory));
    }

//    datovo solutions
    public List<Product> groupedByCategoryAndOrderedByPrice2(List<Product> products) {
        var productsList = products
                .stream()
                .sorted((o1, o2) -> o2.getPrice().compareTo(o1.getPrice()))
                .toList();

        return productsList
                .stream()
                .sorted((o1, o2) -> o2.getCategory().compareTo(o1.getCategory()))
                .toList();
    }

}
