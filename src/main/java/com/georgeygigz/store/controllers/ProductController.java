package com.georgeygigz.store.controllers;

import com.georgeygigz.store.dtos.ProductDto;
import com.georgeygigz.store.exceptions.CategoryNotFoundException;
import com.georgeygigz.store.exceptions.ProductNotFoundException;
import com.georgeygigz.store.mappers.ProductMapper;
import com.georgeygigz.store.repositories.CategoryRepository;
import com.georgeygigz.store.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final CategoryRepository categoryRepository;
    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(
            @RequestBody ProductDto request,
            UriComponentsBuilder uriBuilder
    ){
        var product = productService.addProduct(request);

        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(productMapper.toDto(product));
    }

    @GetMapping
    public Iterable<ProductDto> getAllProducts(@RequestParam(name = "categoryId", required = false) Long categoryId){
        var products = productService.getAllProducts(categoryId);
        return products.stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable("productId") Long productId){
        return productService.getProduct(productId);
    }

    @PatchMapping("/{productId}")
    public ProductDto updateProduct(
            @PathVariable("productId") Long productId,
            @RequestBody ProductDto request
    ){
        return productService.updateProduct(request, productId);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCategoryNotFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Category not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found"));
    }
}
