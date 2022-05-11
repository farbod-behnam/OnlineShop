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
        Category category = new Category("134", "f12");
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    public void category_nullName_shouldNotValidate()
    {
        Category category = new Category("134", null);
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

//        for (ConstraintViolation<Category> violation : violations)
//        {
//            System.out.println("Violation Message: " + violation.getMessage());
//        }

        ConstraintViolation<Category> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("name is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void category_blankName_shouldNotValidate()
    {
        Category category = new Category("   ", null);
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

//        for (ConstraintViolation<Category> violation : violations)
//        {
//            System.out.println("Violation Message: " + violation.getMessage());
//        }

        ConstraintViolation<Category> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("name is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void category_nameStartWithNumber_shouldNotValidate()
    {
        Category category = new Category("134", "1fasdf");
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

//        for (ConstraintViolation<Category> violation : violations)
//        {
//            System.out.println("Violation Message: " + violation.getMessage());
//        }

        ConstraintViolation<Category> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("name should only contain chars/digits and not start with digit");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getInvalidValue()).isEqualTo("1fasdf");
    }

    @Test
    public void category_nameSpecialCharacter_shouldNotValidate()
    {
        Category category = new Category("134", "f@!asdf");
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

//        for (ConstraintViolation<Category> violation : violations)
//        {
//            System.out.println("Violation Message: " + violation.getMessage());
//        }

        ConstraintViolation<Category> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("name should only contain chars/digits and not start with digit");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getInvalidValue()).isEqualTo("f@!asdf");
    }


    @Test
    public void category_lessThan3Chars_shouldNotValidate()
    {
        Category category = new Category("134", "fa");
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

//        for (ConstraintViolation<Category> violation : violations)
//        {
//            System.out.println("Violation Message: " + violation.getMessage());
//        }

        ConstraintViolation<Category> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("name must be between 3 and 16 characters");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getInvalidValue()).isEqualTo("fa");
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

        ConstraintViolation<Category> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("name must be between 3 and 16 characters");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getInvalidValue()).isEqualTo("fasdf asdf lkjlk asdf lkjasd");
    }
}