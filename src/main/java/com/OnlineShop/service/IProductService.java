package com.OnlineShop.service;

import com.OnlineShop.dto.request.ProductRequest;
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
    Product createProduct(ProductRequest productDto);

    @Transactional
    Product updateProduct(ProductRequest productDto);

    @Transactional
    void deleteProduct(String productId);

    @Transactional
    boolean productNameExists(String productName);
}
