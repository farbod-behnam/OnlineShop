package com.OnlineShop.service;

import com.OnlineShop.dto.AppUserDto;
import com.OnlineShop.entity.AppUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface IUserService
{

        List<AppUser> getUsers();

        AppUser getUserById(String userId);

        AppUser getUserByUsername(String username);

        AppUser createUser(AppUserDto user);

        AppUser updateUser(AppUserDto user);

        void deleteUserById(String userId);

}
