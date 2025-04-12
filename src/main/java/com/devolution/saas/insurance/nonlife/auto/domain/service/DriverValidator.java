package com.devolution.saas.insurance.nonlife.auto.domain.service;

import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;

import java.util.List;
import java.util.UUID;

/**
 * Service de validation des conducteurs.
 */
public interface DriverValidator {

    /**
     * Valide un conducteur pour la création.
     *
     * @param driver         Le conducteur à valider
     * @param organizationId L'ID de l'organisation
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateForCreation(Driver driver, UUID organizationId);

    /**
     * Valide un conducteur pour la mise à jour.
     *
     * @param driver         Le conducteur à valider
     * @param existingDriver Le conducteur existant
     * @param organizationId L'ID de l'organisation
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateForUpdate(Driver driver, Driver existingDriver, UUID organizationId);

    /**
     * Valide l'existence des entités référencées par le conducteur.
     *
     * @param driver         Le conducteur à valider
     * @param organizationId L'ID de l'organisation
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateReferences(Driver driver, UUID organizationId);

    /**
     * Valide les règles métier spécifiques pour un conducteur.
     *
     * @param driver Le conducteur à valider
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateBusinessRules(Driver driver);
}
