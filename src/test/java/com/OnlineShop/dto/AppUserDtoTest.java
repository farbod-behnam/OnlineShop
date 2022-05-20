package com.OnlineShop.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AppUserDtoTest
{
    private Validator validator;

    @BeforeEach
    void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }


    @Test
    public void user_validUser()
    {
        // given
        Set<String> roles = new HashSet<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserDto user = new AppUserDto(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUserDto>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserDto> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isTrue();
    }
}