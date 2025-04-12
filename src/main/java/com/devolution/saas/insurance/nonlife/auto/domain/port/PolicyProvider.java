package com.devolution.saas.insurance.nonlife.auto.domain.port;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;

import java.util.Optional;
import java.util.UUID;

/**
 * Port pour accéder aux polices d'assurance depuis le domaine.
 * Cette interface respecte le principe d'inversion de dépendance
 * en permettant au domaine de définir ses besoins sans dépendre
 * de l'implémentation.
 */
public interface PolicyProvider {

    /**
     * Trouve une police par son ID.
     *
     * @param id             L'ID de la police
     * @param organizationId L'ID de l'organisation
     * @return La police trouvée, ou empty si non trouvée
     */
    Optional<AutoPolicy> findPolicyById(UUID id, UUID organizationId);

    /**
     * Vérifie si une police avec le numéro spécifié existe.
     *
     * @param policyNumber   Le numéro de police
     * @param organizationId L'ID de l'organisation
     * @return true si une police avec ce numéro existe, false sinon
     */
    boolean policyNumberExists(String policyNumber, UUID organizationId);
}
