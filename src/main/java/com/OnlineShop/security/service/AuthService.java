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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    public AuthService(AuthenticationManager authenticationManager, IUserRepository userRepository, IRoleService roleService, ICountryService countryService, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, ITokenService tokenService)
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
        // authenticationManager has been initialized inside com.OnlineShop.security.SecurityConfig class
        // by AuthenticationManagerBuilder. It uses an implementation of UserDetailsService for finding the username
        // and also decode the password based on the PasswordEncoder initialized inside com.OnlineShop.security.SecurityConfig class
        //
        // we also need to implement UserDetails interface since UserDetailsServiceImpl returns a UserDetails

        Authentication authentication;

        try
        {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        }
        catch (BadCredentialsException exception)
        {
           throw new BadCredentialsException("Incorrect username or password");
        }


        SecurityContextHolder.getContext().setAuthentication(authentication);

        // get the authenticated user
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

//        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(loginRequest.getUsername());

        // create a JWT based on UserDetails implemented class
        String token = tokenService.createJWT(userDetails);

        List<String> roles = userDetails.getStringListRoles();


        return new UserInfoResponse(
               userDetails.getId(),
               userDetails.getFirstName(),
               userDetails.getLastName(),
               userDetails.getUsername(),
               userDetails.getEmail(),
               userDetails.getPhoneNumber(),
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

        // first check if the username, email or phone number already exists in the database
        boolean result = userRepository.existsByUsernameOrEmailOrPhoneNumber(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPhoneNumber());

        if (result)
            throw new AlreadyExistsException("User already exists.");

        Country country = countryService.getCountryById(registerRequest.getCountryId());

        Set<AppRole> roleSet = new HashSet<>();

        // a normal user only needs to have ROLE_USER, so we need to find the
        // ROLE_USER based on its name
        AppRole role = roleService.getRoleByName(RoleEnum.ROLE_USER.name());
        roleSet.add(role);

        // Create AppUser entity
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

        // Now we need to build the authenticated user
        UserDetailsImpl userDetails = UserDetailsImpl.buildUserDetails(createdAppUser);


        // Now create JWT based on authenticated user
        String token = tokenService.createJWT(userDetails);

        List<String> roles = userDetails.getStringListRoles();


        return new UserInfoResponse(
                userDetails.getId(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getPhoneNumber(),
                roles,
                token
        );

    }

    @Override
    public UserInfoResponse updateUser(UpdateRequest updateRequest)
    {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            throw new UsernameNotFoundException("User is not logged in");

//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String loggedInUsername = (String) authentication.getPrincipal();

        // check if the email or phone number already exists in the database
        boolean result = userRepository.existsByEmailOrPhoneNumber(updateRequest.getEmail(), updateRequest.getPhoneNumber());

        if (result)
            throw new AlreadyExistsException("User with these descriptions already exists");


        Optional<AppUser> userToBeUpdatedOptional = userRepository.findByUsername(loggedInUsername);

        if (userToBeUpdatedOptional.isEmpty())
            throw new NotFoundException("User with username: [" + loggedInUsername + "] cannot be found");


        AppUser foundUser = userToBeUpdatedOptional.get();


        Set<AppRole> roleSet = foundUser.getRoles();

        AppUser user = new AppUser(
                foundUser.getId(),
                updateRequest.getFirstName().trim().strip().toLowerCase(Locale.ROOT),
                updateRequest.getLastName().trim().strip().toLowerCase(Locale.ROOT),
                updateRequest.getPhoneNumber().trim().strip(),
                updateRequest.getEmail().trim().strip().toLowerCase(Locale.ROOT),
                roleSet,
                foundUser.getUsername(), // username cannot be changed
                passwordEncoder.encode(updateRequest.getPassword()),
                countryService.getCountryById(updateRequest.getCountryId()),
                updateRequest.getAddress().trim().strip(),
                foundUser.getCreatedAt(), // createdAt Date cannot be change
                LocalDateTime.now() // update the updatedAt Date
        );

        AppUser updatedUser = userRepository.save(user);

        // create a string list to add string role name to it
        List<String> stringListRoles = new ArrayList<>();

        for (AppRole role: roleSet)
            stringListRoles.add(role.getName());

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
}
