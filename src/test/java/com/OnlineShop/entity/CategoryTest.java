package com.OnlineShop.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest
{
    private Validator validator;

    @BeforeEach
    void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void category_validCategory()
    {
        Category category = new Category("134", "f1");
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    public void category_emptyName_shouldNotValidate()
    {
        Category category = new Category("134", " ");
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(3);

//        for (ConstraintViolation<Category> violation : violations)
//        {
//            System.out.println("Violation Message: " + violation.getMessage());
//        }
    }

    @Test
    public void category_startWithNumber_shouldNotValidate()
    {
        Category category = new Category("134", "1fasdf");
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        for (ConstraintViolation<Category> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }
    }

    @Test
    public void category_startWithSpace_shouldNotValidate()
    {
        Category category = new Category("134", " fasdf");
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

//        for (ConstraintViolation<Category> violation : violations)
//        {
//            System.out.println("Violation Message: " + violation.getMessage());
//        }
    }

    @Test
    public void category_moreThan16Chars_shouldNotValidate()
    {
        Category category = new Category("134", "fasdf asdf lkjlk asdf lkjasd");
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

//        for (ConstraintViolation<Category> violation : violations)
//        {
//            System.out.println("Violation Message: " + violation.getMessage());
//        }
    }
}