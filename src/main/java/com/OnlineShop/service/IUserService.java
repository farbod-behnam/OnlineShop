package com.OnlineShop.service;

import com.OnlineShop.entity.AppUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface IUserService
{

        List<AppUser> getUsers();

        AppUser getUserById(String userId);

        AppUser createUser(AppUser user);

        AppUser updateUser(AppUser user);

        void deleteUserById(String userId);

}
