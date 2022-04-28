package com.OnlineShop.controller;

import com.OnlineShop.entity.Category;
import com.OnlineShop.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class CategoryControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;


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

}
