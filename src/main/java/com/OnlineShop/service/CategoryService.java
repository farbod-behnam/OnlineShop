package com.OnlineShop.service;

import com.OnlineShop.entity.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService
{
    public List<Category> findAll()
    {
        return new ArrayList<>();
    }

    public Category findById(String categoryId)
    {
        return new Category();
    }
}
