package com.example.online_store.service;
import com.example.online_store.entity.CartItem;
import com.example.online_store.entity.Product;
import com.example.online_store.entity.User;
import com.example.online_store.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;


    /*
     * ============================
     * GET CART ITEMS
     * ============================
     */
    public List<CartItem> getCartItems(User user) {

        return cartItemRepository.findByUser(user);
    }


    /*
     * ============================
     * ADD PRODUCT TO CART
     * ============================
     */
    public void addToCart(User user, Product product) {

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        if (product.getStock() <= 0) {
            throw new RuntimeException("Product is out of stock");
        }

        CartItem existingItem =
                cartItemRepository.findByUserAndProduct(user, product)
                        .orElse(null);

        if (existingItem != null) {

            int newQuantity = existingItem.getQuantity() + 1;

            if (newQuantity > product.getStock()) {
                throw new RuntimeException(
                        "Cannot add more than available stock"
                );
            }

            existingItem.setQuantity(newQuantity);

            cartItemRepository.save(existingItem);

        } else {

            CartItem cartItem = new CartItem();

            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);

            cartItemRepository.save(cartItem);
        }
    }


    /*
     * ============================
     * INCREASE QUANTITY
     * ============================
     */
    public void increaseQuantity(
            Long cartItemId,
            User user) {

        CartItem cartItem = getUserCartItem(cartItemId, user);

        Product product = cartItem.getProduct();

        int newQuantity = cartItem.getQuantity() + 1;

        if (newQuantity > product.getStock()) {
            throw new RuntimeException(
                    "Cannot add more. Only "
                            + product.getStock()
                            + " item(s) available."
            );
        }

        cartItem.setQuantity(newQuantity);

        cartItemRepository.save(cartItem);
    }


    /*
     * ============================
     * DECREASE QUANTITY
     * ============================
     */
    public void decreaseQuantity(
            Long cartItemId,
            User user) {

        CartItem cartItem = getUserCartItem(cartItemId, user);

        int newQuantity = cartItem.getQuantity() - 1;

        if (newQuantity <= 0) {

            cartItemRepository.delete(cartItem);

        } else {

            cartItem.setQuantity(newQuantity);

            cartItemRepository.save(cartItem);
        }
    }


    /*
     * ============================
     * REMOVE FROM CART
     * ============================
     */
    public void removeFromCart(
            Long cartItemId,
            User user) {

        CartItem cartItem = getUserCartItem(cartItemId, user);

        cartItemRepository.delete(cartItem);
    }


    /*
     * ============================
     * CALCULATE CART TOTAL
     * ============================
     */
    public BigDecimal getCartTotal(User user) {

        List<CartItem> cartItems =
                cartItemRepository.findByUser(user);

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cartItems) {

            Product product = item.getProduct();

            BigDecimal price = getProductPrice(product);

            BigDecimal quantity =
                    BigDecimal.valueOf(item.getQuantity());

            BigDecimal subtotal =
                    price.multiply(quantity);

            total = total.add(subtotal);
        }

        return total;
    }


    /*
     * ============================
     * GET PRODUCT PRICE
     * ============================
     */
    private BigDecimal getProductPrice(Product product) {

        if (product.isOnSale()
                && product.getSalePrice() != null) {

            return product.getSalePrice();
        }

        return product.getPrice();
    }


    /*
     * ============================
     * FIND CART ITEM BELONGING
     * TO CURRENT USER
     * ============================
     */
    private CartItem getUserCartItem(
            Long cartItemId,
            User user) {

        CartItem cartItem =
                cartItemRepository.findById(cartItemId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Cart item not found"
                                )
                        );

        /*
         * Security check:
         * Make sure the cart item belongs
         * to the currently logged-in user.
         */
        if (!cartItem.getUser().getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "You are not allowed to modify this cart item"
            );
        }

        return cartItem;
    }
}
