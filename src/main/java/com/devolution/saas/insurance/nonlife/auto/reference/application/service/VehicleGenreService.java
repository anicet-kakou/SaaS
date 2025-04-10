package com.devolution.saas.insurance.nonlife.auto.reference.application.service;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleGenreDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleGenre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des genres de véhicule.
 */
public interface VehicleGenreService {

    /**
     * Crée un nouveau genre de véhicule.
     *
     * @param vehicleGenre   Le genre de véhicule à créer
     * @param organizationId L'ID de l'organisation
     * @return Le genre de véhicule créé
     */
    VehicleGenreDTO createVehicleGenre(VehicleGenre vehicleGenre, UUID organizationId);

    /**
     * Met à jour un genre de véhicule.
     *
     * @param id             L'ID du genre de véhicule
     * @param vehicleGenre   Le genre de véhicule mis à jour
     * @param organizationId L'ID de l'organisation
     * @return Le genre de véhicule mis à jour, ou empty si non trouvé
     */
    Optional<VehicleGenreDTO> updateVehicleGenre(UUID id, VehicleGenre vehicleGenre, UUID organizationId);

    /**
     * Récupère un genre de véhicule par son ID.
     *
     * @param id             L'ID du genre de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le genre de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleGenreDTO> getVehicleGenreById(UUID id, UUID organizationId);

    /**
     * Récupère un genre de véhicule par son code.
     *
     * @param code           Le code du genre de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le genre de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleGenreDTO> getVehicleGenreByCode(String code, UUID organizationId);

    /**
     * Liste tous les genres de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des genres de véhicule
     */
    List<VehicleGenreDTO> getAllVehicleGenres(UUID organizationId);

    /**
     * Liste tous les genres de véhicule actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des genres de véhicule actifs
     */
    List<VehicleGenreDTO> getAllActiveVehicleGenres(UUID organizationId);

    /**
     * Supprime un genre de véhicule.
     *
     * @param id             L'ID du genre de véhicule
     * @param organizationId L'ID de l'organisation
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteVehicleGenre(UUID id, UUID organizationId);
}
