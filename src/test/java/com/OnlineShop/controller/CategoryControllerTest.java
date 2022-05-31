package com.OnlineShop.controller;

import com.OnlineShop.OnlineShopApplication;
import com.OnlineShop.entity.Category;
import com.OnlineShop.entity.Product;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.security.SecurityConfig;
import com.OnlineShop.security.service.ITokenService;
import com.OnlineShop.security.userdetails.UserDetailsServiceImpl;
import com.OnlineShop.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CategoryController.class)
//@Import(SecurityConfig.class)
public class CategoryControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICategoryService categoryService;

    // need to be mocked because SecurityConfig.class injects
    // these two services ( UserDetailsService, ITokenService ) into itself
    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private ITokenService tokenService;



    @Test
    public void getCategories_returnsEmptyCategoryList() throws Exception
    {
        // given
        List<Category> categoryList = new ArrayList<>();

        given(categoryService.getCategories()).willReturn(categoryList);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(categoryList.size()));

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

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(categoryList.size()))
                .andExpect(jsonPath("$[0].id").value(equalTo("11")))
                .andExpect(jsonPath("$[0].name").value(equalTo("Video Games")));

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

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(categoryList.size()))
                .andExpect(jsonPath("$[0].id").value(equalTo("11")));
    }

    @Test
    public void getCategory_returnsACategory() throws Exception
    {
        // given
        Category category = new Category("11", "Video Games");
        String categoryId = "11";

        given(categoryService.getCategoryById(categoryId)).willReturn(category);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(equalTo("11")))
                .andExpect(jsonPath("$.name").value(equalTo("Video Games")))
                .andDo(print());

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

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(equalTo("11")))
                .andExpect(jsonPath("$.name").value(equalTo("Video Games")))
                .andDo(print());

    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void postCategory_authorized_returnsCreatedCategory() throws Exception
    {
        // given
        Category category = new Category("11", "Video Games");

        given(categoryService.createCategory(any(Category.class))).willReturn(category);

        // when

        // then
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

    }

    @Test
    public void postCategory_shouldBeUnauthorized() throws Exception
    {
        // given
        Category category = new Category("11", "Video Games");

        given(categoryService.createCategory(any(Category.class))).willReturn(category);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .content(asJsonString(category))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void postCategory_shouldBeUnauthorizedByUser() throws Exception
    {
        // given
        Category category = new Category("11", "Video Games");

        given(categoryService.createCategory(any(Category.class))).willReturn(category);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .content(asJsonString(category))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());

    }


    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void putCategory_authorized_returnsUpdatedCategory() throws Exception
    {
        // given
        Category category = new Category("11", "Video Games");

        given(categoryService.updateCategory(any(Category.class))).willReturn(category);

        // when

        // then
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

    }

    @Test
    public void putCategory_shouldBeUnauthorized() throws Exception
    {
        // given
        Category category = new Category("11", "Video Games");

        given(categoryService.updateCategory(any(Category.class))).willReturn(category);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/categories")
                        .content(asJsonString(category))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void putCategory_shouldBeUnauthorizedByUser() throws Exception
    {
        // given
        Category category = new Category("11", "Video Games");

        given(categoryService.updateCategory(any(Category.class))).willReturn(category);

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/categories")
                        .content(asJsonString(category))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void deleteCategory_authorized_ShouldReturnString() throws Exception
    {
        // given
        String categoryId = "11";

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/categories/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(equalTo("Category with id: [" + categoryId + "] is deleted")))
                .andDo(print());

    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    public void deleteCategory_shouldBeUnauthorizedByUser() throws Exception
    {
        // given
        String categoryId = "11";


        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/categories/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString("Category with id: [" + categoryId + "] is deleted")))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    public void deleteCategory_shouldBeUnauthorized() throws Exception
    {
        // given
        String categoryId = "11";

        // when

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/categories/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());

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
