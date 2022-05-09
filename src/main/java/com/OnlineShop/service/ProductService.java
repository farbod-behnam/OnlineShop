package com.OnlineShop.service;

import com.OnlineShop.entity.Product;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
        Optional<Product> result = productRepository.findById(productId);

        Product product;

        if (result.isPresent())
            product = result.get();
        else
            throw new NotFoundException("Product with id: [" + productId + "] cannot be found");

        return product;
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

    @Override
    @Transactional
    public boolean productNameExists(String productName)
    {
        List<Product> productList = productRepository.findAll();

        productName = productName.toLowerCase(Locale.ROOT).trim();

        boolean categoryExists = false;

        for (Product p: productList)
        {
            if (p.getName().toLowerCase(Locale.ROOT).equals(productName))
            {
                categoryExists = true;
                break;
            }
        }

        return categoryExists;
    }
}
