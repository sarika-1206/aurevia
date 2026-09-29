package com.aurevia.ecommerce.service;

import com.aurevia.ecommerce.entity.CartItem;
import com.aurevia.ecommerce.entity.Product;
import com.aurevia.ecommerce.entity.User;
import com.aurevia.ecommerce.repository.CartItemRepository;
import com.aurevia.ecommerce.repository.ProductRepository;
import com.aurevia.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository) {

        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<CartItem> getCart(String email) {

        User user = getUser(email);

        return cartItemRepository.findByUser(user);
    }

    public CartItem addToCart(String email, Long productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        User user = getUser(email);

        Product product = productRepository.findById(productId)
                .filter(Product::isActive)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        CartItem cartItem = cartItemRepository
                .findByUserAndProduct(user, product)
                .orElse(null);

        if (cartItem == null) {

            cartItem = new CartItem();

            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);

        } else {

            int newQuantity = cartItem.getQuantity() + quantity;

            if (newQuantity > product.getStock()) {
                throw new RuntimeException("Insufficient stock");
            }

            cartItem.setQuantity(newQuantity);
        }

        return cartItemRepository.save(cartItem);
    }

    public CartItem updateQuantity(String email,
                                   Long cartItemId,
                                   Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        User user = getUser(email);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot modify this cart item");
        }

        if (quantity > cartItem.getProduct().getStock()) {
            throw new RuntimeException("Insufficient stock");
        }

        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    public void removeFromCart(String email, Long cartItemId) {

        User user = getUser(email);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot remove this cart item");
        }

        cartItemRepository.delete(cartItem);
    }

    public void clearCart(String email) {

        User user = getUser(email);

        cartItemRepository.deleteByUser(user);
    }

    private User getUser(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}