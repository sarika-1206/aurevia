package com.aurevia.ecommerce.repository;

import com.aurevia.ecommerce.entity.OrderItem;
import com.aurevia.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
}