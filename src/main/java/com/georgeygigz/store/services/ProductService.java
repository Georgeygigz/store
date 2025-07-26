package com.georgeygigz.store.services;

import com.georgeygigz.store.dtos.ProductDto;
import com.georgeygigz.store.entities.Product;
import com.georgeygigz.store.exceptions.CategoryNotFoundException;
import com.georgeygigz.store.exceptions.ProductNotFoundException;
import com.georgeygigz.store.mappers.ProductMapper;
import com.georgeygigz.store.repositories.CategoryRepository;
import com.georgeygigz.store.repositories.ProductRepository;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductEventService eventService;
    private final ProductLockService lockService;


    public static final String PRODUCTS_CACHE = "products";


    @CacheEvict(value=PRODUCTS_CACHE, allEntries = true)
    public Product addProduct(ProductDto productDto){
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if(category==null){
            throw new CategoryNotFoundException();
        }
        var product = productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);

        //Async send message to RabbitMQ
        eventService.sendProductCreatedEvent(product);
        return product;
    }

    @RateLimiter(name = "productService")
    @Cacheable(value=PRODUCTS_CACHE)
    public List<Product> getAllProducts(Long categoryId){
        List<Product> products;
        if (categoryId !=null)
            products = productRepository.findByCategoryId(categoryId);
        else
            products = productRepository.findAllWithCategory();

        return products;
    }

    @RateLimiter(name = "productService")
    @Cacheable(value=PRODUCTS_CACHE, key="#productId")
    public ProductDto getProduct(Long productId){
        // Check Redis blocker first
        if (lockService.isBlocked(productId)) {
            throw new ProductNotFoundException();
        }

        var product = productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new ProductNotFoundException();
        }
        return productMapper.toDto(product);
    }

    @CacheEvict(value=PRODUCTS_CACHE, allEntries = true)
    public ProductDto updateProduct(ProductDto productDto, Long productId){

        //Set blocker during update
        lockService.blockProduct(productId);

        try {
            var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
            productMapper.update(productDto, product);

            if (productDto.getCategoryId() != null) {
                var category = categoryRepository.findById(productDto.getCategoryId())
                        .orElseThrow(CategoryNotFoundException::new);
                product.setCategory(category);
            }
            productRepository.save(product);

            // Async send message to RabbitMQ
            eventService.sendProductUpdatedEvent(product);

            return productMapper.toDto(product);
        }finally {

            // Release blocker after update
            lockService.unblockProduct(productId);
        }
    }

    @CacheEvict(value=PRODUCTS_CACHE, allEntries = true)
    public void deleteProduct(Long productId){

        // Set blocker during deletion
        lockService.blockProduct(productId);

        try{
            productRepository.deleteById(productId);

            // Async send message to RabbitMQ
            eventService.sendProductDeletedEvent(productId);
        }finally {
            // Release blocker after deletion
            lockService.unblockProduct(productId);
        }

    }

}
