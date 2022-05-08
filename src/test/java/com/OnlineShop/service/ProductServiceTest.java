package com.OnlineShop.service;

import com.OnlineShop.entity.Category;
import com.OnlineShop.entity.Product;
import com.OnlineShop.repository.IProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest
{

    private IProductService underTestProductService;

    @Mock
    private IProductRepository productRepository;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        underTestProductService = new ProductService(productRepository);
    }

    @Test
    void getProducts_shouldReturnProductList()
    {
        // given
        List<Product> productList = new ArrayList<>();

        BigDecimal price = new BigDecimal("69.99");
        Category category = new Category("11", "Video Games");

        Product product1 = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Product product2 = new Product(
                "19",
                "The Last of Us",
                "A narrative game with action sequences",
                price,
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        productList.add(product1);
        productList.add(product2);

        given(productRepository.findAll()).willReturn(productList);
        // when
        underTestProductService.getProducts();

        // then
        verify(productRepository).findAll();
    }
}