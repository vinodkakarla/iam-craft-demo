package com.intuit.interview.demo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * This class is a utility that handles the conversion between JSON and Java objects.
 * It uses the Jackson ObjectMapper for the conversion.
 * It also logs any errors that occur during the conversion.
 */
@Component
public class JsonUtils {

    // The Logger used to log messages
    private final Logger logger = Logger.getLogger(JsonUtils.class.getName());

    // The ObjectMapper used for the conversion between JSON and Java objects
    @Autowired
    private ObjectMapper mapper;

    /**
     * This constructor initializes the JsonUtils.
     * It was originally intended to configure the ObjectMapper, but the configuration has been commented out.
     */
    public JsonUtils() {
//        this.mapper = new ObjectMapper();
//        this.mapper
//                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * This method converts a JSON string to a Java object of the specified class.
     *
     * @param response the JSON string to convert
     * @param theClass the class of the Java object to convert to
     * @return the converted Java object
     * @throws Exception if an error occurs during the conversion
     */
    public <T> T covertToObject(String response, Class<T> theClass) throws Exception {
        return mapper.readValue(response, theClass);
    }

    /**
     * This method converts a Java object to a JSON string.
     * If an error occurs during the conversion, it logs the error and returns the string representation of the object.
     *
     * @param obj the Java object to convert
     * @return the converted JSON string, or the string representation of the object if an error occurs
     */
    public String getJsonFromObject(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.warning("Error while converting object to json: " + e.getMessage());
            return obj.toString();
        }
    }

}