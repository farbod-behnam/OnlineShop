package com.OnlineShop.service;

import com.OnlineShop.entity.Product;
import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        if (productNameExists(product.getName()))
            throw new AlreadyExistsException("Category with name [" + product.getName() +"] already exists");

        // in order to register entity as a new record
        // the id should be null
        product.setId(null);

        String trimmedName = product.getName().trim();
        product.setName(trimmedName);

        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProduct(Product product)
    {
        Optional<Product> result = productRepository.findById(product.getId());

        if (result.isEmpty())
            throw new NotFoundException("Product with id: [" + product.getId() + "] cannot be found");

        String trimmedName = product.getName().trim();
        product.setName(trimmedName);

        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(String productId)
    {
        Optional<Product> result = productRepository.findById(productId);

        if (result.isEmpty())
            throw new NotFoundException("Product with id: [" + productId + "] cannot be found");

        productRepository.deleteById(productId);
    }

    @Override
    @Transactional
    public boolean productNameExists(String productName)
    {
        if (productName == null || productName.isBlank())
            throw new IllegalArgumentException("Product name cannot be empty");

        productName = productName.trim().strip().toLowerCase(Locale.ROOT);

        return productRepository.existsByNameIgnoreCase(productName);

    }
}
