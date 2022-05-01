package com.OnlineShop.service;

import com.OnlineShop.repository.ICategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest
{
    private ICategoryService underTestCategoryService;

    @Mock
    private ICategoryRepository categoryRepository;


    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        underTestCategoryService = new CategoryService(categoryRepository);
    }


    @Test
    void findAll_returnCategoryList()
    {
        // given
        // when
        underTestCategoryService.getAllCategories();
        // then
        verify(categoryRepository).findAll();
    }

}