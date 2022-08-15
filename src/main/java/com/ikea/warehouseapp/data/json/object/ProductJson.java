package com.ikea.warehouseapp.data.json.object;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
public class ProductJson {

    @JsonDeserialize(using = ProductListDeserializer.class)
    private List<Product> products;
}
