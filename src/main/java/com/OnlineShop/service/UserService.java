package com.OnlineShop.service;

import com.OnlineShop.entity.AppUser;
import com.OnlineShop.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService
{
    @Override
    public List<AppUser> getUsers()
    {
        return null;
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
    public AppUser updateUser(Product product)
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