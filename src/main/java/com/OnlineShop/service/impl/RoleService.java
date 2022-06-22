package com.OnlineShop.service.impl;

import com.OnlineShop.entity.AppRole;
import com.OnlineShop.exception.AlreadyExistsException;
import com.OnlineShop.exception.NotFoundException;
import com.OnlineShop.repository.IRoleRepository;
import com.OnlineShop.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public Set<AppRole> getRoles(List<String> roleIdList)
    {
        if ( roleIdList == null || roleIdList.isEmpty())
            throw new IllegalArgumentException("Role cannot be empty");


        Set<AppRole> roleSet = new HashSet<>();

        for (String roleIdStr: roleIdList)
        {
            AppRole role = getRoleById(roleIdStr);
            roleSet.add(role);
        }

        return roleSet;

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
        // in order to create new entity
        role.setId(null);

        Optional<AppRole> result = roleRepository.findByName(role.getName());

        if (result.isPresent())
            throw new AlreadyExistsException("Role name already exists.");


        return roleRepository.save(role);
    }



    @Override
    public AppRole updateRole(AppRole role)
    {
        Optional<AppRole> result = roleRepository.findByName(role.getName());

        if (result.isPresent())
            throw new AlreadyExistsException("Role with name: [" + role.getName() + "] already exists.");

        result = roleRepository.findById(role.getId());

        if (result.isEmpty())
            throw new NotFoundException("Role with id: [" + role.getId() + "] cannot be found");

        return roleRepository.save(role);
    }
}
