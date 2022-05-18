package com.OnlineShop.service;

import com.OnlineShop.entity.AppRole;
import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
    public AppRole getRoleById(String roleId)
    {
        Optional<AppRole> result = roleRepository.findById(roleId);

        AppRole role;

        if (result.isPresent())
            role = result.get();
        else
            throw new NotFoundException("Role with id: [" + roleId + "] cannot be found");

        return role;
    }

    @Override
    public AppRole getRoleByName(String name)
    {
        Optional<AppRole> result = roleRepository.findByName(name);

        AppRole role;

        if (result.isPresent())
            role = result.get();
        else
            throw new NotFoundException("Role with name: [" + name + "] cannot be found");

        return role;
    }

    @Override
    public AppRole createRole(AppRole role)
    {
        return null;
    }

    @Override
    public AppRole updateRole(@Valid AppRole role)
    {
        return null;
    }
}
