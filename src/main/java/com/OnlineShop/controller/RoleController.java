package com.OnlineShop.controller;

import com.OnlineShop.entity.AppRole;
import com.OnlineShop.entity.AppUser;
import com.OnlineShop.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping
    public ResponseEntity<AppRole> postRole(@RequestBody AppRole role)
    {
        AppRole createdRole = roleService.createRole(role);

        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<AppRole> putRole(@RequestBody AppRole role)
    {
        AppRole updateRole = roleService.updateRole(role);

        return new ResponseEntity<>(updateRole, HttpStatus.CREATED);
    }

}
