package com.OnlineShop.controller;

import com.OnlineShop.entity.Category;
import com.OnlineShop.service.ICategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController
{
    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService)
    {
        this.categoryService = categoryService;
    }


    @GetMapping
    public ResponseEntity<List<Category>> getCategories()
    {
        List<Category> categories = categoryService.findAll();

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable String categoryId)
    {
        Category category = categoryService.findById(categoryId);

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category)
    {
        Category createdCategory = categoryService.createCategory(category);

        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }
}
