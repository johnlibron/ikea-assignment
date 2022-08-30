package com.ikea.warehouseapp.data.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ikea.warehouseapp.data.json.serializer.ProductsDeserializer;
import com.ikea.warehouseapp.data.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Products {

    @JsonDeserialize(using = ProductsDeserializer.class)
    private List<Product> products;
}
