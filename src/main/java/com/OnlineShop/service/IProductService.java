package com.OnlineShop.service;

import com.OnlineShop.entity.Product;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IProductService
{
    @Transactional
    List<Product> getProducts();

    @Transactional
    Product getProductById(String productId);

    @Transactional
    Product createProduct(Product product);

    @Transactional
    Product updateProduct(Product product);

    @Transactional
    void deleteProduct(String productId);
}
