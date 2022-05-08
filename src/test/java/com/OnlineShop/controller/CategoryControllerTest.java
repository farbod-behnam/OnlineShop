package com.OnlineShop.controller;

import com.OnlineShop.entity.Category;
import com.OnlineShop.entity.Product;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class CategoryControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICategoryService categoryService;



    @Test
    public void getCategories_returnsEmptyCategoryList() throws Exception
    {
        // given
        List<Category> categoryList = new ArrayList<>();

        given(categoryService.getCategories()).willReturn(categoryList);

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
        categoryList.add(new Category("11", "Video Games"));
        categoryList.add(new Category("12", "Clothes"));

        given(categoryService.getCategories()).willReturn(categoryList);

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


        categoryList.add(new Category("11", "Video Games"));
        categoryList.add(new Category("12", "Clothes"));

        given(categoryService.getCategories()).willReturn(categoryList);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(categoryList.size()))
                .andExpect(jsonPath("$[0].id").value(equalTo("11")));
        // then
//        verify(categoryService, times(1)).findAll();
    }

    @Test
    public void getCategory_returnsACategory() throws Exception
    {
        // given
        Category category = new Category("11", "Video Games");
        String categoryId = "11";

        given(categoryService.getCategoryById(categoryId)).willReturn(category);

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

        Category category = new Category("11", "Video Games");
        String categoryId = "11";

        given(categoryService.getCategoryById(categoryId)).willReturn(category);

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
    public void postCategory_returnsCreatedCategory() throws Exception
    {
        // given
        Category category = new Category("11", "Video Games");

        given(categoryService.createCategory(any(Category.class))).willReturn(category);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .content(asJsonString(category))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.id").value(equalTo("11")))
                .andExpect(jsonPath("$.name").value(equalTo("Video Games")))
                .andDo(print());

        // then
//        verify(categoryService, times(1)).findAll();
    }

    @Test
    public void putCategory_returnsUpdatedCategory() throws Exception
    {
        // given
        Category category = new Category("11", "Video Games");

        given(categoryService.updateCategory(any(Category.class))).willReturn(category);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/categories")
                        .content(asJsonString(category))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.id").value(equalTo("11")))
                .andExpect(jsonPath("$.name").value(equalTo("Video Games")))
                .andDo(print());

        // then
//        verify(categoryService, times(1)).findAll();
    }

    @Test
    public void deleteCategory_returnsUpdatedCategory() throws Exception
    {
        // given
        Category category = new Category("11", "Video Games");

//        given(categoryService.deleteCategory(anyString());)

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/categories/" + category.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").value(equalTo("Category with id: [" + category.getId() + "] is deleted")))
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
