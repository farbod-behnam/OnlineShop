package com.OnlineShop.controller;

import com.OnlineShop.entity.Product;
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
    @GetMapping
    public ResponseEntity<List<Product>> getProducts()
    {
        List<Product> products = new ArrayList<>();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
