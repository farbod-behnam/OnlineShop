package com.OnlineShop.service;

import com.OnlineShop.dto.ProductDto;
import com.OnlineShop.entity.Category;
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
    private final ICategoryService categoryService;

    @Autowired
    public ProductService(IProductRepository productRepository, ICategoryService categoryService)
    {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
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
    public Product createProduct(ProductDto productDto)
    {
        if (productNameExists(productDto.getName()))
            throw new AlreadyExistsException("Product with name [" + productDto.getName() +"] already exists");

        productDto.setName(productDto.getName().trim().strip());
        productDto.setDescription(productDto.getDescription().trim().strip());

        Category category = categoryService.getCategoryById(productDto.getCategoryId());

        Product product = new Product(
                null, // in order to register entity as a new record
                productDto.getName(),
                productDto.getDescription(),
                productDto.getPrice(),
                productDto.getQuantity(),
                productDto.getImageUrl(),
                category,
                productDto.getActive(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProduct(ProductDto productDto)
    {
        productDto.setName(productDto.getName().trim().strip());
        productDto.setDescription(productDto.getDescription().trim().strip());

        if (productNameExists(productDto.getName()))
            throw new AlreadyExistsException("Product name: ["+ productDto.getName() +"] already exists");

        Optional<Product> result = productRepository.findById(productDto.getId());

        if (result.isEmpty())
            throw new NotFoundException("Product with id: [" + productDto.getId() + "] cannot be found");

        Product foundProduct = result.get();




        Category category = categoryService.getCategoryById(productDto.getCategoryId());

        Product product = new Product(
                foundProduct.getId(),
                productDto.getName(),
                productDto.getDescription(),
                productDto.getPrice(),
                productDto.getQuantity(),
                productDto.getImageUrl(),
                category,
                productDto.getActive(),
                foundProduct.getCreatedAt(),
                LocalDateTime.now()
        );

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

        return productRepository.existsByNameIgnoreCase(productName);

    }
}
