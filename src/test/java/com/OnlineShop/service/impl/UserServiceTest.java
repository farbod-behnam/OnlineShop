package com.OnlineShop.service.impl;

import com.OnlineShop.dto.request.AppUserRequest;
import com.OnlineShop.entity.AppRole;
import com.OnlineShop.entity.AppUser;
import com.OnlineShop.entity.Country;
import com.OnlineShop.enums.CountryEnum;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.rabbitmq.service.IPaymentService;
import com.OnlineShop.repository.IUserRepository;
import com.OnlineShop.service.ICountryService;
import com.OnlineShop.service.IRoleService;
import com.OnlineShop.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Mock
    private IPaymentService paymentService;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        underTestUserService = new UserService(userRepository, roleService, countryService, passwordEncoder, paymentService);
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
    void getLoggedInUser_shouldReturnLoggedInUser()
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

        // user need to be authenticated for this test
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(RoleEnum.ROLE_USER.name()));

        Authentication authentication = new UsernamePasswordAuthenticationToken("john.wick", null,authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));

        // when
        AppUser foundLoggedInUser = underTestUserService.getLoggedInUser();

        // then
        verify(userRepository).findByUsername(anyString());
        assertThat(foundLoggedInUser).isEqualTo(user);
    }

    @Test
    void getLoggedInUser_nullAuthentication_shouldThrowUsernameNotFoundException()
    {
        // given
        SecurityContextHolder.getContext().setAuthentication(null);

        // when

        // then
        assertThatThrownBy(() -> underTestUserService.getLoggedInUser())
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User is not logged in");
    }

    @Test
    void getLoggedInUser_anonymousAuthentication_shouldThrowUsernameNotFoundException()
    {
        // given
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(RoleEnum.ROLE_USER.name()));

        Authentication authentication = new AnonymousAuthenticationToken("123", "anonymousUser", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when

        // then
        assertThatThrownBy(() -> underTestUserService.getLoggedInUser())
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User is not logged in");
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

        given(passwordEncoder.encode(anyString())).willReturn(user.getPassword());


        // when
        underTestUserService.createUser(userDto);

        // then
        ArgumentCaptor<AppUser> userArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(userRepository).existsByUsernameOrEmailOrPhoneNumber(user.getUsername(), user.getEmail(), user.getPhoneNumber());
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


        given(userRepository.existsByUsernameOrEmailOrPhoneNumber(anyString(), anyString(), anyString())).willReturn(true);

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
                .hasMessageContaining("User with username:[" + userToBeCreated.getUsername() + "] or email:[" + userToBeCreated.getUsername() + "] or phone number:[" + userToBeCreated.getUsername() + "] already exists.");

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

        given(passwordEncoder.encode(anyString())).willReturn(user.getPassword());
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

        given(userRepository.existsByEmailOrPhoneNumber(anyString(), anyString())).willReturn(true);

        // when

        // then
        assertThatThrownBy(() -> underTestUserService.updateUser(userToBeUpdated))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("User with email:[" + userToBeUpdated.getEmail() + "] or phone number:[" + userToBeUpdated.getPhoneNumber() + "] already exists.");

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

        AppUser loggedInUser = new AppUser(
                "2020",
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

        // user need to be authenticated for this test
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(RoleEnum.ROLE_USER.name()));

        Authentication authentication = new UsernamePasswordAuthenticationToken("john.wick", null,authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(loggedInUser));

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

    @Test
    void deleteUserById_deletingYourOwnAccount_shouldThrowUnsupportedOperationException()
    {
        // given
        String userId = "19";

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


        // user need to be authenticated for this test
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(RoleEnum.ROLE_USER.name()));

        Authentication authentication = new UsernamePasswordAuthenticationToken("john.wick", null,authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(userToBeDeleted));



        // when

        // then
        assertThatThrownBy(() -> underTestUserService.deleteUserById(userId))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("You cannot delete your own user account");
    }
}