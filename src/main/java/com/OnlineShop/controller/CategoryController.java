package com.OnlineShop.controller;

import com.OnlineShop.entity.Category;
import com.OnlineShop.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController
{
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }


    @GetMapping
    public ResponseEntity<List<Category>> getCategories()
    {
        List<Category> categories = categoryService.findAll();

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
