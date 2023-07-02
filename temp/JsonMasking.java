package com.bookmyticket.entity;

import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonMasking {

    public static void main(String[] args) throws JsonProcessingException {

        Theatre theatre = new Theatre();
        theatre.setPincode(60021);
        theatre.setTheatreCode("Code");

        Movie movie = new Movie();
        theatre.setMovie(movie);
        movie.setMovieName("name");

        System.out.println(maskRequiredField(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(theatre), "movieName"));

    }

    private static String maskRequiredField(String json, String fieldToMask)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        maskRequiredFieldHelper(root, fieldToMask);
        String maskedJson = mapper.writeValueAsString(root);
        return maskedJson;
    }

    private static void maskRequiredFieldHelper(JsonNode node, String fieldToMask) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                if (field.getKey().equals(fieldToMask)) {
                    objectNode.put(fieldToMask, "******");
                } else {
                    maskRequiredFieldHelper(field.getValue(), fieldToMask);
                }
            }
        }
    }

}
