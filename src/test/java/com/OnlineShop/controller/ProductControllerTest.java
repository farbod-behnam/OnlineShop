package com.OnlineShop.controller;

import com.OnlineShop.dto.ProductDto;
import com.OnlineShop.entity.Category;
import com.OnlineShop.entity.Product;
import com.OnlineShop.service.IProductService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductService productService;

    @BeforeEach
    void setUp()
    {

        // -----------------------------------------------------
        // for comparison of BigDecimal this config is necessary
        // -----------------------------------------------------

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

        Configuration.setDefaults(new Configuration.Defaults() {

            private final JsonProvider jsonProvider = new JacksonJsonProvider(objectMapper);
            private final MappingProvider mappingProvider = new JacksonMappingProvider(objectMapper);

            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            @Override
            public MappingProvider mappingProvider() {
                return mappingProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
        // -----------------------------------------------------
        // end of configuration
        // -----------------------------------------------------

    }

    @Test
    public void getProducts_shouldReturnProductList() throws Exception
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

        given(productService.getProducts()).willReturn(productList);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(productList.size()))
                .andExpect(jsonPath("$[0].id").value(equalTo("19")))
                .andExpect(jsonPath("$[0].name").value(equalTo("Bloodborne")))
                .andExpect(jsonPath("$[0].price").value(equalTo(new BigDecimal("69.99"))))
                .andExpect(jsonPath("$[0].quantity").value(equalTo(19)))
                .andExpect(jsonPath("$[0].category.id").value(equalTo("11")))
                .andExpect(jsonPath("$[0].category.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$[0].active").value(equalTo(true)));

    }

    @Test
    public void getProduct_shouldReturnACategory() throws Exception
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

        String productId = "11";

        given(productService.getProductById(productId)).willReturn(product);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(equalTo("19")))
                .andExpect(jsonPath("$.name").value(equalTo("Bloodborne")))
                .andExpect(jsonPath("$.price").value(equalTo(new BigDecimal("69.99"))))
                .andExpect(jsonPath("$.quantity").value(equalTo(19)))
                .andExpect(jsonPath("$.category.id").value(equalTo("11")))
                .andExpect(jsonPath("$.category.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$.active").value(equalTo(true)))
                .andDo(print());

        // then
//        verify(categoryService, times(1)).findAll();
    }

    @Test
    public void createProduct_shouldReturnCreatedCategory() throws Exception
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        ProductDto productDto = new ProductDto(
                "19",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                "11",
                true
        );

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

        given(productService.createProduct(any(ProductDto.class))).willReturn(product);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .content(asJsonString(productDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(equalTo("19")))
                .andExpect(jsonPath("$.name").value(equalTo("Bloodborne")))
                .andExpect(jsonPath("$.price").value(equalTo(new BigDecimal("69.99"))))
                .andExpect(jsonPath("$.quantity").value(equalTo(19)))
                .andExpect(jsonPath("$.category.id").value(equalTo("11")))
                .andExpect(jsonPath("$.category.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$.active").value(equalTo(true)))
                .andDo(print());

    }

    @Test
    public void putProduct_shouldReturnUpdatedProduct() throws Exception
    {
        // given
        BigDecimal price = new BigDecimal("69.99");

        ProductDto productDto = new ProductDto(
                "19",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
                "11",
                true
        );

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

        given(productService.updateProduct(any(ProductDto.class))).willReturn(product);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/products")
                        .content(asJsonString(productDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(equalTo("19")))
                .andExpect(jsonPath("$.name").value(equalTo("Bloodborne")))
                .andExpect(jsonPath("$.price").value(equalTo(new BigDecimal("69.99"))))
                .andExpect(jsonPath("$.quantity").value(equalTo(19)))
                .andExpect(jsonPath("$.category.id").value(equalTo("11")))
                .andExpect(jsonPath("$.category.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$.active").value(equalTo(true)))
                .andDo(print());

    }

    @Test
    public void deleteProduct_ShouldReturnString() throws Exception
    {
        // given
        String productId = "11";

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/" + productId)
                        .content(asJsonString("Product with id: [" + productId + "] is deleted"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.").value(equalTo("Product with id: [" + productId + "] is deleted")))
                .andDo(print());

    }

    private String asJsonString(final Object obj)
    {
        try
        {
            final ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule()); // add jackson support for conversion of Date Time

            return mapper.writeValueAsString(obj);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}