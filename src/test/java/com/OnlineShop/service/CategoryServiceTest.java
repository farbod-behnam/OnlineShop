package com.OnlineShop.service;

import com.OnlineShop.entity.Category;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.ICategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
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
    void getCategories_returnCategoryList()
    {
        // given
        // when
        underTestCategoryService.getCategories();
        // then
        verify(categoryRepository).findAll();
    }

    @Test
    void getCategoryById_shouldReturnACategory()
    {
        // given
        Category category = new Category("11", "Video Games", null);
        given(categoryRepository.findById(anyString())).willReturn(Optional.of(category));

        // when
        Category foundCategory = underTestCategoryService.getCategoryById(anyString());

        // then
        verify(categoryRepository).findById(anyString());
        assertThat(foundCategory.getId()).isEqualTo(category.getId());
        assertThat(foundCategory.getName()).isEqualTo(category.getName());
    }

    @Test
    void getCategoryById_shouldThrowNotFoundException()
    {
        // given
        String categoryId = "11";
        given(categoryRepository.findById(categoryId)).willThrow(new NotFoundException("Category with id: [" + categoryId + "] not found"));

        // when
        assertThatThrownBy(() -> underTestCategoryService.getCategoryById(categoryId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Category with id: [" + categoryId + "] not found");

        // then

    }



}