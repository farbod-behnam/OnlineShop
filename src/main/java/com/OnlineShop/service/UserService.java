package com.OnlineShop.service;

import com.OnlineShop.dto.AppUserDto;
import com.OnlineShop.entity.AppRole;
import com.OnlineShop.entity.AppUser;
import com.OnlineShop.entity.Country;
import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserService(IUserRepository userRepository, IRoleService roleService, ICountryService countryService, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.countryService = countryService;
        this.passwordEncoder = passwordEncoder;
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
    public AppUser createUser(AppUserDto userDto)
    {

        userDto.setFirstName(userDto.getFirstName().trim().strip().toLowerCase(Locale.ROOT));
        userDto.setLastName(userDto.getLastName().trim().strip().toLowerCase(Locale.ROOT));
        userDto.setPhoneNumber(userDto.getPhoneNumber().trim().strip());
        userDto.setEmail(userDto.getEmail().trim().strip().toLowerCase(Locale.ROOT));
        userDto.setUsername(userDto.getUsername().trim().strip().toLowerCase(Locale.ROOT));
        userDto.setAddress(userDto.getAddress().trim().strip());

        Optional<AppUser> result = userRepository.findByUsernameOrEmailOrPhoneNumber(userDto.getUsername(), userDto.getEmail(), userDto.getPhoneNumber());

        if (result.isPresent())
            throw new AlreadyExistsException("User already exists.");


        Country country = countryService.getCountryById(userDto.getCountryId());

        List<String> roleIdSet = userDto.getRolesId();
        Set<AppRole> roleSet = roleService.getRoles(roleIdSet);

        AppUser user = new AppUser(
                null, // in order to create new entity
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getPhoneNumber(),
                userDto.getEmail(),
                roleSet,
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()), // encode the password
                country,
                userDto.getAddress(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );



        return userRepository.save(user);
    }

    @Override
    public AppUser updateUser(AppUserDto userDto)
    {

        Optional<AppUser> result = userRepository.findByUsernameOrEmailOrPhoneNumber(userDto.getUsername(), userDto.getEmail(), userDto.getPhoneNumber());

        // see if username, email or phone number already is in use
        if (result.isPresent())
            throw new AlreadyExistsException("User with these descriptions already exists");

        // if not then see if the user with this id already exists
        result = userRepository.findById(userDto.getId());

        if (result.isEmpty())
            throw new NotFoundException("User with id: [" + userDto.getId() + "] cannot be found");

        AppUser foundUser = result.get();

        Country country = countryService.getCountryById(userDto.getCountryId());

        List<String> roleIdSet = userDto.getRolesId();
        Set<AppRole> roleSet = roleService.getRoles(roleIdSet);

        AppUser user = new AppUser(
                foundUser.getId(),
                userDto.getFirstName().trim().strip().toLowerCase(Locale.ROOT),
                userDto.getLastName().trim().strip().toLowerCase(Locale.ROOT),
                userDto.getPhoneNumber(),
                userDto.getEmail().trim().strip().toLowerCase(Locale.ROOT),
                roleSet,
                foundUser.getUsername(), // username cannot be changed
                passwordEncoder.encode(userDto.getPassword()), // encode the password
                country,
                userDto.getAddress().trim().strip(),
                foundUser.getCreatedAt(), // createdAt Date cannot be change
                LocalDateTime.now() // update the updatedAt Date
        );



        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(String userId)
    {
        Optional<AppUser> result = userRepository.findById(userId);

        if (result.isEmpty())
            throw new NotFoundException("User with id: [" + userId + "] cannot be found");

        userRepository.deleteById(userId);
    }

}
