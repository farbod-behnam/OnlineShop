package com.OnlineShop.repository;

import com.OnlineShop.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IProductRepository extends MongoRepository<Product, String>
{
}
