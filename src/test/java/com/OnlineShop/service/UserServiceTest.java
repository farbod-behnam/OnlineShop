package com.OnlineShop.service;

import com.OnlineShop.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
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
    void getUserById()
    {
    }

    @Test
    void createUser()
    {
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