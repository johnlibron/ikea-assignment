package com.ikea.warehouseapp.data.mapper;

import com.ikea.warehouseapp.data.dto.NewProductDto;
import com.ikea.warehouseapp.data.dto.ProductDto;
import com.ikea.warehouseapp.data.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    List<ProductDto> toDtoList(List<Product> products);

    ProductDto toDto(NewProductDto newProductDto);
}
