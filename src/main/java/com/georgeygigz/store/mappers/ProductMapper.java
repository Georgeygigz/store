package com.georgeygigz.store.mappers;

import com.georgeygigz.store.dtos.ProductDto;
import com.georgeygigz.store.entities.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto (Product product);
    Product toEntity (ProductDto productDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void update(ProductDto productDto, @MappingTarget Product product);

}
