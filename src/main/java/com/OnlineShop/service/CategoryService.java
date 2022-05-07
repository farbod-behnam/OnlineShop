package com.OnlineShop.service;

import com.OnlineShop.entity.Category;
import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.ICategoryRepository;
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

    @Transactional
    public List<Category> getCategories()
    {
        return categoryRepository.findAll();
    }

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

    @Transactional
    public Category createCategory(Category category)
    {
        if (categoryNameExists(category.getName()))
            throw new AlreadyExistsException("Category with name [" + category.getName() +"] already exists");

        // in order to register entity as a new record
        // the id should be null
        category.setId(null);
        String trimmedName = category.getName().trim();
        category.setName(trimmedName);

        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category)
    {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String categoryId)
    {
        Category foundCategory = getCategoryById(categoryId);

        categoryRepository.delete(foundCategory);
    }


    @Override
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
