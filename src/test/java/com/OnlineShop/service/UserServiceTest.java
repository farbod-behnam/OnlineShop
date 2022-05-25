package com.OnlineShop.service;

import com.OnlineShop.dto.request.AppUserRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest
{

    private IUserService underTestUserService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IRoleService roleService;

    @Mock
    ICountryService countryService;

    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        underTestUserService = new UserService(userRepository, roleService, countryService, passwordEncoder);
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
    void getUserByUsername_shouldReturnAppUser()
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
                "johnwick",
                "password1234",
                country,
                "This is an address",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));

        // when
        AppUser foundUser = underTestUserService.getUserByUsername(anyString());

        // then
        verify(userRepository).findByUsername(anyString());
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void getUserByUsername_shouldThrowNotFoundException()
    {
        // given
        String username = "johnwick";

        // when

        // then
        assertThatThrownBy(() -> underTestUserService.getUserByUsername(username))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with username: [" + username + "] cannot be found");
    }

    @Test
    void createUser_shouldReturnCreatedUser()
    {
        // given

        // user dto
        List<String> roleIdList = new ArrayList<>();
        String roleId = "11";
        roleIdList.add(roleId);

        String countryId = "11";

        AppUserRequest userDto = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roleIdList,
                "john.wick",
                "password1234",
                countryId,
                "This is an address"
        );

        // user

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

        given(countryService.getCountryById(anyString())).willReturn(country);
        given(roleService.getRoles(roleIdList)).willReturn(roles);



        // when
        underTestUserService.createUser(userDto);

        // then
        ArgumentCaptor<AppUser> userArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(userRepository).findByUsernameOrEmailOrPhoneNumber(user.getUsername(), user.getEmail(), user.getPhoneNumber());
        verify(userRepository).save(userArgumentCaptor.capture());
        AppUser capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser.getFirstName()).isEqualTo(user.getFirstName().toLowerCase(Locale.ROOT));
        assertThat(capturedUser.getLastName()).isEqualTo(user.getLastName().toLowerCase(Locale.ROOT));
        assertThat(capturedUser.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(capturedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(capturedUser.getRoles()).isEqualTo(user.getRoles());
        assertThat(capturedUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(capturedUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(capturedUser.getCountry()).isEqualTo(user.getCountry());
        assertThat(capturedUser.getAddress()).isEqualTo(user.getAddress());
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

        // user dto
        List<String> roleIdList = new ArrayList<>();
        String roleId = "11";
        roleIdList.add(roleId);

        String countryId = "11";

        AppUserRequest userToBeCreated = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roleIdList,
                "john.wick",
                "password1234",
                countryId,
                "This is an address"
        );

        // when

        // then
        assertThatThrownBy(() -> underTestUserService.createUser(userToBeCreated))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("User already exists.");

    }

    @Test
    void updateUser_shouldReturnUpdatedUser()
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

        // user dto
        List<String> roleIdList = new ArrayList<>();
        String roleId = "11";
        roleIdList.add(roleId);

        String countryId = "11";

        AppUserRequest userToBeUpdated = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roleIdList,
                "john.wick",
                "password1234",
                countryId,
                "This is an address"
        );

        given(userRepository.findById(anyString())).willReturn(Optional.of(user));

        given(countryService.getCountryById(anyString())).willReturn(country);
        given(roleService.getRoles(roleIdList)).willReturn(roles);

        // when
        underTestUserService.updateUser(userToBeUpdated);

        // then
        ArgumentCaptor<AppUser> userArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(userRepository).findById(userToBeUpdated.getId());
        verify(userRepository).save(userArgumentCaptor.capture());
        AppUser capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser.getFirstName()).isEqualTo(user.getFirstName().toLowerCase(Locale.ROOT));
        assertThat(capturedUser.getLastName()).isEqualTo(user.getLastName().toLowerCase(Locale.ROOT));
        assertThat(capturedUser.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(capturedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(capturedUser.getRoles()).isEqualTo(user.getRoles());
        assertThat(capturedUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(capturedUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(capturedUser.getCountry()).isEqualTo(user.getCountry());
        assertThat(capturedUser.getAddress()).isEqualTo(user.getAddress());
    }

    @Test
    void updateUser_shouldThrowNotFoundException()
    {
        // given
        List<String> roleIdList = new ArrayList<>();
        String roleId = "11";
        roleIdList.add(roleId);

        String countryId = "11";

        AppUserRequest notFoundUser = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roleIdList,
                "john.wick",
                "password1234",
                countryId,
                "This is an address"
        );

        given(userRepository.findById(anyString())).willReturn(Optional.empty());

        // when

        // then
        assertThatThrownBy(() -> underTestUserService.updateUser(notFoundUser))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with id: [" + notFoundUser.getId() + "] cannot be found");

    }

    @Test
    void updateUser_shouldAlreadyExistsException()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser foundUser = new AppUser(
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

        List<String> roleIdList = new ArrayList<>();
        String roleId = "11";
        roleIdList.add(roleId);

        String countryId = "11";

        AppUserRequest userToBeUpdated = new AppUserRequest(
                "19",
                "John",
                "Wick",
                "001666666666",
                "john.wick@gmail.com",
                roleIdList,
                "john.wick",
                "password1234",
                countryId,
                "This is an address"
        );

        given(userRepository.findByUsernameOrEmailOrPhoneNumber(anyString(), anyString(), anyString())).willReturn(Optional.of(foundUser));

        // when

        // then
        assertThatThrownBy(() -> underTestUserService.updateUser(userToBeUpdated))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("User with these descriptions already exists");

    }

    @Test
    void deleteUserById_shouldDeleteAUser()
    {
        // given
        Set<AppRole> roles = new HashSet<>();

        Country country = new Country("10", CountryEnum.Germany.name());
        AppRole role = new AppRole("11", RoleEnum.ROLE_USER.name());

        roles.add(role);

        AppUser userToBeDeleted = new AppUser(
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

        given(userRepository.findById(anyString())).willReturn(Optional.of(userToBeDeleted));

        // when
        underTestUserService.deleteUserById(userToBeDeleted.getId());

        // then
        verify(userRepository).deleteById(userToBeDeleted.getId());
    }

    @Test
    void deleteUserById_shouldThrowNotFoundException()
    {
        // given
        String userId = "11";

        given(userRepository.findById(anyString())).willReturn(Optional.empty());

        // when

        // then
        assertThatThrownBy(() -> underTestUserService.deleteUserById(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with id: [" + userId + "] cannot be found");
    }


}