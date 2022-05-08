package com.OnlineShop.service;

import com.OnlineShop.entity.Product;
import com.OnlineShop.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements IProductService
{
    private final IProductRepository productRepository;

    @Autowired
    public ProductService(IProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public List<Product> getProducts()
    {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public Product getProductById(String productId)
    {
        return new Product();
    }

    @Override
    @Transactional
    public Product createProduct(Product product)
    {
        return new Product();
    }

    @Override
    @Transactional
    public Product updateProduct(Product product)
    {
        return new Product();
    }

    @Override
    @Transactional
    public void deleteProduct(String productId)
    {

    }
}
