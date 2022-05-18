package com.OnlineShop.controller;

import com.OnlineShop.entity.AppRole;
import com.OnlineShop.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController
{
    private final IRoleService roleService;

    @Autowired
    public RoleController(IRoleService roleService)
    {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<AppRole>> getRoles()
    {
        List<AppRole> roles = roleService.getRoles();

        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<AppRole> getRole(@PathVariable String roleId)
    {
        AppRole role = roleService.getRoleById(roleId);

        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}
