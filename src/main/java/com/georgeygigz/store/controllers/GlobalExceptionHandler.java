package com.georgeygigz.store.controllers;

import com.georgeygigz.store.dtos.ErrorDto;
import com.georgeygigz.store.exceptions.CategoryNotFoundException;
import com.georgeygigz.store.exceptions.ProductNotFoundException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDto> handleProductNotFound(Exception ex){
        return ResponseEntity.badRequest().body(
                new ErrorDto(ex.getMessage())
        );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDto> handleCategoryNotFound(Exception ex){
        return ResponseEntity.badRequest().body(
                new ErrorDto(ex.getMessage())
        );
    }

    @ExceptionHandler(RequestNotPermitted.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResponseEntity<ErrorDto> handleRateLimiterException(RequestNotPermitted ex){
        return ResponseEntity.badRequest().body(
                new ErrorDto("Too many request You have exceeded your rate limit. Please try again later")
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException exception
    ) {
        var errors = new HashMap<String, String>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
