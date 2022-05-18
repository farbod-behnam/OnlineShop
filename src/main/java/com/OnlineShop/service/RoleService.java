package com.OnlineShop.service;

import com.OnlineShop.entity.AppRole;
import com.OnlineShop.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@Service
@Transactional
public class RoleService implements IRoleService
{
    private final IRoleRepository roleRepository;

    @Autowired
    public RoleService(IRoleRepository roleRepository)
    {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<AppRole> getRoles()
    {
        return roleRepository.findAll();
    }

    @Override
    public AppRole getRoleById(String userId)
    {
        return null;
    }

    @Override
    public AppRole getRoleByName(String name)
    {
        return null;
    }

    @Override
    public AppRole createRole(@Valid AppRole role)
    {
        return null;
    }

    @Override
    public AppRole updateRole(@Valid AppRole role)
    {
        return null;
    }
}
