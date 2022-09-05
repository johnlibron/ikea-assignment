package com.ikea.warehouseapp.data.mybatis;

import com.ikea.warehouseapp.data.dto.NewProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductCreateMapper {

    void addNewProduct(@Param("newProductId") long newProductId, @Param("newProductDto") NewProductDto newProductDto);
}
