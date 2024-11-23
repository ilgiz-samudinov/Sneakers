
package org.example.sneakers.service;

import org.example.sneakers.model.Product;
import org.example.sneakers.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт не найден"));
    }


    public List<Product> searchProducts(String name, String category, Double minPrice, Double maxPrice) {
        return productRepository.findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndPriceBetween(
                name, category, minPrice, maxPrice);
    }

    public Product updateProduct(Long productId, Product productDetails) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Продукт не найден!"));

        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        product.setCategory(productDetails.getCategory());
        product.setImageUrl(productDetails.getImageUrl());

        return productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Продукт не найден!"));
        productRepository.delete(product);
    }

}
