package com.OnlineShop.security.service;

import com.OnlineShop.dto.request.LoginRequest;
import com.OnlineShop.dto.request.RegisterRequest;
import com.OnlineShop.dto.request.UpdateRequest;
import com.OnlineShop.dto.response.UserInfoResponse;

/**
 * An interface for handling authentication
 */
public interface IAuthService
{
    /**
     * @param loginRequest a class containing the username and password attribute ({@link LoginRequest})
     * @return userInfoResponse a class that contains essential user information ({@link UserInfoResponse})
     */
    UserInfoResponse loginUser(LoginRequest loginRequest);

    /**
     * @param registerRequest  a class containing attribute for registering user ({@link RegisterRequest})
     * @return userInfoResponse a class that contains essential user information ({@link UserInfoResponse})
     */
    UserInfoResponse registerUser(RegisterRequest registerRequest);

    /**
     *
     * @param updateRequest a class containing attribute for updating user ({@link RegisterRequest})
     * @return userInfoResponse a class that contains essential user information ({@link UserInfoResponse}) n
     */
    UserInfoResponse updateUser(UpdateRequest updateRequest);
}
