package com.devolution.saas.insurance.common.domain.service;

import com.devolution.saas.insurance.common.domain.model.Policy;

import java.util.List;
import java.util.UUID;

/**
 * Interface commune pour la validation des polices d'assurance.
 * Définit les méthodes que tous les validateurs de police doivent implémenter.
 */
public interface PolicyValidator<T extends Policy> {

    /**
     * Valide une police pour la création.
     *
     * @param policy         La police à valider
     * @param organizationId L'ID de l'organisation
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateForCreation(T policy, UUID organizationId);

    /**
     * Valide une police pour la mise à jour.
     *
     * @param policy         La police à valider
     * @param existingPolicy La police existante
     * @param organizationId L'ID de l'organisation
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateForUpdate(T policy, T existingPolicy, UUID organizationId);

    /**
     * Valide l'existence des entités référencées par la police.
     *
     * @param policy         La police à valider
     * @param organizationId L'ID de l'organisation
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateReferences(T policy, UUID organizationId);

    /**
     * Valide les règles métier spécifiques pour une police.
     *
     * @param policy La police à valider
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateBusinessRules(T policy);

    /**
     * Valide les garanties sélectionnées pour une police.
     *
     * @param policy         La police à valider
     * @param organizationId L'ID de l'organisation
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateCoverages(T policy, UUID organizationId);

    /**
     * Valide les dates de la police.
     *
     * @param policy La police à valider
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateDates(T policy);

    /**
     * Valide le montant de la prime.
     *
     * @param policy La police à valider
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validatePremium(T policy);
}
