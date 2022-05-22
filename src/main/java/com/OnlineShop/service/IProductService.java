package com.OnlineShop.service;

import com.OnlineShop.dto.ProductDto;
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
    Product createProduct(ProductDto productDto);

    @Transactional
    Product updateProduct(ProductDto productDto);

    @Transactional
    void deleteProduct(String productId);

    @Transactional
    boolean productNameExists(String productName);
}
