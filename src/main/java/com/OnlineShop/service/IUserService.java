package com.OnlineShop.service;

import com.OnlineShop.dto.request.AppUserRequest;
import com.OnlineShop.entity.AppUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface IUserService
{

        List<AppUser> getUsers();

        AppUser getUserById(String userId);

        AppUser getUserByUsername(String username);

        AppUser createUser(AppUserRequest userRequest);

        AppUser updateUser(AppUserRequest userRequest);

        void deleteUserById(String userId);

}
