package com.OnlineShop.controller;

import com.OnlineShop.entity.Category;
import com.OnlineShop.entity.Product;
import com.OnlineShop.service.CategoryService;
import com.OnlineShop.service.ICategoryService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class CategoryControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICategoryService categoryService;

    @BeforeEach
    void setUp()
    {

        // -----------------------------------------------------
        // for comparison of BigDecimal this config is necessary
        // -----------------------------------------------------

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.USE_LONG_FOR_INTS);
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
    public void getCategories_returnsEmptyCategoryList() throws Exception
    {
        // given
        List<Category> categoryList = new ArrayList<>();

        given(categoryService.findAll()).willReturn(categoryList);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(categoryList.size()));

        // then
//        verify(categoryService, times(1)).findAll();
    }


    @Test
    public void getCategories_returnsCategoryList() throws Exception
    {
        // given
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("11", "Video Games", null));
        categoryList.add(new Category("12", "Clothes", null));

        given(categoryService.findAll()).willReturn(categoryList);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(categoryList.size()))
                .andExpect(jsonPath("$[0].id").value(equalTo("11")))
                .andExpect(jsonPath("$[0].name").value(equalTo("Video Games")));

        // then
//        verify(categoryService, times(1)).findAll();
    }

    @Test
    public void getCategories_returnsCategoryListWithProducts() throws Exception
    {
        // given
        List<Category> categoryList = new ArrayList<>();
        List<Product> productList = new ArrayList<>();

        BigDecimal price = new BigDecimal("69.99");

        Product product1 = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
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
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        productList.add(product1);
        productList.add(product2);

        categoryList.add(new Category("11", "Video Games", productList));
        categoryList.add(new Category("12", "Clothes", new ArrayList<>()));

        given(categoryService.findAll()).willReturn(categoryList);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(categoryList.size()))
                .andExpect(jsonPath("$[0].id").value(equalTo("11")))
                .andExpect(jsonPath("$[0].products").isArray())
                .andExpect(jsonPath("$[0].products.length()").value(productList.size()))
                .andExpect(jsonPath("$[0].products[0].id").value(equalTo("19")))
//                .andExpect(jsonPath("$[0].products[0].price", Matchers.comparesEqualTo(price.doubleValue())))
                .andExpect(jsonPath("$[0].products[0].price").value(equalTo(price)))
                .andExpect(jsonPath("$[1].products").isArray())
                .andExpect(jsonPath("$[1].products.length()").value(0));
        // then
//        verify(categoryService, times(1)).findAll();
    }

    @Test
    public void getCategory_returnsACategory() throws Exception
    {
        // given
        Category category = new Category("11", "Video Games", null);
        String categoryId = "11";

        given(categoryService.findById(categoryId)).willReturn(category);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(equalTo("11")))
                .andExpect(jsonPath("$.name").value(equalTo("Video Games")))
                .andDo(print());

        // then
//        verify(categoryService, times(1)).findAll();
    }

    @Test
    public void getCategory_returnsACategoryWithProducts() throws Exception
    {
        // given
        List<Product> productList = new ArrayList<>();

        BigDecimal price = new BigDecimal("69.99");

        Product product1 = new Product(
                "19",
                "Bloodborne",
                "A souls like game",
                price,
                19,
                "http://image_url",
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
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        productList.add(product1);
        productList.add(product2);

        Category category = new Category("11", "Video Games", productList);
        String categoryId = "11";

        given(categoryService.findById(categoryId)).willReturn(category);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(equalTo("11")))
                .andExpect(jsonPath("$.name").value(equalTo("Video Games")))
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products[0].id").value(equalTo("19")))
                .andExpect(jsonPath("$.products[0].name").value(equalTo("Bloodborne")))
                .andExpect(jsonPath("$.products[0].price").value(equalTo(price)))
                .andDo(print());

        // then
//        verify(categoryService, times(1)).findAll();
    }

    @Test
    public void postCategory_returnsCreatedCategory() throws Exception
    {
        // given
        Category category = new Category("11", "Video Games", null);

        given(categoryService.createCategory(category)).willReturn(category);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .content(asJsonString(category))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$.id").value(equalTo("11")))
                .andExpect(jsonPath("$.name").value(equalTo("Video Games")))
                .andDo(print());

        // then
//        verify(categoryService, times(1)).findAll();
    }

    private String asJsonString(final Object obj)
    {
        try
        {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
