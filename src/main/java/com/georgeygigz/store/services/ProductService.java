package com.georgeygigz.store.services;

import com.georgeygigz.store.dtos.ProductDto;
import com.georgeygigz.store.entities.Product;
import com.georgeygigz.store.exceptions.CategoryNotFoundException;
import com.georgeygigz.store.exceptions.ProductNotFoundException;
import com.georgeygigz.store.mappers.ProductMapper;
import com.georgeygigz.store.repositories.CategoryRepository;
import com.georgeygigz.store.repositories.ProductRepository;
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
        return product;
    }

    @Cacheable(value=PRODUCTS_CACHE)
    public List<Product> getAllProducts(Long categoryId){
        List<Product> products;
        if (categoryId !=null)
            products = productRepository.findByCategoryId(categoryId);
        else
            products = productRepository.findAllWithCategory();

        return products;
    }

    @Cacheable(value=PRODUCTS_CACHE, key="#productId")
    public ProductDto getProduct(Long productId){
        var product = productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new ProductNotFoundException();
        }
        return productMapper.toDto(product);
    }

    @CacheEvict(value=PRODUCTS_CACHE, allEntries = true)
    public ProductDto updateProduct(ProductDto productDto, Long productId){
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        productMapper.update(productDto, product);

        if (productDto.getCategoryId() != null) {
            var category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(CategoryNotFoundException::new);
            product.setCategory(category);
        }
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    @CacheEvict(value=PRODUCTS_CACHE, allEntries = true)
    public void deleteProduct(Long productId){
        productRepository.deleteById(productId);
    }
}
