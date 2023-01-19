package com.cg.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.convert.ReadingConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ReadingConverter
public class JsonToMapConverter {
        private static final Logger LOGGER = LoggerFactory.getLogger(JsonToMapConverter.class);

        public static Map<String, ?> convertToDatabaseColumn(String attribute)
        {
            if (attribute == null) {
                return new HashMap<>();
            }
            try
            {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(attribute, HashMap.class);
            }
            catch (IOException e) {
                LOGGER.error("Convert error while trying to convert string(JSON) to map data structure.");
            }
            return new HashMap<>();
        }

        public static String convertToEntityAttribute(Map<String, ?> dbData)
        {
            try
            {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.writeValueAsString(dbData);
            }
            catch (JsonProcessingException e)
            {
                LOGGER.error("Could not convert map to json string.");
                return null;
            }
        }
}
