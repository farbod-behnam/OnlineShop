package com.OnlineShop.repository;

import com.OnlineShop.entity.Country;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ICountryRepository extends MongoRepository<Country, String>
{
    Optional<Country> findCountryByName(String name);
}
