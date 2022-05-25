package com.OnlineShop.controller;

import com.OnlineShop.dto.request.ProductRequest;
import com.OnlineShop.entity.Product;
import com.OnlineShop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController
{
    private final IProductService productService;

    @Autowired
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
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequest product)
    {
        Product createdProduct = productService.createProduct(product);

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody ProductRequest productDto)
    {
        Product updatedProduct = productService.updateProduct(productDto);

        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable String productId)
    {
        productService.deleteProduct(productId);

        return new ResponseEntity<>("Product with id: [" + productId + "] is deleted", HttpStatus.OK);
    }
}
