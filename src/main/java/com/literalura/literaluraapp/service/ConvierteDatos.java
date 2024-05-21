package com.literalura.literaluraapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ConvierteDatos implements IConvierteDatos {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            // Parsear la cadena JSON a un JsonNode
            JsonNode rootNode = objectMapper.readTree(json);

            // Obtener el array de results
            JsonNode resultsArray = rootNode.get("results");


            if (resultsArray != null && resultsArray.size() > 0) {
                // Obtener el primer objeto en el array de resultados
                JsonNode firstResult = resultsArray.get(0);

                // Convertir el primer resultado a la clase especifica
                return objectMapper.treeToValue(firstResult, clase);
            } else {
                // Manejar el caso en el que no se encontraron resultado
                throw new RuntimeException("No se encontraron resultados en el JSON.");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public <T> List<T> obtenerDatosArray(String json, Class<T> clase) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);

            JsonNode resultsArray = rootNode.get("results");

            if (resultsArray != null && resultsArray.size() > 0) {
                List<T> resultList = new ArrayList<>();
                for (JsonNode node : resultsArray) {
                    T result = objectMapper.treeToValue(node, clase);
                    resultList.add(result);
                }
                return resultList;
            } else {
                throw new RuntimeException("No se encontraron resultados en el JSON.");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
