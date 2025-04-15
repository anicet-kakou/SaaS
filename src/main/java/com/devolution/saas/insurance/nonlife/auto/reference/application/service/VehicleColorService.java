package com.devolution.saas.insurance.nonlife.auto.reference.application.service;

import com.devolution.saas.common.abstracts.TenantAwareCrudService;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleColorDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleColor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des couleurs de véhicule.
 */
public interface VehicleColorService extends TenantAwareCrudService<VehicleColorDTO, VehicleColor> {

    /**
     * Crée une nouvelle couleur de véhicule.
     *
     * @param vehicleColor   La couleur de véhicule à créer
     * @param organizationId L'ID de l'organisation
     * @return La couleur de véhicule créée
     */
    VehicleColorDTO createVehicleColor(VehicleColor vehicleColor, UUID organizationId);

    /**
     * Met à jour une couleur de véhicule.
     *
     * @param id             L'ID de la couleur de véhicule
     * @param vehicleColor   La couleur de véhicule mise à jour
     * @param organizationId L'ID de l'organisation
     * @return La couleur de véhicule mise à jour, ou empty si non trouvée
     */
    Optional<VehicleColorDTO> updateVehicleColor(UUID id, VehicleColor vehicleColor, UUID organizationId);

    /**
     * Récupère une couleur de véhicule par son ID.
     *
     * @param id             L'ID de la couleur de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La couleur de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleColorDTO> getVehicleColorById(UUID id, UUID organizationId);

    /**
     * Récupère une couleur de véhicule par son code.
     *
     * @param code           Le code de la couleur de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La couleur de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleColorDTO> getVehicleColorByCode(String code, UUID organizationId);

    /**
     * Liste toutes les couleurs de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des couleurs de véhicule
     */
    List<VehicleColorDTO> getAllVehicleColors(UUID organizationId);

    /**
     * Liste toutes les couleurs de véhicule actives d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des couleurs de véhicule actives
     */
    List<VehicleColorDTO> getAllActiveVehicleColors(UUID organizationId);

    /**
     * Supprime une couleur de véhicule.
     *
     * @param id             L'ID de la couleur de véhicule
     * @param organizationId L'ID de l'organisation
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteVehicleColor(UUID id, UUID organizationId);
}
