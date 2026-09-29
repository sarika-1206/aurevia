package com.aurevia.ecommerce.controller;

import com.aurevia.ecommerce.entity.Order;
import com.aurevia.ecommerce.entity.Product;
import com.aurevia.ecommerce.repository.OrderRepository;
import com.aurevia.ecommerce.repository.ProductRepository;
import com.aurevia.ecommerce.repository.UserRepository;
import com.aurevia.ecommerce.service.OrderService;
import com.aurevia.ecommerce.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public AdminController(
            ProductService productService,
            OrderService orderService,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            UserRepository userRepository) {

        this.productService = productService;
        this.orderService = orderService;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // PRODUCT MANAGEMENT
    // =========================

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(
            @RequestBody Product product) {

        Product savedProduct =
                productService.createProduct(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedProduct);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        return ResponseEntity.ok(
                productService.updateProduct(id, product)
        );
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }

    // =========================
    // ORDER MANAGEMENT
    // =========================

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {

        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }

    // =========================
    // ADMIN DASHBOARD STATS
    // =========================

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {

        long productCount = productRepository.count();
        long orderCount = orderRepository.count();
        long userCount = userRepository.count();

        Map<String, Object> stats = Map.of(
                "products", productCount,
                "orders", orderCount,
                "users", userCount,
                "storeStatus", "Live"
        );

        return ResponseEntity.ok(stats);
    }
}