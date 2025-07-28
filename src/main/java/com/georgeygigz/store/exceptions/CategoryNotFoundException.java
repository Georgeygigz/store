package com.georgeygigz.store.exceptions;

public class CategoryNotFoundException extends  RuntimeException{
    public CategoryNotFoundException(){
        super("Category not found");
    }
}
