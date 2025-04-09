package com.devolution.saas.insurance.nonlife.auto.application.service;

import com.devolution.saas.insurance.nonlife.auto.application.command.CreateVehicleCommand;
import com.devolution.saas.insurance.nonlife.auto.application.dto.VehicleDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service d'application pour la gestion des véhicules.
 */
public interface VehicleService {

    /**
     * Crée un nouveau véhicule.
     *
     * @param command La commande de création
     * @return Le DTO du véhicule créé
     */
    VehicleDTO createVehicle(CreateVehicleCommand command);

    /**
     * Récupère un véhicule par son ID.
     *
     * @param id             L'ID du véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le DTO du véhicule
     */
    VehicleDTO getVehicleById(UUID id, UUID organizationId);

    /**
     * Récupère un véhicule par son numéro d'immatriculation.
     *
     * @param registrationNumber Le numéro d'immatriculation
     * @param organizationId     L'ID de l'organisation
     * @return Le DTO du véhicule
     */
    VehicleDTO getVehicleByRegistrationNumber(String registrationNumber, UUID organizationId);

    /**
     * Liste tous les véhicules d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des DTOs des véhicules
     */
    List<VehicleDTO> getAllVehicles(UUID organizationId);

    /**
     * Liste tous les véhicules d'un propriétaire.
     *
     * @param ownerId        L'ID du propriétaire
     * @param organizationId L'ID de l'organisation
     * @return La liste des DTOs des véhicules
     */
    List<VehicleDTO> getVehiclesByOwner(UUID ownerId, UUID organizationId);

    /**
     * Met à jour un véhicule.
     *
     * @param id      L'ID du véhicule
     * @param command La commande de mise à jour
     * @return Le DTO du véhicule mis à jour
     */
    VehicleDTO updateVehicle(UUID id, CreateVehicleCommand command);

    /**
     * Supprime un véhicule.
     *
     * @param id             L'ID du véhicule
     * @param organizationId L'ID de l'organisation
     */
    void deleteVehicle(UUID id, UUID organizationId);
}
