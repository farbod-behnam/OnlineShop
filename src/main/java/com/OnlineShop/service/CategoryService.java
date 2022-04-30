package com.OnlineShop.service;

import com.OnlineShop.entity.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService implements ICategoryService
{
    @Transactional
    public List<Category> findAll()
    {
        return new ArrayList<>();
    }

    @Transactional
    public Category findById(String categoryId)
    {
        return new Category();
    }

    @Transactional
    public Category createCategory(Category category)
    {
        category.setId(null);

        return category;
    }
}
