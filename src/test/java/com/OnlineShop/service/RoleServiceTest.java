package com.OnlineShop.service;

import com.OnlineShop.repository.IRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest
{

    private IRoleService underTestRoleService;

    @Mock
    private IRoleRepository roleRepository;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        underTestRoleService = new RoleService(roleRepository);
    }

    @Test
    void getRoles_shouldReturnRoleList()
    {
        // given

        // when
        underTestRoleService.getRoles();

        // then
        verify(roleRepository).findAll();
    }

    @Test
    void getRoleById()
    {
    }

    @Test
    void getRoleByName()
    {
    }

    @Test
    void createRole()
    {
    }

    @Test
    void updateRole()
    {
    }
}