package com.ikea.warehouseapp.data.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductUpdateMapper {

    void updateProductAvailableStock(@Param("id") Long id);
}
