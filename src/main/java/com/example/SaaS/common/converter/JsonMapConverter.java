package com.example.SaaS.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Map;

@Converter
public class JsonMapConverter implements AttributeConverter<Map<String, String>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, String> attribute) {
        try {
            if (attribute == null || attribute.isEmpty()) {
                return "{}";
            }

            return mapper.writeValueAsString(attribute);

        } catch (Exception e) {
            throw new RuntimeException("Error converting Map to JSON", e);
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.trim().isEmpty()) {
                return Collections.emptyMap();
            }

            return mapper.readValue(
                    dbData,
                    new TypeReference<Map<String, String>>() {}
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error converting JSON to Map. DB value: " + dbData,
                    e
            );
        }
    }
}