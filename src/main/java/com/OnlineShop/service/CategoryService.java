package com.OnlineShop.service;

import com.OnlineShop.entity.Category;
import com.OnlineShop.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService implements ICategoryService
{

    private final ICategoryRepository categoryRepository;

    @Autowired
    public CategoryService(ICategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public List<Category> getAllCategories()
    {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category getCategoryById(String categoryId)
    {
        return new Category();
    }

    @Transactional
    public Category createCategory(Category category)
    {
        return new Category();
    }

    @Override
    public Category updateCategory(Category category)
    {
        return new Category();
    }

    @Override
    public void deleteCategory(String categoryId)
    {
        String test = "just a test";
    }
}
