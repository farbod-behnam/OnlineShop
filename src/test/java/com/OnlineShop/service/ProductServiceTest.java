package com.OnlineShop.service;

import com.OnlineShop.entity.Category;
import com.OnlineShop.entity.Product;
import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.IProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Test
    void getProductById_shouldReturnAProduct()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        Product product = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(productRepository.findById(anyString())).willReturn(Optional.of(product));

        // when
        Product foundProduct = underTestProductService.getProductById(anyString());

        // then
        verify(productRepository).findById(anyString());
        assertThat(foundProduct.getId()).isEqualTo(product.getId());
        assertThat(foundProduct.getName()).isEqualTo(product.getName());
    }

    @Test
    void getProductById_shouldThrowNotFoundException()
    {
        // given
        String productId = "11";

        // when

        // then
        assertThatThrownBy(() -> underTestProductService.getProductById(productId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Product with id: [" + productId + "] cannot be found");

    }

    @Test
    void productNameExists_shouldReturnTrue()
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

        String productName = " bloodborne ";


        // when
        boolean result = underTestProductService.productNameExists(productName);

        // then
        assertThat(result).isTrue();

    }


    @Test
    void categoryNameExists_shouldReturnFalse()
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

        String productName = " devil may cry 5 ";
        // when
        boolean result = underTestProductService.productNameExists(productName);
        // then
        assertThat(result).isFalse();
    }

    @Test
    void createProduct_shouldReturnAProduct()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        Product newProduct = new Product(
                "19",
                "Red Dead Redemption 2",
                "A Wild West Sandbox",
                price,
                19,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        List<Product> productList = new ArrayList<>();

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
        underTestProductService.createProduct(newProduct);

        // then
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).findAll();
        verify(productRepository).save(productArgumentCaptor.capture());
        Product capturedProduct = productArgumentCaptor.getValue();
        assertThat(capturedProduct).isEqualTo(newProduct);
    }

    @Test
    void createProduct_shouldThrowAlreadyExistsException()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        Product alreadyExistProduct = new Product(
                "19",
                "Bloodborne",
                "A Wild West Sandbox",
                price,
                19,
                "http://image_url",
                new Category("11", "Video Games"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        List<Product> productList = new ArrayList<>();

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

        // then
        assertThatThrownBy(() -> underTestProductService.createProduct(alreadyExistProduct))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Category with name [" + alreadyExistProduct.getName() +"] already exists");

    }
}