package com.devolution.saas.insurance.nonlife.auto.domain.service;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;

import java.util.List;
import java.util.UUID;

/**
 * Service de validation des polices d'assurance automobile.
 */
public interface PolicyValidator {

    /**
     * Valide une police pour la création.
     *
     * @param policy         La police à valider
     * @param organizationId L'ID de l'organisation
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateForCreation(AutoPolicy policy, UUID organizationId);

    /**
     * Valide une police pour la mise à jour.
     *
     * @param policy         La police à valider
     * @param existingPolicy La police existante
     * @param organizationId L'ID de l'organisation
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateForUpdate(AutoPolicy policy, AutoPolicy existingPolicy, UUID organizationId);

    /**
     * Valide l'existence des entités référencées par la police.
     *
     * @param policy         La police à valider
     * @param organizationId L'ID de l'organisation
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateReferences(AutoPolicy policy, UUID organizationId);

    /**
     * Valide les règles métier spécifiques pour une police.
     *
     * @param policy La police à valider
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateBusinessRules(AutoPolicy policy);
}
