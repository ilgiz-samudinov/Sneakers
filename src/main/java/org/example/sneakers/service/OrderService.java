package org.example.sneakers.service;

import org.example.sneakers.model.Cart;
import org.example.sneakers.model.Order;
import org.example.sneakers.model.Product;
import org.example.sneakers.repository.CartRepository;
import org.example.sneakers.repository.OrderRepository;
import org.example.sneakers.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    // Создание заказа
    public Order createOrder(Long userId, String address) {
        // Получаем все товары из корзины пользователя
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Корзина пуста!");
        }

        double totalAmount = 0;

        // Проверка доступности каждого товара
        for (Cart cartItem : cartItems) {
            Optional<Product> productOpt = productRepository.findById(cartItem.getProductId());
            if (productOpt.isEmpty()) {
                throw new RuntimeException("Продукт не найден!");
            }

            Product product = productOpt.get();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Недостаточно товара на складе для " + product.getName());
            }

            // Рассчитываем общую сумму заказа
            totalAmount += product.getPrice() * cartItem.getQuantity();
        }

        // Создаем новый заказ
        Order order = new Order();
        order.setUserId(userId); // Устанавливаем ID пользователя
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalAmount);

        // Сохраняем заказ
        Order savedOrder = orderRepository.save(order);

        // После создания заказа очищаем корзину
        cartRepository.deleteAll(cartItems);

        return savedOrder;
    }

    // Получить заказ по ID
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден!"));
    }

    // Получить заказы пользователя по его ID
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
