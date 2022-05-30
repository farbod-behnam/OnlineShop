package com.OnlineShop.security.service;

import com.OnlineShop.dto.request.LoginRequest;
import com.OnlineShop.dto.request.RegisterRequest;
import com.OnlineShop.dto.request.UpdateRequest;
import com.OnlineShop.dto.response.UserInfoResponse;

public interface IAuthService
{
    UserInfoResponse loginUser(LoginRequest loginRequest);
    UserInfoResponse registerUser(RegisterRequest registerRequest);
    UserInfoResponse updateUser(UpdateRequest updateRequest);
}
