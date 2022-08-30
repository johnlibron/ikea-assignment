package com.ikea.warehouseapp.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonMapperUtils {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public static <T> T toObject(final File file, final Class<T> type) throws IOException {
        // TODO - Validate Incoming Json With Json Schema
        // TODO - Add Json token validation for products data structure
        JsonParser parser = null;
        try {
            final JsonFactory factory = new JsonFactory();
            parser = factory.createParser(file);
            parser.setCodec(jsonMapper);
            return jsonMapper.readValue(parser, type);
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }
}
