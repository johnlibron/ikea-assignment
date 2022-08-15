package com.ikea.warehouseapp.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonMapperUtils {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public static <T> T toObject(final Class<T> type, final JsonParser parser) throws IOException {
        parser.setCodec(jsonMapper);
        return jsonMapper.readValue(parser, type);
    }
}
