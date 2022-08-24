package com.ikea.warehouseapp.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectionUtils {

    public static <T> Set<T> getDuplicateItems(List<T> data) {
        Set<T> items = new HashSet<>();
        return data.stream().filter(n -> !items.add(n)).collect(Collectors.toSet());
    }
}
