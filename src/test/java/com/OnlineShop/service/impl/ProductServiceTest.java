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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest
{

    private IProductService underTestProductService;

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ICategoryService categoryService;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        underTestProductService = new ProductService(productRepository, categoryService);
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

        given(productRepository.existsByNameIgnoreCase(anyString())).willReturn(true);

        String productName = " Bloodborne ";


        // when
        boolean result = underTestProductService.productNameExists(productName);

        // then
        assertThat(result).isTrue();

    }


    @Test
    void productNameExists_shouldReturnFalse()
    {
        // given

        given(productRepository.existsByNameIgnoreCase(anyString())).willReturn(false);

        String productName = " devil may cry 5 ";

        // when
        boolean result = underTestProductService.productNameExists(productName);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void productNameExists_shouldThrowIllegalArgumentException()
    {
        // given

        String productName = "    ";

        // when

        // then
        assertThatThrownBy(() -> underTestProductService.productNameExists(productName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product name cannot be empty");
    }

    @Test
    void createProduct_shouldReturnAProduct()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        ProductRequest newProduct = new ProductRequest(
                "19",
                "Red Dead Redemption 2",
                "A Wild West Sandbox",
                price,
                19,
                "http://image_url",
                "11",
                true
        );

        Category category = new Category("11", "Video Games");

        given(categoryService.getCategoryById(anyString())).willReturn(category);
        given(productRepository.existsByNameIgnoreCase(anyString())).willReturn(false);
        // when
        underTestProductService.createProduct(newProduct);

        // then
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
//        verify(productRepository).existsByNameIgnoreCase(newProduct.getName());
        verify(productRepository).save(productArgumentCaptor.capture());
        Product capturedProduct = productArgumentCaptor.getValue();

        assertThat(capturedProduct.getName()).isEqualTo(newProduct.getName());
        assertThat(capturedProduct.getDescription()).isEqualTo(newProduct.getDescription());
        assertThat(capturedProduct.getPrice()).isEqualTo(newProduct.getPrice());
        assertThat(capturedProduct.getQuantity()).isEqualTo(newProduct.getQuantity());
        assertThat(capturedProduct.getImageUrl()).isEqualTo(newProduct.getImageUrl());
        assertThat(capturedProduct.isActive()).isEqualTo(newProduct.getActive());
    }

    @Test
    void createProduct_shouldThrowAlreadyExistsException()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        ProductRequest alreadyExistProduct = new ProductRequest(
                "19",
                "Bloodborne",
                "A Wild West Sandbox",
                price,
                19,
                "http://image_url",
                "11",
                true
        );


        given(productRepository.existsByNameIgnoreCase(alreadyExistProduct.getName())).willReturn(true);

//        given(categoryService.getCategoryById(anyString())).willReturn(new Category("11", "TV"));

        // when
//        underTestProductService.createProduct(alreadyExistProduct);

        // then
        assertThatThrownBy(() -> underTestProductService.createProduct(alreadyExistProduct))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Product with name [" + alreadyExistProduct.getName() +"] already exists");

    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");




        Category category = new Category("11", "Video Games");

        Product foundProduct = new Product(
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

        ProductRequest productDto = new ProductRequest(
                "19",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                "11",
                true
        );

        given(categoryService.getCategoryById(anyString())).willReturn(category);
        given(productRepository.findById(anyString())).willReturn(Optional.of(foundProduct));

        // when
        underTestProductService.updateProduct(productDto);

        // then
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());
        Product capturedProduct = productArgumentCaptor.getValue();

        assertThat(capturedProduct.getName()).isEqualTo(foundProduct.getName());
        assertThat(capturedProduct.getDescription()).isEqualTo(foundProduct.getDescription());
        assertThat(capturedProduct.getPrice()).isEqualTo(foundProduct.getPrice());
        assertThat(capturedProduct.getQuantity()).isEqualTo(foundProduct.getQuantity());
        assertThat(capturedProduct.getImageUrl()).isEqualTo(foundProduct.getImageUrl());
        assertThat(capturedProduct.isActive()).isEqualTo(foundProduct.isActive());
    }

    @Test
    void updateProduct_shouldThrowAlreadyExistsException()
    {
        // given
        Category category = new Category("11", "Video Games");


        ProductRequest productDto = new ProductRequest(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("69.99"),
                19,
                "http://image_url",
                "11",
                true
        );


        given(productRepository.existsByNameIgnoreCase(productDto.getName())).willReturn(true);

        // when

        // then
        assertThatThrownBy(() -> underTestProductService.updateProduct(productDto))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Product name: ["+ productDto.getName() +"] already exists");
    }

    @Test
    void updateProduct_shouldThrowNotFoundException()
    {
        // given
        Category category = new Category("11", "Video Games");


        ProductRequest productDto = new ProductRequest(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("69.99"),
                19,
                "http://image_url",
                "11",
                true
        );


        given(productRepository.findById(productDto.getId())).willReturn(Optional.empty());

        // when

        // then
        assertThatThrownBy(() -> underTestProductService.updateProduct(productDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Product with id: [" + productDto.getId() + "] cannot be found");
    }

    @Test
    void deleteCategory_shouldDeleteProduct()
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        Product deleteProduct = new Product(
                "11",
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

        given(productRepository.findById(anyString())).willReturn(Optional.of(deleteProduct));

        // when
        underTestProductService.deleteProduct(deleteProduct.getId());

        // then
        verify(productRepository).deleteById(deleteProduct.getId());
    }

    @Test
    void deleteCategory_shouldThrowNotFoundException()
    {
        // given
        Category category = new Category("11", "Video Games");

        Product notFoundProduct = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("69.99"),
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        given(productRepository.findById(notFoundProduct.getId())).willReturn(Optional.empty());

        // when

        // then
        assertThatThrownBy(() -> underTestProductService.deleteProduct(notFoundProduct.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Product with id: [" + notFoundProduct.getId() + "] cannot be found");
    }

    @Test
    void subtractProductQuantity_shouldReturnProductWithSubtractedQuantity()
    {
        // given
        Category category = new Category("11", "Video Games");

        Product foundProduct = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("69.99"),
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(productRepository.findById(anyString())).willReturn(Optional.of(foundProduct));

        // when
        Product subtractedQuantityProduct = underTestProductService.subtractProductQuantity(anyString(), 2);

        // then
        verify(productRepository).findById(anyString());
        assertThat(subtractedQuantityProduct).isEqualTo(foundProduct);

    }
    @Test
    void subtractProductQuantity_shouldThrowLimitExceedException()
    {
        // given
        Category category = new Category("11", "Video Games");

        Product foundProduct = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                new BigDecimal("69.99"),
                19,
                "http://image_url",
                category,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(productRepository.findById(anyString())).willReturn(Optional.of(foundProduct));

        int requestedQuantity = 20;
        // when

        // then
        assertThatThrownBy(() -> underTestProductService.subtractProductQuantity(anyString(), requestedQuantity))
                .isInstanceOf(LimitExceedException.class)
                .hasMessageContaining("Requested quantity: [" + requestedQuantity + "] for Product: [" + foundProduct.getName() +"] exceeds the limit of [" + foundProduct.getQuantity() + "]");

    }
}