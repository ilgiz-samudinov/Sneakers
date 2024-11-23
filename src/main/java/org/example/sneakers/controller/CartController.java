package org.example.sneakers.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.sneakers.model.Cart;
import org.example.sneakers.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Добавить продукт в корзину
    @Operation(summary = "Добавить продукт в корзину", description = "Добавляет товар в корзину пользователя по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно добавлен в корзину"),
            @ApiResponse(responseCode = "400", description = "Ошибка, если товар не найден или другие ошибки ввода")
    })
    @PostMapping
    public Cart addProductToCart(
            @Parameter(description = "Идентификатор пользователя") @RequestParam("userId") Long userId,
            @Parameter(description = "Идентификатор продукта") @RequestParam("productId") Long productId,
            @Parameter(description = "Количество товара") @RequestParam("quantity") int quantity
    ) {
        return cartService.addProductToCart(userId, productId, quantity);
    }

    // Получить все товары в корзине
    @Operation(summary = "Получить все товары в корзине", description = "Возвращает все товары, добавленные в корзину пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список товаров в корзине"),
            @ApiResponse(responseCode = "404", description = "Корзина пользователя не найдена")
    })
    @GetMapping
    public List<Cart> getCartByUserId(
            @Parameter(description = "Идентификатор пользователя") @RequestParam("userId") Long userId
    ) {
        return cartService.getCartByUserId(userId);
    }

    // Удалить продукт из корзины
    @Operation(summary = "Удалить продукт из корзины", description = "Удаляет товар из корзины пользователя по его идентификатору продукта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно удалён из корзины"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден в корзине")
    })
    @DeleteMapping("/{productId}")
    public void removeProductFromCart(
            @Parameter(description = "Идентификатор пользователя") @RequestParam("userId") Long userId,
            @Parameter(description = "Идентификатор продукта для удаления") @PathVariable("productId") Long productId
    ) {
        cartService.removeProductFromCart(userId, productId);
    }
}
