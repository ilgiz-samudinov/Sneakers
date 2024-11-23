package org.example.sneakers.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.sneakers.model.Order;
import org.example.sneakers.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Создать заказ
    @Operation(summary = "Создать новый заказ", description = "Создаёт заказ для пользователя с указанным адресом")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заказ успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка при создании заказа")
    })
    @PostMapping
    public Order createOrder(
            @Parameter(description = "Идентификатор пользователя, который создает заказ") @RequestParam("userId") Long userId,
            @Parameter(description = "Адрес доставки для заказа") @RequestParam("address") String address
    ) {
        return orderService.createOrder(userId, address);
    }

    // Получить заказ по ID
    @Operation(summary = "Получить заказ по ID", description = "Возвращает заказ по указанному идентификатору заказа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заказ найден"),
            @ApiResponse(responseCode = "404", description = "Заказ с таким ID не найден")
    })
    @GetMapping("/{orderId}")
    public Order getOrderById(
            @Parameter(description = "Идентификатор заказа") @PathVariable("orderId") Long orderId
    ) {
        return orderService.getOrderById(orderId);
    }

    // Получить все заказы пользователя
    @Operation(summary = "Получить все заказы пользователя", description = "Возвращает все заказы, связанные с указанным пользователем")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список заказов пользователя"),
            @ApiResponse(responseCode = "404", description = "Заказы для указанного пользователя не найдены")
    })
    @GetMapping
    public List<Order> getOrdersByUserId(
            @Parameter(description = "Идентификатор пользователя") @RequestParam("userId") Long userId
    ) {
        return orderService.getOrdersByUserId(userId);
    }
}
