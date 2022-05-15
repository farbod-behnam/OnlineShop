package com.OnlineShop.service;

import com.OnlineShop.entity.AppUser;
import com.OnlineShop.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return null;
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
