package com.OnlineShop.entity;

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

class AppUserTest
{
    private Validator validator;

    @BeforeEach
    void setUp()
    {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }








    @Test
    public void user_phoneNumberContainsAlphabetOrSpecialCharacters_shouldNotValidate()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "00166 fasd!@$",
                "johnwick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                country,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

        for (ConstraintViolation<AppUser> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUser> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("phone number must only contain number");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("phoneNumber");
        assertThat(violation.getInvalidValue()).isEqualTo("00166 fasd!@$");
    }


    @Test
    public void user_phoneNumberHasAWrongCountryCode_shouldNotValidate()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "00461111111111111",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                country,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

        for (ConstraintViolation<AppUser> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUser> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("phone number is invalid");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("phoneNumber");
        assertThat(violation.getInvalidValue()).isEqualTo("00461111111111111");
    }

    @Test
    public void user_phoneNumberHasWrongLengthForCountryCode_shouldNotValidate()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "0049999999",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                country,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

        for (ConstraintViolation<AppUser> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUser> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("phone number is invalid");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("phoneNumber");
        assertThat(violation.getInvalidValue()).isEqualTo("0049999999");
    }

    @Test
    public void user_nullEmail_shouldNotValidate()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "0016666666666",
                null,
                roles,
                "johnwick", // j.wick
                "Password1234!",
                country,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

        for (ConstraintViolation<AppUser> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUser> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("email is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void user_invalidEmail_shouldNotValidate()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "hello!@#$@124",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                country,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

        for (ConstraintViolation<AppUser> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUser> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("email is invalid");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
        assertThat(violation.getInvalidValue()).isEqualTo("hello!@#$@124");
    }

    @Test
    public void user_nullUsername_shouldNotValidate()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                null,
                "Password1234!",
                country,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

        for (ConstraintViolation<AppUser> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUser> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("username is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("username");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void user_usernameLessThan3_shouldNotValidate()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "ja",
                "Password1234!",
                country,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

        for (ConstraintViolation<AppUser> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUser> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("username must be between 3 and 24 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("username");
        assertThat(violation.getInvalidValue()).isEqualTo("ja");
    }

    @Test
    public void user_usernameGreaterThan24_shouldNotValidate()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "fasdfasdfasdfasdfasdfasdf",
                "Password1234!",
                country,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

        for (ConstraintViolation<AppUser> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUser> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("username must be between 3 and 24 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("username");
        assertThat(violation.getInvalidValue()).isEqualTo("fasdfasdfasdfasdfasdfasdf");
    }

    @Test
    public void user_invalidUsername_shouldNotValidate()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "hello1234fasdfjlkjsdf", // hello1234fasdfjlkjsdf - how.1234
                "Password1234!",
                country,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

        for (ConstraintViolation<AppUser> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUser> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("username is invalid");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("username");
        assertThat(violation.getInvalidValue()).isEqualTo("hello1234fasdfjlkjsdf");
    }

    @Test
    public void user_passwordLessThan10_shouldNotValidate()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "hello123",
                "Passw0rd!",
                country,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

        for (ConstraintViolation<AppUser> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUser> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("password must be greater than 10 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
        assertThat(violation.getInvalidValue()).isEqualTo("Passw0rd!");
    }

    @Test
    public void user_passwordGreaterThan64_shouldNotValidate()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "hello123",
                "Passw0rd!1234passwordpasswordpassword1234@@  !#Passw0rd!1234passwordpasswordpassword1234@@  !#",
                country,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

        for (ConstraintViolation<AppUser> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUser> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("password must be greater than 10 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
        assertThat(violation.getInvalidValue()).isEqualTo("Passw0rd!1234passwordpasswordpassword1234@@  !#Passw0rd!1234passwordpasswordpassword1234@@  !#");
    }

    @Test
    public void user_invalidPassword_shouldNotValidate()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "hello123",
                "passw0rd1234",
                country,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

        for (ConstraintViolation<AppUser> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUser> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("password must contain at least one upper case letter, one lower case letter, number and special character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
        assertThat(violation.getInvalidValue()).isEqualTo("passw0rd1234");
    }

    @Test
    public void user_nullCountry_shouldNotValidate()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                null,
                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

        for (ConstraintViolation<AppUser> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUser> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("country is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("country");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void user_nullAddress_shouldNotValidate()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                country,
                null,
//                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

        for (ConstraintViolation<AppUser> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUser> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("address is required");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("address");
        assertThat(violation.getInvalidValue()).isEqualTo(null);
    }

    @Test
    public void user_addressGreaterThan100_shouldNotValidate()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                country,
                "this is an address this is an address this is an address this is an address this is an address this is an address this is an address ",
//                "Cecilia Chapman 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

        for (ConstraintViolation<AppUser> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUser> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("address must be less than 100 character");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("address");
        assertThat(violation.getInvalidValue()).isEqualTo("this is an address this is an address this is an address this is an address this is an address this is an address this is an address ");
    }

    @Test
    public void user_addressUnauthorizedSpecialCharacter_shouldNotValidate()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser user = new AppUser(
                "19",
                "John",
                "Wick",
                "0016666666666",
                "john.wick@gmail.com",
                roles,
                "johnwick", // j.wick
                "Password1234!",
                country,
                "# Cecilia[ ] @ Chapman ! 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        Set<ConstraintViolation<AppUser>> violations = validator.validate(user);

        for (ConstraintViolation<AppUser> violation : violations)
        {
            System.out.println("Violation Message: " + violation.getMessage());
        }

        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);

        ConstraintViolation<AppUser> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("address is invalid");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("address");
        assertThat(violation.getInvalidValue()).isEqualTo("# Cecilia[ ] @ Chapman ! 711-2880 Nulla St. Mankato Mississippi 96522 (257) 563-7401");
    }
}