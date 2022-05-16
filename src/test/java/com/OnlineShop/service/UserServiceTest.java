package com.OnlineShop.service;

import com.OnlineShop.entity.AppRole;
import com.OnlineShop.entity.AppUser;
import com.OnlineShop.entity.Country;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest
{

    private IUserService underTestUserService;

    @Mock
    private IUserRepository userRepository;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        underTestUserService = new UserService(userRepository);
    }

    @Test
    void getUsers_shouldReturnUserList()
    {
        // given

        // when
        underTestUserService.getUsers();

        // then
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_shouldReturnAnAppUser()
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
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "password1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(userRepository.findById(anyString())).willReturn(Optional.of(user));

        // when
        AppUser foundUser = underTestUserService.getUserById(anyString());

        // then
        verify(userRepository).findById(anyString());
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void getUserById_shouldThrowNotFoundException()
    {
        // given
        String userId = "11";

        // when

        // then
        assertThatThrownBy(() -> underTestUserService.getUserById(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with id: [" + userId + "] cannot be found");
    }

    @Test
    void createUser_shouldReturnCreatedUser()
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
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "password1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when
        underTestUserService.createUser(user);

        // then
        ArgumentCaptor<AppUser> userArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(userRepository).findByUsernameOrEmailOrPhoneNumber(user.getUsername(), user.getEmail(), user.getPhoneNumber());
        verify(userRepository).save(userArgumentCaptor.capture());
        AppUser capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    void createUser_shouldThrowAlreadyExistsException()
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
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "password1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(userRepository.findByUsernameOrEmailOrPhoneNumber(anyString(), anyString(), anyString())).willReturn(Optional.of(user));

        AppUser userToBeCreated = new AppUser(
                null,
                "User",
                "ToBeCreated",
                "001666666666",
                "john.wick@gmail.com",
                roles,
                "john.wick",
                "password1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when

        // then
        assertThatThrownBy(() -> underTestUserService.createUser(userToBeCreated))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("User already exists.");

    }

    @Test
    void updateUser()
    {
    }

    @Test
    void deleteUserById()
    {
    }

    @Test
    void usernameExists()
    {
    }
}