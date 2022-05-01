package com.OnlineShop.service;

import com.OnlineShop.entity.Category;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICategoryService
{
    @Transactional
    List<Category> getAllCategories();

    @Transactional
    Category getCategoryById(String categoryId);

    @Transactional
    Category createCategory(Category category);

    Category updateCategory(Category category);

    void deleteCategory(String categoryId);
}
