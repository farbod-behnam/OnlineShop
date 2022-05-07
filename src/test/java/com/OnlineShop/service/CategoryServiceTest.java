package com.OnlineShop.service;

import com.OnlineShop.entity.Category;
import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.ICategoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
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
        Category category = new Category("11", "Video Games");
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

        // when
        assertThatThrownBy(() -> underTestCategoryService.getCategoryById(categoryId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Category with id: [" + categoryId + "] cannot be found");

        // then

    }

    @Test
    void createCategory_shouldReturnACategory()
    {
        // given
        Category category = new Category("11", "Video Games");

        // when
        underTestCategoryService.createCategory(category);

        // then
        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).findAll();
        verify(categoryRepository).save(categoryArgumentCaptor.capture());
        Category capturedCategory = categoryArgumentCaptor.getValue();
        assertThat(capturedCategory).isEqualTo(category);
    }


    @Test
    void createCategory_shouldThrowAlreadyExistsException()
    {
        // given
        List<Category> categoryList = new ArrayList<>();

        categoryList.add(new Category("11", "Video Games"));
        categoryList.add(new Category("12", "Clothes"));

        given(categoryRepository.findAll()).willReturn(categoryList);

        Category category = new Category("134", " video games ");

        // when

        // then
        assertThatThrownBy(() -> underTestCategoryService.createCategory(category))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Category with name [" + category.getName() +"] already exists");

    }

    @Test
    void updateCategory_returnUpdatedCategory()
    {
        // given
        Category category = new Category("11", "Video Games");
        // when
        underTestCategoryService.updateCategory(category);
        // then
        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());
        Category capturedCategory = categoryArgumentCaptor.getValue();
        assertThat(capturedCategory).isEqualTo(category);
    }

    @Test
    void deleteCategory()
    {
        // given
        Category category = new Category("11", "Video Games");
        given(categoryRepository.findById(anyString())).willReturn(Optional.of(category));
        // when
        underTestCategoryService.deleteCategory(category.getId());
        // then
        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
//        verify(categoryRepository).delete(any(Category.class));
        verify(categoryRepository).delete(categoryArgumentCaptor.capture());
        Category capturedCategory = categoryArgumentCaptor.getValue();
        assertThat(capturedCategory).isEqualTo(category);
    }

    @Test
    void categoryNameExists_shouldReturnTrue()
    {

        // given
        List<Category> categoryList = new ArrayList<>();

        categoryList.add(new Category("11", "Video Games"));
        categoryList.add(new Category("12", "Clothes"));

        String categoryName = " video games ";

        given(categoryRepository.findAll()).willReturn(categoryList);

        // when
        boolean result = underTestCategoryService.categoryNameExists(categoryName);

        // then
        assertThat(result).isTrue();

    }


    @Test
    void categoryNameExists_shouldReturnFalse()
    {
        // given
        String categoryName = "category";
        // when
        boolean result = underTestCategoryService.categoryNameExists(categoryName);
        // then
        assertThat(result).isFalse();
    }

}