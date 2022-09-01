package com.ikea.warehouseapp.data.mybatis;

import com.ikea.warehouseapp.data.Page;
import com.ikea.warehouseapp.data.dto.AvailableProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductReadMapper {

    List<AvailableProductDto> findAvailableProducts(@Param("page") Page page);

    long countAvailableProducts();

    AvailableProductDto findProductAvailableStock(@Param("id") Long id);
}
