package com.devolution.saas.insurance.nonlife.auto.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des polices d'assurance automobile.
 */
public interface AutoPolicyRepository {

    /**
     * Sauvegarde une police d'assurance automobile.
     *
     * @param autoPolicy La police à sauvegarder
     * @return La police sauvegardée
     */
    AutoPolicy save(AutoPolicy autoPolicy);

    /**
     * Trouve une police par son ID.
     *
     * @param id L'ID de la police
     * @return La police trouvée, ou empty si non trouvée
     */
    Optional<AutoPolicy> findById(UUID id);

    /**
     * Trouve une police par son numéro.
     *
     * @param policyNumber   Le numéro de police
     * @param organizationId L'ID de l'organisation
     * @return La police trouvée, ou empty si non trouvée
     */
    Optional<AutoPolicy> findByPolicyNumberAndOrganizationId(String policyNumber, UUID organizationId);

    /**
     * Liste toutes les polices d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des polices
     */
    List<AutoPolicy> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste toutes les polices actives d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des polices actives
     */
    List<AutoPolicy> findAllActiveByOrganizationId(UUID organizationId);

    /**
     * Liste toutes les polices d'un véhicule.
     *
     * @param vehicleId      L'ID du véhicule
     * @param organizationId L'ID de l'organisation
     * @return La liste des polices
     */
    List<AutoPolicy> findAllByVehicleIdAndOrganizationId(UUID vehicleId, UUID organizationId);

    /**
     * Liste toutes les polices d'un conducteur principal.
     *
     * @param driverId       L'ID du conducteur
     * @param organizationId L'ID de l'organisation
     * @return La liste des polices
     */
    List<AutoPolicy> findAllByPrimaryDriverIdAndOrganizationId(UUID driverId, UUID organizationId);

    /**
     * Liste toutes les polices qui expirent entre deux dates.
     *
     * @param startDate      La date de début
     * @param endDate        La date de fin
     * @param organizationId L'ID de l'organisation
     * @return La liste des polices
     */
    List<AutoPolicy> findAllByEndDateBetweenAndOrganizationId(LocalDate startDate, LocalDate endDate, UUID organizationId);

    /**
     * Supprime une police.
     *
     * @param id L'ID de la police à supprimer
     */
    void deleteById(UUID id);
}
