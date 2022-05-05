package com.OnlineShop.repository;

import com.OnlineShop.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ICategoryRepository extends MongoRepository<Category, String>
{
}
