package com.OnlineShop.service;

import com.OnlineShop.entity.AppUser;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements IUserService
{

    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository)
    {
        this.userRepository = userRepository;
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
    public AppUser createUser(AppUser user)
    {
        return null;
    }

    @Override
    public AppUser updateUser(AppUser user)
    {
        return null;
    }

    @Override
    public void deleteUserById(String productId)
    {

    }

    @Override
    public boolean usernameExists(String username)
    {
        return false;
    }
}
