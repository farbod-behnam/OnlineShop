package com.OnlineShop.service.impl;

import com.OnlineShop.dto.request.ProductRequest;
import com.OnlineShop.entity.Category;
import com.OnlineShop.entity.Product;
import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.LimitExceedException;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.IProductRepository;
import com.OnlineShop.service.ICategoryService;
import com.OnlineShop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.LimitExceededException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
    public List<Product> getProducts()
    {
        return productRepository.findAll();
    }

    @Override
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
    public Product createProduct(ProductRequest productDto)
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
    public Product updateProduct(ProductRequest productDto)
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
    public void deleteProduct(String productId)
    {
        Optional<Product> result = productRepository.findById(productId);

        if (result.isEmpty())
            throw new NotFoundException("Product with id: [" + productId + "] cannot be found");

        productRepository.deleteById(productId);
    }

    @Override
    public boolean productNameExists(String productName)
    {
        if (productName == null || productName.isBlank())
            throw new IllegalArgumentException("Product name cannot be empty");

        return productRepository.existsByNameIgnoreCase(productName);

    }

    @Override
    public Product subtractProductQuantity(String productId, Integer quantity)
    {
        Product product = getProductById(productId);

        if (product.getQuantity() < quantity)
            throw new LimitExceedException("Requested quantity: [" + quantity + "] for Product: [" + product.getName() +"] exceeds the limit of [" + product.getQuantity() + "]");


        product.setQuantity(product.getQuantity() - quantity);

        return product;
    }
}
