package com.devolution.saas.insurance.nonlife.auto.reference.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleMake;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des marques de véhicule.
 */
public interface VehicleMakeRepository {

    /**
     * Sauvegarde une marque de véhicule.
     *
     * @param vehicleMake La marque de véhicule à sauvegarder
     * @return La marque de véhicule sauvegardée
     */
    VehicleMake save(VehicleMake vehicleMake);

    /**
     * Trouve une marque de véhicule par son ID.
     *
     * @param id L'ID de la marque de véhicule
     * @return La marque de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleMake> findById(UUID id);

    /**
     * Trouve une marque de véhicule par son code.
     *
     * @param code           Le code de la marque de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La marque de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleMake> findByCodeAndOrganizationId(String code, UUID organizationId);

    /**
     * Liste toutes les marques de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des marques de véhicule
     */
    List<VehicleMake> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste toutes les marques de véhicule actives d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des marques de véhicule actives
     */
    List<VehicleMake> findAllActiveByOrganizationId(UUID organizationId);

    /**
     * Supprime une marque de véhicule.
     *
     * @param id L'ID de la marque de véhicule à supprimer
     */
    void deleteById(UUID id);
}
