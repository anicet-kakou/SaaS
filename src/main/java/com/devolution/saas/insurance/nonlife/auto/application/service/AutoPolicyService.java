package com.devolution.saas.insurance.nonlife.auto.application.service;

import com.devolution.saas.insurance.nonlife.auto.application.dto.AutoPolicyDTO;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service d'application pour la gestion des polices d'assurance automobile.
 */
public interface AutoPolicyService {

    /**
     * Crée une nouvelle police d'assurance automobile.
     *
     * @param policy         La police à créer
     * @param organizationId L'ID de l'organisation
     * @return Le DTO de la police créée
     */
    AutoPolicyDTO createPolicy(AutoPolicy policy, UUID organizationId);

    /**
     * Met à jour une police d'assurance automobile existante.
     *
     * @param id             L'ID de la police à mettre à jour
     * @param policy         La police avec les nouvelles valeurs
     * @param organizationId L'ID de l'organisation
     * @return Le DTO de la police mise à jour, ou empty si non trouvée
     */
    Optional<AutoPolicyDTO> updatePolicy(UUID id, AutoPolicy policy, UUID organizationId);

    /**
     * Récupère une police par son ID.
     *
     * @param id             L'ID de la police
     * @param organizationId L'ID de l'organisation
     * @return Le DTO de la police, ou empty si non trouvée
     */
    Optional<AutoPolicyDTO> getPolicyById(UUID id, UUID organizationId);

    /**
     * Récupère une police par son numéro.
     *
     * @param policyNumber   Le numéro de police
     * @param organizationId L'ID de l'organisation
     * @return Le DTO de la police, ou empty si non trouvée
     */
    Optional<AutoPolicyDTO> getPolicyByNumber(String policyNumber, UUID organizationId);

    /**
     * Liste toutes les polices d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des DTOs des polices
     */
    List<AutoPolicyDTO> getAllPolicies(UUID organizationId);

    /**
     * Liste toutes les polices actives d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des DTOs des polices actives
     */
    List<AutoPolicyDTO> getAllActivePolicies(UUID organizationId);

    /**
     * Liste toutes les polices d'un véhicule.
     *
     * @param vehicleId      L'ID du véhicule
     * @param organizationId L'ID de l'organisation
     * @return La liste des DTOs des polices
     */
    List<AutoPolicyDTO> getPoliciesByVehicle(UUID vehicleId, UUID organizationId);

    /**
     * Liste toutes les polices d'un conducteur principal.
     *
     * @param driverId       L'ID du conducteur
     * @param organizationId L'ID de l'organisation
     * @return La liste des DTOs des polices
     */
    List<AutoPolicyDTO> getPoliciesByPrimaryDriver(UUID driverId, UUID organizationId);

    /**
     * Liste toutes les polices qui expirent entre deux dates.
     *
     * @param startDate      La date de début
     * @param endDate        La date de fin
     * @param organizationId L'ID de l'organisation
     * @return La liste des DTOs des polices
     */
    List<AutoPolicyDTO> getPoliciesExpiringBetween(LocalDate startDate, LocalDate endDate, UUID organizationId);

    /**
     * Supprime une police.
     *
     * @param id             L'ID de la police à supprimer
     * @param organizationId L'ID de l'organisation
     * @return true si la police a été supprimée, false sinon
     */
    boolean deletePolicy(UUID id, UUID organizationId);
}
