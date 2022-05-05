package com.OnlineShop.service;

import com.OnlineShop.entity.Category;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICategoryService
{
    @Transactional
    List<Category> getCategories();

    @Transactional
    Category getCategoryById(String categoryId);

    @Transactional
    Category createCategory(Category category);

    @Transactional
    Category updateCategory(Category category);

    @Transactional
    void deleteCategory(String categoryId);

    @Transactional
    boolean categoryNameExists(String categoryName);
}
