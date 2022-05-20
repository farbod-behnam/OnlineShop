package com.OnlineShop.dto;

import com.OnlineShop.entity.AppRole;
import com.OnlineShop.entity.AppUser;
import com.OnlineShop.entity.Country;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.enums.RoleEnum;
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
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
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

    @Test
    public void user_nullName_shouldNotValidate()
    {
        // given
        Set<String> roles = new HashSet<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);


        AppUserDto user = new AppUserDto(
                "19",
                null,
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserDto>> violations = validator.validate(user);

//        for (ConstraintViolation<AppUser> violation : violations)
//        {
//            System.out.println("Violation Message: " + violation.getMessage());
//        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserDto> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("first name is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void user_sizeNameLessThan3_shouldNotValidate()
    {
        // given
        Set<String> roles = new HashSet<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);


        AppUserDto user = new AppUserDto(
                "19",
                "Jo",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserDto>> violations = validator.validate(user);

//        for (ConstraintViolation<AppUser> violation : violations)
//        {
//            System.out.println("Violation Message: " + violation.getMessage());
//        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserDto> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("first name must be between 3 and 25 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
        assertThat(violation.getInvalidValue()).isEqualTo("Jo");
    }


    @Test
    public void user_sizeNameGreaterThan45_shouldNotValidate()
    {
        // given
        Set<String> roles = new HashSet<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);


        AppUserDto user = new AppUserDto(
                "19",
                "asdfasdfasdfgasdfasdfgasdf",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserDto>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserDto> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserDto> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("first name must be between 3 and 25 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
        assertThat(violation.getInvalidValue()).isEqualTo("asdfasdfasdfgasdfasdfgasdf");
    }

    @Test
    public void user_nameContainsSpecialCharactersOrNumbers_shouldNotValidate()
    {
        // given
        Set<String> roles = new HashSet<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserDto user = new AppUserDto(
                "19",
                "John @!",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserDto>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserDto> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserDto> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("first name should only contain alphabet character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
        assertThat(violation.getInvalidValue()).isEqualTo("John @!");
    }

    @Test
    public void user_phoneNumberContainsAlphabetOrSpecialCharacters_shouldNotValidate()
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
                "00166 fasd!@$",
                "johnwick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserDto>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserDto> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserDto> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("phone number must only contain number");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("phoneNumber");
        assertThat(violation.getInvalidValue()).isEqualTo("00166 fasd!@$");
    }


    @Test
    public void user_phoneNumberHasAWrongCountryCode_shouldNotValidate()
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
                "00461111111111111",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserDto>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserDto> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserDto> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("phone number is invalid");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("phoneNumber");
        assertThat(violation.getInvalidValue()).isEqualTo("00461111111111111");
    }

    @Test
    public void user_phoneNumberHasWrongLengthForCountryCode_shouldNotValidate()
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
                "0049999999",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserDto>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserDto> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserDto> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("phone number is invalid");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("phoneNumber");
        assertThat(violation.getInvalidValue()).isEqualTo("0049999999");
    }

}