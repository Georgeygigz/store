package com.georgeygigz.store.controllers;

import com.georgeygigz.store.dtos.ProductDto;
import com.georgeygigz.store.mappers.ProductMapper;
import com.georgeygigz.store.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
@Tag(name = "Products")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);


    @PostMapping
    @Operation(summary = "Add a product")
    public ResponseEntity<ProductDto> addProduct(
            @Valid @RequestBody ProductDto request,
            UriComponentsBuilder uriBuilder
    ){
        var product = productService.addProduct(request);

        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(productMapper.toDto(product));
    }

    @GetMapping
    @Operation(summary = "Get all products or products using categoryId")
    public Iterable<ProductDto> getAllProducts(@RequestParam(name = "categoryId", required = false) Long categoryId){
        var products = productService.getAllProducts(categoryId);
        return products.stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get a product using productId")
    public ProductDto getProduct(@PathVariable("productId") Long productId){
        logger.info("Fetching product with ID: {}", productId);
        return productService.getProduct(productId);
    }

    @PatchMapping("/{productId}")
    @Operation(summary = "Update a product")
    public ProductDto updateProduct(
            @PathVariable("productId") Long productId,
            @RequestBody ProductDto request
    ){
        return productService.updateProduct(request, productId);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete a product")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

}
