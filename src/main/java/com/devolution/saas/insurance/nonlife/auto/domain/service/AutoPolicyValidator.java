package com.devolution.saas.insurance.nonlife.auto.domain.service;

import com.devolution.saas.insurance.common.domain.service.PolicyValidator;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;

import java.util.List;
import java.util.UUID;

/**
 * Interface pour la validation des polices d'assurance automobile.
 * Étend l'interface commune PolicyValidator en la spécialisant pour AutoPolicy.
 */
public interface AutoPolicyValidator extends PolicyValidator<AutoPolicy> {

    /**
     * Valide les informations spécifiques à l'assurance auto.
     *
     * @param policy La police à valider
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateAutoSpecificRules(AutoPolicy policy);

    /**
     * Valide les informations du véhicule associé à la police.
     *
     * @param policy         La police à valider
     * @param organizationId L'ID de l'organisation
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateVehicle(AutoPolicy policy, UUID organizationId);

    /**
     * Valide les informations du conducteur associé à la police.
     *
     * @param policy         La police à valider
     * @param organizationId L'ID de l'organisation
     * @return La liste des erreurs de validation, vide si aucune erreur
     */
    List<String> validateDriver(AutoPolicy policy, UUID organizationId);
}
