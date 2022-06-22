package com.OnlineShop.repository;

import com.OnlineShop.entity.AppUser;
import com.OnlineShop.entity.order.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IOrderRepository extends MongoRepository<Order, String>
{
    List<Order> findOrdersByUser(AppUser user);
    Optional<Order> findOrderByUserAndId(AppUser user, String id);
}
