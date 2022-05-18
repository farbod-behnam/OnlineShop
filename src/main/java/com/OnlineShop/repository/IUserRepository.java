package com.OnlineShop.repository;

import com.OnlineShop.entity.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IUserRepository extends MongoRepository<AppUser, String>
{
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByPhoneNumber(String email);
    Optional<AppUser> findByUsernameOrEmailOrPhoneNumber(String username, String email, String phoneNumber);
}
