package com.devolution.saas.insurance.nonlife.auto.domain.port;

import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;

import java.util.Optional;
import java.util.UUID;

/**
 * Port pour accéder aux véhicules depuis le domaine.
 * Cette interface respecte le principe d'inversion de dépendance
 * en permettant au domaine de définir ses besoins sans dépendre
 * de l'implémentation.
 */
public interface VehicleProvider {

    /**
     * Trouve un véhicule par son ID.
     *
     * @param id             L'ID du véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le véhicule trouvé, ou empty si non trouvé
     */
    Optional<Vehicle> findVehicleById(UUID id, UUID organizationId);

    /**
     * Vérifie si un véhicule existe.
     *
     * @param id             L'ID du véhicule
     * @param organizationId L'ID de l'organisation
     * @return true si le véhicule existe, false sinon
     */
    boolean vehicleExists(UUID id, UUID organizationId);
}
