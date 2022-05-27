package com.OnlineShop.service;

import com.OnlineShop.entity.AppRole;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.IRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

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
    void getRoles_shouldReturnARoleSet()
    {
        // given
        List<String> roleIdList = new ArrayList<>();
        roleIdList.add("11");


        AppRole role = new AppRole("19", RoleEnum.ROLE_USER.name());
//
        given(roleRepository.findById(anyString())).willReturn(Optional.of(role));

        // when
        Set<AppRole> roleSet = underTestRoleService.getRoles(roleIdList);

        // then
        verify(roleRepository).findById(anyString());
        assertThat(roleSet.contains(role)).isEqualTo(true);
    }

    @Test
    void getRoles_nullList_shouldThrowIllegalArgumentException()
    {
        // given

        // when

        // then
        assertThatThrownBy(() -> underTestRoleService.getRoles(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Role cannot be empty");
    }

    @Test
    void getRoles_emptyList_shouldThrowIllegalArgumentException()
    {
        // given
        List<String> roleIdList = new ArrayList<>();

        // when

        // then
        assertThatThrownBy(() -> underTestRoleService.getRoles(roleIdList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Role cannot be empty");
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
    void getRoleByName_shouldReturnARole()
    {
        // given
        AppRole role = new AppRole("19", RoleEnum.ROLE_USER.name());

        given(roleRepository.findByName(anyString())).willReturn(Optional.of(role));

        // when
        AppRole foundRole = underTestRoleService.getRoleByName(anyString());

        // then
        verify(roleRepository).findByName(anyString());
        assertThat(foundRole).isEqualTo(role);
    }

    @Test
    void getRoleByName_shouldThrowNotFoundException()
    {
        String name = "name";

        // when

        // then
        assertThatThrownBy(() -> underTestRoleService.getRoleByName(name))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Role with name: [" + name + "] cannot be found");
    }

    @Test
    void createRole_shouldReturnCreatedRole()
    {
        // given
        AppRole role = new AppRole("19", RoleEnum.ROLE_USER.name());

        // when
        underTestRoleService.createRole(role);

        // then
        ArgumentCaptor<AppRole> roleArgumentCaptor = ArgumentCaptor.forClass(AppRole.class);
        verify(roleRepository).findByName(role.getName());
        verify(roleRepository).save(roleArgumentCaptor.capture());
        AppRole capturedRole = roleArgumentCaptor.getValue();
        assertThat(capturedRole).isEqualTo(role);

    }

    @Test
    void createRole_shouldThrowAlreadyExistsException()
    {
        // given
        AppRole role = new AppRole("19", RoleEnum.ROLE_USER.name());

        given(roleRepository.findByName(anyString())).willReturn(Optional.of(role));

        AppRole roleToBeCreated = new AppRole("19", RoleEnum.ROLE_USER.name());

        // when

        // then
        assertThatThrownBy(() -> underTestRoleService.createRole(roleToBeCreated))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Role name already exists.");

    }

    @Test
    void updateRole_shouldReturnUpdatedRole()
    {
        // given
        AppRole roleToBeUpdated = new AppRole("19", RoleEnum.ROLE_USER.name());

//        given(roleRepository.findByName(anyString())).willReturn(Optional.of(roleToBeUpdated));
        given(roleRepository.findById(anyString())).willReturn(Optional.of(roleToBeUpdated));

        // when
        underTestRoleService.updateRole(roleToBeUpdated);

        // then
        ArgumentCaptor<AppRole> roleArgumentCaptor = ArgumentCaptor.forClass(AppRole.class);
        verify(roleRepository).findByName(roleToBeUpdated.getName());
        verify(roleRepository).save(roleArgumentCaptor.capture());
        AppRole capturedRole = roleArgumentCaptor.getValue();
        assertThat(capturedRole).isEqualTo(roleToBeUpdated);
    }

    @Test
    void updateRole_shouldThrowAlreadyExistsException()
    {
        // given
        AppRole roleToBeUpdated = new AppRole("19", RoleEnum.ROLE_USER.name());

        given(roleRepository.findByName(anyString())).willReturn(Optional.of(roleToBeUpdated));
//        given(roleRepository.findById(anyString())).willReturn(Optional.of(roleToBeUpdated));

        // when

        // then
        assertThatThrownBy(() -> underTestRoleService.updateRole(roleToBeUpdated))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Role with name: [" + roleToBeUpdated.getName() + "] already exists.");
    }

    @Test
    void updateRole_shouldThrowNotFoundException()
    {
        // given
        AppRole roleToBeUpdated = new AppRole("19", RoleEnum.ROLE_USER.name());

        given(roleRepository.findByName(anyString())).willReturn(Optional.empty());
        given(roleRepository.findById(anyString())).willReturn(Optional.empty());

        // when

        // then
        assertThatThrownBy(() -> underTestRoleService.updateRole(roleToBeUpdated))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Role with id: [" + roleToBeUpdated.getId() + "] cannot be found");
    }
}