package com.OnlineShop.controller;

import com.OnlineShop.dto.request.LoginRequest;
import com.OnlineShop.dto.request.RegisterRequest;
import com.OnlineShop.dto.request.UpdateRequest;
import com.OnlineShop.dto.response.UserInfoResponse;
import com.OnlineShop.security.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    private final IAuthService authService;

    @Autowired
    public AuthController(IAuthService authService)
    {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserInfoResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest)
    {
        UserInfoResponse loggedInUser = authService.loginUser(loginRequest);

        return new ResponseEntity<>(loggedInUser, HttpStatus.ACCEPTED);
    }

    @PostMapping("/register")
    public ResponseEntity<UserInfoResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest)
    {
       UserInfoResponse registeredUser = authService.registerUser(registerRequest);

       return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<UserInfoResponse> updateUser(@Valid @RequestBody UpdateRequest updateRequest)
    {
       UserInfoResponse updatedUser = authService.updateUser(updateRequest);

       return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser()
    {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
