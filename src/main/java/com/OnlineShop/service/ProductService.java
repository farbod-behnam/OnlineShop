package com.OnlineShop.service;

import com.OnlineShop.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements IProductService
{
    @Override
    @Transactional
    public List<Product> getProducts()
    {
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public Product getProductById(String productId)
    {
        return null;
    }

    @Override
    @Transactional
    public Product createProduct(Product product)
    {
        return null;
    }

    @Override
    @Transactional
    public Product updateProduct(Product product)
    {
        return null;
    }

    @Override
    @Transactional
    public void deleteProduct(String productId)
    {

    }
}
