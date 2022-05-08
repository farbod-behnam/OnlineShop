package com.OnlineShop.controller;

import com.OnlineShop.entity.Product;
import com.OnlineShop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController
{
    private final IProductService productService;

    public ProductController(IProductService productService)
    {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts()
    {
        List<Product> products = productService.getProducts();

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable String productId)
    {
        Product product = productService.getProductById(productId);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product)
    {
        Product createdProduct = productService.createProduct(product);

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@RequestBody Product product)
    {
        Product updatedProduct = productService.updateProduct(product);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
