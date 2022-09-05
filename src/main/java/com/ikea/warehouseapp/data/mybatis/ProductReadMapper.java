package com.ikea.warehouseapp.data.mybatis;

import com.ikea.warehouseapp.data.Page;
import com.ikea.warehouseapp.data.dto.AvailableProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductReadMapper {
    // TODO: Transfer to SQL file per query to have elegant

    long selectNextProductIdSeq();

    List<AvailableProductDto> findAvailableProducts(@Param("page") Page page);

    long countAvailableProducts();

    AvailableProductDto findProductAvailableStock(@Param("id") Long id);

    List<String> findProductsByNameIn(@Param("productNames") List<String> productNames);
}
