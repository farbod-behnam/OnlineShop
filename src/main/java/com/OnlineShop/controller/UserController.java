package com.OnlineShop.controller;

import com.OnlineShop.dto.request.AppUserRequest;
import com.OnlineShop.entity.AppUser;
import com.OnlineShop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController
{
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService)
    {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<AppUser>> getUsers()
    {
        List<AppUser> users = userService.getUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AppUser> getUser(@PathVariable String userId)
    {
        AppUser user = userService.getUserById(userId);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AppUser> postUser(@Valid @RequestBody AppUserRequest user)
    {
        AppUser createdUser = userService.createUser(user);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<AppUser> putUser(@Valid @RequestBody AppUserRequest user)
    {
        AppUser updatedUser = userService.updateUser(user);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId)
    {
        userService.deleteUserById(userId);

        return new ResponseEntity<>("User with id: [" + userId + "] is deleted", HttpStatus.OK);
    }


}
