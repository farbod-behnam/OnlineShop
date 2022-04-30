package com.OnlineShop.service;

import com.OnlineShop.entity.Category;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICategoryService
{
    @Transactional
    List<Category> findAll();

    @Transactional
    Category findById(String categoryId);

    @Transactional
    Category createCategory(Category category);

    Category updateCategory(Category category);

    void deleteCategory(String categoryId);
}
