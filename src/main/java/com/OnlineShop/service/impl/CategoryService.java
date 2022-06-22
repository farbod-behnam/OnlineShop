package com.OnlineShop.service.impl;

import com.OnlineShop.entity.Category;
import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.ICategoryRepository;
import com.OnlineShop.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService
{

    private final ICategoryRepository categoryRepository;

    @Autowired
    public CategoryService(ICategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public List<Category> getCategories()
    {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category getCategoryById(String categoryId)
    {
        Optional<Category> result = categoryRepository.findById(categoryId);

        Category category;

        if (result.isPresent())
            category = result.get();
        else
            throw new NotFoundException("Category with id: [" + categoryId + "] cannot be found");

        return category;

    }

    @Override
    @Transactional
    public Category createCategory(Category category)
    {
        if (categoryNameExists(category.getName()))
            throw new AlreadyExistsException("Category with name [" + category.getName() +"] already exists");

        // in order to register entity as a new record
        // the id should be null
        category.setId(null);

        category.setName(category.getName().trim().strip());

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Category category)
    {
        Optional<Category> result = categoryRepository.findById(category.getId());

        if (result.isEmpty())
            throw new NotFoundException("Category with id: [" + category.getId() + "] cannot be found");

        String trimmedName = category.getName().trim();
        category.setName(trimmedName);

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String categoryId)
    {
        Optional<Category> result = categoryRepository.findById(categoryId);

        if (result.isEmpty())
            throw new NotFoundException("Category with id: [" + categoryId + "] cannot be found");

        categoryRepository.deleteById(categoryId);
    }


    @Override
    @Transactional
    public boolean categoryNameExists(String categoryName)
    {
        List<Category> categoryList = categoryRepository.findAll();

        categoryName = categoryName.toLowerCase(Locale.ROOT).trim();
        boolean categoryExists = false;

        for (Category c: categoryList)
        {
            if (c.getName().toLowerCase(Locale.ROOT).equals(categoryName))
            {
                categoryExists = true;
                break;
            }
        }

        return categoryExists;
    }
}
