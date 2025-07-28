package com.georgeygigz.store.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Product description is required")
    private String description;

    @Positive(message = "Price cannot be less than zero")
    private BigDecimal price;

    @Positive(message = "Quantity cannot be less than zero")
    private Integer quantity;

    @NotNull
    private Long categoryId;
}
