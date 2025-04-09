package com.devolution.saas.insurance.nonlife.auto.reference.application.service;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.FuelTypeDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Service d'application pour la gestion des données de référence.
 * Ce service fournit une interface commune pour accéder à toutes les données de référence.
 */
public interface ReferenceDataService {

    /**
     * Récupère tous les types de carburant actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carburant actifs
     */
    List<FuelTypeDTO> getActiveFuelTypes(UUID organizationId);

    /**
     * Récupère un type de carburant par son ID.
     *
     * @param id             L'ID du type de carburant
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant, ou empty si non trouvé
     */
    Optional<FuelTypeDTO> getFuelTypeById(UUID id, UUID organizationId);

    /**
     * Récupère un type de carburant par son code.
     *
     * @param code           Le code du type de carburant
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant, ou empty si non trouvé
     */
    Optional<FuelTypeDTO> getFuelTypeByCode(String code, UUID organizationId);

    /**
     * Initialise les données de référence standard pour une nouvelle organisation.
     *
     * @param organizationId L'ID de l'organisation
     */
    void initializeStandardReferenceData(UUID organizationId);

    /**
     * Vérifie si une référence existe.
     *
     * @param referenceType  Le type de référence
     * @param referenceId    L'ID de la référence
     * @param organizationId L'ID de l'organisation
     * @return true si la référence existe, false sinon
     */
    boolean referenceExists(String referenceType, UUID referenceId, UUID organizationId);

    /**
     * Récupère les données de référence pour l'interface utilisateur.
     * Cette méthode retourne toutes les données de référence nécessaires pour l'interface utilisateur.
     *
     * @param organizationId L'ID de l'organisation
     * @return Une carte des données de référence
     */
    Map<String, List<?>> getReferenceDataForUI(UUID organizationId);
}
