package com.OnlineShop.repository;

import com.OnlineShop.entity.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IUserRepository extends MongoRepository<AppUser, String>
{
    Optional<AppUser> findByUsername(String username);
}
