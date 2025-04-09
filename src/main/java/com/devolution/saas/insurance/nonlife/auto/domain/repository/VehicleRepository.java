package com.devolution.saas.insurance.nonlife.auto.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des véhicules.
 */
public interface VehicleRepository {

    /**
     * Sauvegarde un véhicule.
     *
     * @param vehicle Le véhicule à sauvegarder
     * @return Le véhicule sauvegardé
     */
    Vehicle save(Vehicle vehicle);

    /**
     * Trouve un véhicule par son ID.
     *
     * @param id L'ID du véhicule
     * @return Le véhicule trouvé, ou empty si non trouvé
     */
    Optional<Vehicle> findById(UUID id);

    /**
     * Trouve un véhicule par son numéro d'immatriculation.
     *
     * @param registrationNumber Le numéro d'immatriculation
     * @param organizationId     L'ID de l'organisation
     * @return Le véhicule trouvé, ou empty si non trouvé
     */
    Optional<Vehicle> findByRegistrationNumber(String registrationNumber, UUID organizationId);

    /**
     * Liste tous les véhicules d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des véhicules
     */
    List<Vehicle> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste tous les véhicules d'un propriétaire.
     *
     * @param ownerId        L'ID du propriétaire
     * @param organizationId L'ID de l'organisation
     * @return La liste des véhicules
     */
    List<Vehicle> findAllByOwnerIdAndOrganizationId(UUID ownerId, UUID organizationId);

    /**
     * Supprime un véhicule.
     *
     * @param id L'ID du véhicule à supprimer
     */
    void deleteById(UUID id);
}
