package com.devolution.saas.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Utilitaire pour la manipulation des données JSON.
 */
@Slf4j
public final class JsonUtils {

    private static final ObjectMapper objectMapper = createObjectMapper();

    private JsonUtils() {
        // Constructeur privé pour empêcher l'instanciation
    }

    /**
     * Crée et configure un ObjectMapper.
     *
     * @return ObjectMapper configuré
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    /**
     * Convertit un objet en chaîne JSON.
     *
     * @param object Objet à convertir
     * @return Chaîne JSON
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Erreur lors de la conversion en JSON", e);
            throw new RuntimeException("Erreur lors de la conversion en JSON", e);
        }
    }

    /**
     * Convertit une chaîne JSON en objet.
     *
     * @param json  Chaîne JSON
     * @param clazz Classe de l'objet
     * @param <T>   Type de l'objet
     * @return Objet converti
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("Erreur lors de la conversion depuis JSON", e);
            throw new RuntimeException("Erreur lors de la conversion depuis JSON", e);
        }
    }

    /**
     * Convertit une chaîne JSON en JsonNode.
     *
     * @param json Chaîne JSON
     * @return JsonNode
     */
    public static JsonNode toJsonNode(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            log.error("Erreur lors de la conversion en JsonNode", e);
            throw new RuntimeException("Erreur lors de la conversion en JsonNode", e);
        }
    }

    /**
     * Vérifie si une chaîne est un JSON valide.
     *
     * @param json Chaîne à vérifier
     * @return true si la chaîne est un JSON valide, false sinon
     */
    public static boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Récupère l'ObjectMapper pour un usage personnalisé.
     *
     * @return ObjectMapper
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
