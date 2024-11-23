package org.example.sneakers.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.sneakers.model.Product;
import org.example.sneakers.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Получить список всех продуктов с фильтрами
    @Operation(summary = "Получить все продукты", description = "Возвращает все продукты, с возможностью фильтрации по имени, категории и цене")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список продуктов успешно возвращён"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры запроса")
    })
    @GetMapping
    public List<Product> getAllProducts(
            @Parameter(description = "Название продукта для поиска (необязательно)")
            @RequestParam(value = "name", required = false, defaultValue = "") String name,

            @Parameter(description = "Категория продукта для поиска (необязательно)")
            @RequestParam(value = "category", required = false, defaultValue = "") String category,

            @Parameter(description = "Минимальная цена продукта для поиска (необязательно)", example = "0")
            @RequestParam(value = "minPrice", required = false, defaultValue = "0") Double minPrice,

            @Parameter(description = "Максимальная цена продукта для поиска (необязательно)", example = "100000")
            @RequestParam(value = "maxPrice", required = false, defaultValue = "100000") Double maxPrice
    ) {
        return productService.searchProducts(name, category, minPrice, maxPrice);
    }
}
