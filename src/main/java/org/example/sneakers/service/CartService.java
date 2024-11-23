
package org.example.sneakers.service;

import org.example.sneakers.model.Cart;
import org.example.sneakers.model.Product;
import org.example.sneakers.repository.CartRepository;
import org.example.sneakers.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // Добавить товар в корзину
    public Cart addProductToCart(Long userId, Long productId, int quantity) {
        // Проверка, существует ли продукт в базе
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new RuntimeException("Продукт не найден!");
        }

        Product product = productOpt.get();

        // Проверка на наличие достаточного количества товара в запасе
        if (product.getStock() < quantity) {
            throw new RuntimeException("Недостаточно товара на складе!");
        }

        // Проверка, есть ли уже такой товар в корзине
        Optional<Cart> existingCartItem = cartRepository.findByUserIdAndProductId(userId, productId);
        if (existingCartItem.isPresent()) {
            Cart cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity); // Увеличиваем количество
            return cartRepository.save(cartItem);
        }

        // Создаем новый товар для корзины
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setProductId(productId);
        cart.setQuantity(quantity);

        // Сохраняем корзину в БД
        return cartRepository.save(cart);
    }

    // Получить все товары в корзине для конкретного пользователя
    public List<Cart> getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    // Удалить товар из корзины
    public void removeProductFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("Продукт не найден в корзине!"));
        cartRepository.delete(cart);
    }

    // Очищение корзины пользователя после оформления заказа
    public void clearCart(Long userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(cartItems);
    }
}
