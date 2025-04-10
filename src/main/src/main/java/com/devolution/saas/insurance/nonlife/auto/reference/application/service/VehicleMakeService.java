package com.devolution.saas.insurance.nonlife.auto.reference.application.service;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleMakeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleMake;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des marques de véhicule.
 */
public interface VehicleMakeService {

    /**
     * Crée une nouvelle marque de véhicule.
     *
     * @param vehicleMake    La marque de véhicule à créer
     * @param organizationId L'ID de l'organisation
     * @return La marque de véhicule créée
     */
    VehicleMakeDTO createVehicleMake(VehicleMake vehicleMake, UUID organizationId);

    /**
     * Met à jour une marque de véhicule.
     *
     * @param id             L'ID de la marque de véhicule
     * @param vehicleMake    La marque de véhicule mise à jour
     * @param organizationId L'ID de l'organisation
     * @return La marque de véhicule mise à jour, ou empty si non trouvée
     */
    Optional<VehicleMakeDTO> updateVehicleMake(UUID id, VehicleMake vehicleMake, UUID organizationId);

    /**
     * Récupère une marque de véhicule par son ID.
     *
     * @param id             L'ID de la marque de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La marque de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleMakeDTO> getVehicleMakeById(UUID id, UUID organizationId);

    /**
     * Récupère une marque de véhicule par son code.
     *
     * @param code           Le code de la marque de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La marque de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleMakeDTO> getVehicleMakeByCode(String code, UUID organizationId);

    /**
     * Liste toutes les marques de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des marques de véhicule
     */
    List<VehicleMakeDTO> getAllVehicleMakes(UUID organizationId);

    /**
     * Liste toutes les marques de véhicule actives d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des marques de véhicule actives
     */
    List<VehicleMakeDTO> getAllActiveVehicleMakes(UUID organizationId);

    /**
     * Supprime une marque de véhicule.
     *
     * @param id             L'ID de la marque de véhicule
     * @param organizationId L'ID de l'organisation
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteVehicleMake(UUID id, UUID organizationId);
}
