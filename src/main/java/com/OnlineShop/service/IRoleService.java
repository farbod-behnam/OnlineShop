package com.OnlineShop.service;

import com.OnlineShop.entity.AppRole;

import java.util.List;

public interface IRoleService
{
    List<AppRole> getRoles();

    AppRole getRoleById(String userId);

    AppRole getRoleByName(String name);

    AppRole createUser(AppRole role);

    AppRole updateUser(AppRole role);

}
