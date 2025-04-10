package com.devolution.saas.insurance.nonlife.auto.reference.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleColor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des couleurs de véhicule.
 */
public interface VehicleColorRepository {

    /**
     * Sauvegarde une couleur de véhicule.
     *
     * @param vehicleColor La couleur de véhicule à sauvegarder
     * @return La couleur de véhicule sauvegardée
     */
    VehicleColor save(VehicleColor vehicleColor);

    /**
     * Trouve une couleur de véhicule par son ID.
     *
     * @param id L'ID de la couleur de véhicule
     * @return La couleur de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleColor> findById(UUID id);

    /**
     * Trouve une couleur de véhicule par son code.
     *
     * @param code           Le code de la couleur de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La couleur de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleColor> findByCodeAndOrganizationId(String code, UUID organizationId);

    /**
     * Liste toutes les couleurs de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des couleurs de véhicule
     */
    List<VehicleColor> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste toutes les couleurs de véhicule actives d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des couleurs de véhicule actives
     */
    List<VehicleColor> findAllActiveByOrganizationId(UUID organizationId);

    /**
     * Supprime une couleur de véhicule.
     *
     * @param id L'ID de la couleur de véhicule à supprimer
     */
    void deleteById(UUID id);
}
