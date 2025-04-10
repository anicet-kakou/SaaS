package com.devolution.saas.insurance.nonlife.auto.domain.service;

import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;

import java.util.List;
import java.util.UUID;

/**
 * Service de validation des véhicules.
 */
public interface VehicleValidator {

    /**
     * Valide un véhicule pour la création.
     *
     * @param vehicle        Le véhicule à valider
     * @param organizationId L'ID de l'organisation
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateForCreation(Vehicle vehicle, UUID organizationId);

    /**
     * Valide un véhicule pour la mise à jour.
     *
     * @param vehicle         Le véhicule à valider
     * @param existingVehicle Le véhicule existant
     * @param organizationId  L'ID de l'organisation
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateForUpdate(Vehicle vehicle, Vehicle existingVehicle, UUID organizationId);

    /**
     * Valide l'existence des entités référencées par le véhicule.
     *
     * @param vehicle        Le véhicule à valider
     * @param organizationId L'ID de l'organisation
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateReferences(Vehicle vehicle, UUID organizationId);

    /**
     * Valide les règles métier spécifiques pour un véhicule.
     *
     * @param vehicle Le véhicule à valider
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateBusinessRules(Vehicle vehicle);
}
