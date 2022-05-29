package com.OnlineShop.security.service;

import com.OnlineShop.dto.request.LoginRequest;
import com.OnlineShop.dto.request.RegisterRequest;
import com.OnlineShop.dto.request.UpdateRequest;
import com.OnlineShop.dto.response.UserInfoResponse;
import com.OnlineShop.entity.AppRole;
import com.OnlineShop.entity.AppUser;
import com.OnlineShop.entity.Country;
import com.OnlineShop.enums.RoleEnum;
import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.IUserRepository;
import com.OnlineShop.security.userdetails.UserDetailsImpl;
import com.OnlineShop.service.ICountryService;
import com.OnlineShop.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class AuthService implements IAuthService
{
    private final AuthenticationManager authenticationManager;

    private final IUserRepository userRepository;

    private final IRoleService roleService;

    private final ICountryService countryService;

    private final PasswordEncoder passwordEncoder;

    private final ITokenService tokenService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, IUserRepository userRepository, IRoleService roleService, ICountryService countryService, PasswordEncoder passwordEncoder, ITokenService tokenService)
    {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.countryService = countryService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }


    @Override
    public UserInfoResponse loginUser(LoginRequest loginRequest)
    {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String token = tokenService.createJWT(userDetails);

        List<String> roles = userDetails.getStringListRoles();

        Optional<AppUser> userOptional = userRepository.findById(userDetails.getId());

        if (userOptional.isEmpty())
            throw new NotFoundException("User with id: [" + userDetails.getId() + "] cannot be found");

        AppUser user = userOptional.get();

        return new UserInfoResponse(
               userDetails.getId(),
               user.getFirstName(),
                user.getLastName(),
               userDetails.getUsername(),
               userDetails.getEmail(),
                user.getPhoneNumber(),
               roles,
               token
        );

    }

    @Override
    public UserInfoResponse registerUser(RegisterRequest registerRequest)
    {
        registerRequest.setFirstName(registerRequest.getFirstName().trim().strip().toLowerCase(Locale.ROOT));
        registerRequest.setLastName(registerRequest.getLastName().trim().strip().toLowerCase(Locale.ROOT));
        registerRequest.setPhoneNumber(registerRequest.getPhoneNumber().trim().strip());
        registerRequest.setEmail(registerRequest.getEmail().trim().strip().toLowerCase(Locale.ROOT));
        registerRequest.setUsername(registerRequest.getUsername().trim().strip().toLowerCase(Locale.ROOT));
        registerRequest.setAddress(registerRequest.getAddress().trim().strip());

        boolean result = userRepository.existsByUsernameOrEmailOrPhoneNumber(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPhoneNumber());

        if (result)
            throw new AlreadyExistsException("User already exists.");


        Country country = countryService.getCountryById(registerRequest.getCountryId());

        Set<AppRole> roleSet = new HashSet<>();

        AppRole role = roleService.getRoleByName(RoleEnum.ROLE_USER.name());
        roleSet.add(role);

        AppUser user = new AppUser(
                null, // in order to create new entity
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getPhoneNumber(),
                registerRequest.getEmail(),
                roleSet, // user has only ROLE_USER
                registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword()), // encode the password
                country,
                registerRequest.getAddress(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        AppUser createdAppUser = userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest(createdAppUser.getUsername(), createdAppUser.getPassword());

        return loginUser(loginRequest);

    }

    @Override
    public UserInfoResponse updateUser(UpdateRequest updateRequest)
    {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            throw new UsernameNotFoundException("User is not logged in");


        // my implementation of UserDetailsImp does not work !!! but in loginUser method it does work
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String loggedInUsername = (String) authentication.getPrincipal();

        boolean result = userRepository.existsByEmailOrPhoneNumber(updateRequest.getEmail(), updateRequest.getPhoneNumber());

        if (result)
            throw new AlreadyExistsException("User with these descriptions already exists");


        Optional<AppUser> userToBeUpdatedOptional = userRepository.findByUsername(loggedInUsername);

        if (userToBeUpdatedOptional.isEmpty())
            throw new NotFoundException("User with username: [" + loggedInUsername + "] cannot be found");


        AppUser foundUser = userToBeUpdatedOptional.get();


        Set<AppRole> roleSet = foundUser.getRoles();

        List<String> stringListRoles = new ArrayList<>();

        for (AppRole role: roleSet)
            stringListRoles.add(role.getName());



        AppUser user = new AppUser(
                foundUser.getId(),
                updateRequest.getFirstName().trim().strip().toLowerCase(Locale.ROOT),
                updateRequest.getLastName().trim().strip().toLowerCase(Locale.ROOT),
                updateRequest.getPhoneNumber().trim().strip(),
                updateRequest.getEmail().trim().strip().toLowerCase(Locale.ROOT),
                roleSet, // user has only ROLE_USER
                foundUser.getUsername(), // username cannot be changed
                passwordEncoder.encode(updateRequest.getPassword()),
                countryService.getCountryById(updateRequest.getCountryId()),
                updateRequest.getAddress().trim().strip(),
                foundUser.getCreatedAt(), // createdAt Date cannot be change
                LocalDateTime.now() // update the updatedAt Date
        );

        AppUser updatedUser = userRepository.save(user);

        return new UserInfoResponse(
                updatedUser.getId(),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getPhoneNumber(),
                stringListRoles,
                null
        );

    }

    @Override
    public String logoutUser()
    {

        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
        {
            String loggedInUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(null);

            return "You have been logged out";
        }

        return "No user is logged into the application";
    }
}
