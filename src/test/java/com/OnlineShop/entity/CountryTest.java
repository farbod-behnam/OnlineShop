package com.OnlineShop.entity;

import com.OnlineShop.enums.CountryEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class CountryTest
{

    private Validator validator;

    @BeforeEach
    void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void country_voidCountry()
    {
        // given
        Country country = new Country("19", CountryEnum.Germany.toString());

        // when

        // then
        Set<ConstraintViolation<Country>> violations = validator.validate(country);

        for (ConstraintViolation<Country> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    public void country_nullName()
    {
        // given
//        Country country = new Country("19", CountryEnum.Germany.toString());
        Country country = new Country("19", null);

        // when

        // then
        Set<ConstraintViolation<Country>> violations = validator.validate(country);

        for (ConstraintViolation<Country> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<Country> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("name is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }
}