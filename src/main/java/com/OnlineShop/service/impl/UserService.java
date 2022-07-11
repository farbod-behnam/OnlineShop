package com.OnlineShop.service.impl;

import com.OnlineShop.dto.request.AppUserRequest;
import com.OnlineShop.entity.AppRole;
import com.OnlineShop.entity.AppUser;
import com.OnlineShop.entity.Country;
import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.rabbitmq.service.IPaymentService;
import com.OnlineShop.repository.IUserRepository;
import com.OnlineShop.service.ICountryService;
import com.OnlineShop.service.IRoleService;
import com.OnlineShop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
public class UserService implements IUserService
{

    private final IUserRepository userRepository;
    private final IRoleService roleService;
    private final ICountryService countryService;
    private final PasswordEncoder passwordEncoder;
    private final IPaymentService paymentService;

    @Autowired
    public UserService(IUserRepository userRepository, IRoleService roleService, ICountryService countryService, PasswordEncoder passwordEncoder, IPaymentService paymentService)
    {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.countryService = countryService;
        this.passwordEncoder = passwordEncoder;
        this.paymentService = paymentService;
    }

    @Override
    public List<AppUser> getUsers()
    {
        return userRepository.findAll();
    }

    @Override
    public AppUser getUserById(String userId)
    {
        Optional<AppUser> result = userRepository.findById(userId);

        AppUser user;

        if (result.isPresent())
            user = result.get();
        else
            throw new NotFoundException("User with id: [" + userId + "] cannot be found");

        return user;
    }

    @Override
    public AppUser getUserByUsername(String username)
    {
        username = username.trim().strip().toLowerCase(Locale.ROOT);

        Optional<AppUser> result = userRepository.findByUsername(username);

        AppUser user;

        if (result.isPresent())
            user = result.get();
        else
            throw new NotFoundException("User with username: [" + username + "] cannot be found");

        return user;
    }

    @Override
    public AppUser getLoggedInUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean userIsLoggedIn = authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();

        if (!userIsLoggedIn)
            throw new UsernameNotFoundException("User is not logged in");

        String loggedInUsername = (String) authentication.getPrincipal();

        return getUserByUsername(loggedInUsername);

    }

    @Override
    public AppUser createUser(AppUserRequest appUserRequest)
    {

        appUserRequest.setFirstName(appUserRequest.getFirstName().trim().strip().toLowerCase(Locale.ROOT));
        appUserRequest.setLastName(appUserRequest.getLastName().trim().strip().toLowerCase(Locale.ROOT));
        appUserRequest.setPhoneNumber(appUserRequest.getPhoneNumber().trim().strip());
        appUserRequest.setEmail(appUserRequest.getEmail().trim().strip().toLowerCase(Locale.ROOT));
        appUserRequest.setUsername(appUserRequest.getUsername().trim().strip().toLowerCase(Locale.ROOT));
        appUserRequest.setAddress(appUserRequest.getAddress().trim().strip());

        boolean result = userRepository.existsByUsernameOrEmailOrPhoneNumber(appUserRequest.getUsername(), appUserRequest.getEmail(), appUserRequest.getPhoneNumber());

        if (result)
            throw new AlreadyExistsException("User with username:[" + appUserRequest.getUsername() + "] or email:[" + appUserRequest.getUsername() + "] or phone number:[" + appUserRequest.getUsername() + "] already exists.");


        Country country = countryService.getCountryById(appUserRequest.getCountryId());

        List<String> roleIdSet = appUserRequest.getRoleIdList();
        Set<AppRole> roleSet = roleService.getRoles(roleIdSet);

        AppUser user = new AppUser(
                null, // in order to create new entity
                appUserRequest.getFirstName(),
                appUserRequest.getLastName(),
                appUserRequest.getPhoneNumber(),
                appUserRequest.getEmail(),
                roleSet,
                appUserRequest.getUsername(),
                passwordEncoder.encode(appUserRequest.getPassword()), // encode the password
                country,
                appUserRequest.getAddress(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        AppUser createdUser = userRepository.save(user);

        paymentService.saveUserRecord(createdUser);

        return createdUser;
    }

    @Override
    public AppUser updateUser(AppUserRequest appUserRequest)
    {

        boolean userExists = userRepository.existsByEmailOrPhoneNumber(appUserRequest.getEmail(), appUserRequest.getPhoneNumber());

        // see if username, email or phone number already is in use
        if (userExists)
            throw new AlreadyExistsException("User with email:[" + appUserRequest.getEmail() + "] or phone number:[" + appUserRequest.getPhoneNumber() + "] already exists.");

        // if not then see if the user with this id already exists
        Optional<AppUser> result = userRepository.findById(appUserRequest.getId());

        if (result.isEmpty())
            throw new NotFoundException("User with id: [" + appUserRequest.getId() + "] cannot be found");

        AppUser foundUser = result.get();

        Country country = countryService.getCountryById(appUserRequest.getCountryId());

        List<String> roleIdSet = appUserRequest.getRoleIdList();
        Set<AppRole> roleSet = roleService.getRoles(roleIdSet);

        AppUser user = new AppUser(
                foundUser.getId(),
                appUserRequest.getFirstName().trim().strip().toLowerCase(Locale.ROOT),
                appUserRequest.getLastName().trim().strip().toLowerCase(Locale.ROOT),
                appUserRequest.getPhoneNumber(),
                appUserRequest.getEmail().trim().strip().toLowerCase(Locale.ROOT),
                roleSet,
                foundUser.getUsername(), // username cannot be changed
                passwordEncoder.encode(appUserRequest.getPassword()), // encode the password
                country,
                appUserRequest.getAddress().trim().strip(),
                foundUser.getCreatedAt(), // createdAt Date cannot be change
                LocalDateTime.now() // update the updatedAt Date
        );

        AppUser updatedUser = userRepository.save(user);

        paymentService.saveUserRecord(updatedUser);

        return updatedUser;
    }

    @Override
    public void deleteUserById(String userId)
    {
        Optional<AppUser> result = userRepository.findById(userId);

        if (result.isEmpty())
            throw new NotFoundException("User with id: [" + userId + "] cannot be found");

        AppUser loggedInUser = getLoggedInUser();

        if (loggedInUser.getId().equals(result.get().getId()))
            throw new UnsupportedOperationException("You cannot delete your own user account");


        userRepository.deleteById(userId);
    }

}
