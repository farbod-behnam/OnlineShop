package com.OnlineShop.repository;

import com.OnlineShop.entity.AppRole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IRoleRepository extends MongoRepository<AppRole, String>
{
    Optional<AppRole> findByName(String name);
}
