package com.aurevia.ecommerce.controller;

import com.aurevia.ecommerce.entity.Order;
import com.aurevia.ecommerce.entity.OrderItem;
import com.aurevia.ecommerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<Order> placeOrder(
            Authentication authentication) {

        return ResponseEntity.ok(
                orderService.placeOrder(
                        authentication.getName()
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders(
            Authentication authentication) {

        return ResponseEntity.ok(
                orderService.getMyOrders(
                        authentication.getName()
                )
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(
            Authentication authentication,
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderService.getOrder(
                        authentication.getName(),
                        orderId
                )
        );
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItem>> getOrderItems(
            Authentication authentication,
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderService.getOrderItems(
                        authentication.getName(),
                        orderId
                )
        );
    }
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            Authentication authentication,
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
            orderService.cancelOrder(authentication.getName(), orderId)
        );
    }


}