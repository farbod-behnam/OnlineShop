package com.OnlineShop.service;

import com.OnlineShop.entity.AppRole;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.IRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
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
    void getRoleById_shouldReturnARole()
    {
        // given
        AppRole role = new AppRole("19", RoleEnum.ROLE_USER.name());

        given(roleRepository.findById(anyString())).willReturn(Optional.of(role));

        // when
        AppRole foundRole = underTestRoleService.getRoleById(anyString());

        // then
        verify(roleRepository).findById(anyString());
        assertThat(foundRole).isEqualTo(role);
    }

    @Test
    void getRoleById_shouldThrowNotFoundException()
    {
        // given
        String roleId = "19";

        // when

        // then
        assertThatThrownBy(() -> underTestRoleService.getRoleById(roleId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Role with id: [" + roleId + "] cannot be found");
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