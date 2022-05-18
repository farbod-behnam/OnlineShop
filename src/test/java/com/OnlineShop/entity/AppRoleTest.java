package com.OnlineShop.entity;

import com.OnlineShop.enums.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class AppRoleTest
{
    private Validator validator;

    @BeforeEach
    void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void role_validRole()
    {
        // given
        AppRole role = new AppRole("19", RoleEnum.ROLE_USER.name());

        // when

        // then
        Set<ConstraintViolation<AppRole>> violations = validator.validate(role);

        for (ConstraintViolation<AppRole> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    public void role_nullName_shouldNotValidate()
    {
        // given
//        AppRole role = new AppRole("19", RoleEnum.ROLE_USER.name());
        AppRole role = new AppRole("19", null);

        // when

        // then
        Set<ConstraintViolation<AppRole>> violations = validator.validate(role);

        for (ConstraintViolation<AppRole> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppRole> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("name is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }


}