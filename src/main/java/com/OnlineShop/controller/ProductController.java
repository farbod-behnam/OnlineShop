package com.OnlineShop.controller;

import com.OnlineShop.entity.Product;
import com.OnlineShop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
