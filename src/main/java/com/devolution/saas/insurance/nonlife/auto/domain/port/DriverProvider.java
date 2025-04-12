package com.devolution.saas.insurance.nonlife.auto.domain.port;

import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;

import java.util.Optional;
import java.util.UUID;

/**
 * Port pour accéder aux conducteurs depuis le domaine.
 * Cette interface respecte le principe d'inversion de dépendance
 * en permettant au domaine de définir ses besoins sans dépendre
 * de l'implémentation.
 */
public interface DriverProvider {

    /**
     * Trouve un conducteur par son ID.
     *
     * @param id             L'ID du conducteur
     * @param organizationId L'ID de l'organisation
     * @return Le conducteur trouvé, ou empty si non trouvé
     */
    Optional<Driver> findDriverById(UUID id, UUID organizationId);

    /**
     * Vérifie si un conducteur existe.
     *
     * @param id             L'ID du conducteur
     * @param organizationId L'ID de l'organisation
     * @return true si le conducteur existe, false sinon
     */
    boolean driverExists(UUID id, UUID organizationId);

    /**
     * Vérifie si un conducteur avec le numéro de permis spécifié existe.
     *
     * @param licenseNumber  Le numéro de permis
     * @param organizationId L'ID de l'organisation
     * @return true si un conducteur avec ce numéro de permis existe, false sinon
     */
    boolean licenseNumberExists(String licenseNumber, UUID organizationId);
}
