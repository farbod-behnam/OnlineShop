package com.OnlineShop.dto.request;


import com.OnlineShop.dto.request.AppUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AppUserRequestTest
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
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
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
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    public void user_nullName_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);


        AppUserRequest user = new AppUserRequest(
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
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

//        for (ConstraintViolation<AppUserDto> violation : violations)
//        {
//            System.out.println("Violation Message: " + violation.getMessage());
//        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("first name is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void user_sizeNameLessThan3_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);


        AppUserRequest user = new AppUserRequest(
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
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

//        for (ConstraintViolation<AppUserDto> violation : violations)
//        {
//            System.out.println("Violation Message: " + violation.getMessage());
//        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("first name must be between 3 and 25 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
        assertThat(violation.getInvalidValue()).isEqualTo("Jo");
    }


    @Test
    public void user_sizeNameGreaterThan45_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);


        AppUserRequest user = new AppUserRequest(
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
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("first name must be between 3 and 25 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
        assertThat(violation.getInvalidValue()).isEqualTo("asdfasdfasdfgasdfasdfgasdf");
    }

    @Test
    public void user_nameContainsSpecialCharactersOrNumbers_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
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
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("first name should only contain alphabet character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
        assertThat(violation.getInvalidValue()).isEqualTo("John @!");
    }

    @Test
    public void user_phoneNumberContainsAlphabetOrSpecialCharacters_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
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
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("phone number must only contain number");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("phoneNumber");
        assertThat(violation.getInvalidValue()).isEqualTo("00166 fasd!@$");
    }


    @Test
    public void user_phoneNumberHasAWrongCountryCode_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
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
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("phone number is invalid");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("phoneNumber");
        assertThat(violation.getInvalidValue()).isEqualTo("00461111111111111");
    }

    @Test
    public void user_phoneNumberHasWrongLengthForCountryCode_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
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
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("phone number is invalid");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("phoneNumber");
        assertThat(violation.getInvalidValue()).isEqualTo("0049999999");
    }



    @Test
    public void user_nullEmail_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "0016666666666",
                null,
                roles,
                "johnwick", // j.wick
                "Password1234!",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("email is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void user_invalidEmail_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "hello!@#$@124",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("email is invalid");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
        assertThat(violation.getInvalidValue()).isEqualTo("hello!@#$@124");
    }

    @Test
    public void user_nullRole_shouldNotValidate()
    {
        // given

        String countryId = "10";


        AppUserRequest user = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                null,
                "johnwick", // j.wick
                "Password1234!",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("role is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("roleIdList");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void user_roleListSizeGreaterThan2_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId1 = "9";
        String roleId2 = "10";
        String roleId3 = "11";

        roles.add(roleId1);
        roles.add(roleId2);
        roles.add(roleId3);

        AppUserRequest user = new AppUserRequest(
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
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("user must have at least one role and at max two roles");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("roleIdList");
        assertThat(violation.getInvalidValue()).isEqualTo(roles);
    }

    @Test
    public void user_nullUsername_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                null,
                "Password1234!",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("username is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("username");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void user_usernameLessThan3_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "ja",
                "Password1234!",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("username must be between 3 and 24 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("username");
        assertThat(violation.getInvalidValue()).isEqualTo("ja");
    }

    @Test
    public void user_usernameGreaterThan24_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "fasdfasdfasdfasdfasdfasdf",
                "Password1234!",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("username must be between 3 and 24 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("username");
        assertThat(violation.getInvalidValue()).isEqualTo("fasdfasdfasdfasdfasdfasdf");
    }

    @Test
    public void user_invalidUsername_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "hello1234fasdfjlkjsdf", // hello1234fasdfjlkjsdf - how.1234
                "Password1234!",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("username is invalid");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("username");
        assertThat(violation.getInvalidValue()).isEqualTo("hello1234fasdfjlkjsdf");
    }

    @Test
    public void user_passwordLessThan10_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "hello123",
                "Passw0rd!",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("password must be greater than 10 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
        assertThat(violation.getInvalidValue()).isEqualTo("Passw0rd!");
    }

    @Test
    public void user_passwordGreaterThan64_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "hello123",
                "Passw0rd!1234passwordpasswordpassword1234@@  !#Passw0rd!1234passwordpasswordpassword1234@@  !#",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("password must be greater than 10 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
        assertThat(violation.getInvalidValue()).isEqualTo("Passw0rd!1234passwordpasswordpassword1234@@  !#Passw0rd!1234passwordpasswordpassword1234@@  !#");
    }

    @Test
    public void user_invalidPassword_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "hello123",
                "passw0rd1234",
                countryId,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("password must contain at least one upper case letter, one lower case letter, number and special character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
        assertThat(violation.getInvalidValue()).isEqualTo("passw0rd1234");
    }

    @Test
    public void user_nullCountry_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                null,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("country is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("countryId");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void user_nullAddress_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                countryId,
                null
//                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
        );

        // when

        // then
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("address is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("address");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void user_addressGreaterThan100_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                countryId,
                "this is an address this is an address this is an address this is an address this is an address this is an address this is an address "
//                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
        );

        // when

        // then
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("address must be less than 100 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("address");
        assertThat(violation.getInvalidValue()).isEqualTo("this is an address this is an address this is an address this is an address this is an address this is an address this is an address ");
    }

    @Test
    public void user_addressUnauthorizedSpecialCharacter_shouldNotValidate()
    {
        // given
        List<String> roles = new ArrayList<>();

        String countryId = "10";
        String roleId = "11";

        roles.add(roleId);

        AppUserRequest user = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                countryId,
                "# Cecilia[ ] @ Chapman ! 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401"
        );

        // when

        // then
        Set<ConstraintViolation<AppUserRequest>> violations = validator.validate(user);

        for (ConstraintViolation<AppUserRequest> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUserRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("address is invalid");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("address");
        assertThat(violation.getInvalidValue()).isEqualTo("# Cecilia[ ] @ Chapman ! 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401");
    }

}