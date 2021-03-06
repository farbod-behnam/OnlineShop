package com.OnlineShop.controller;

import com.OnlineShop.entity.Category;
import com.OnlineShop.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController
{
    private final ICategoryService categoryService;

    @Autowired
    public CategoryController(ICategoryService categoryService)
    {
        this.categoryService = categoryService;
    }


    @GetMapping
    public ResponseEntity<List<Category>> getCategories()
    {
        List<Category> categories = categoryService.getCategories();

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable String categoryId)
    {
        Category category = categoryService.getCategoryById(categoryId);

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category)
    {
        Category createdCategory = categoryService.createCategory(category);

        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Category> updateCategory(@Valid @RequestBody Category category)
    {
        Category updateCategory = categoryService.updateCategory(category);

        return new ResponseEntity<>(updateCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable String categoryId)
    {
        categoryService.deleteCategory(categoryId);

        return new ResponseEntity<>("Category with id: [" + categoryId + "] is deleted", HttpStatus.OK);
    }
}
