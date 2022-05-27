package com.OnlineShop.service;

import com.OnlineShop.entity.AppRole;

import java.util.List;
import java.util.Set;

public interface IRoleService
{
    List<AppRole> getRoles();

    Set<AppRole> getRoles(List<String> roleIdList);

    AppRole getRoleById(String roleId);

    AppRole getRoleByName(String name);

    AppRole createRole(AppRole role);

    AppRole updateRole(AppRole role);

}
