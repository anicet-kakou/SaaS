package com.devolution.saas.insurance.nonlife.auto.application.service;

import com.devolution.saas.insurance.nonlife.auto.application.dto.DriverDTO;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service d'application pour la gestion des conducteurs.
 */
public interface DriverService {

    /**
     * Crée un nouveau conducteur.
     *
     * @param driver         Le conducteur à créer
     * @param organizationId L'ID de l'organisation
     * @return Le DTO du conducteur créé
     */
    DriverDTO createDriver(Driver driver, UUID organizationId);

    /**
     * Met à jour un conducteur existant.
     *
     * @param id             L'ID du conducteur à mettre à jour
     * @param driver         Le conducteur avec les nouvelles valeurs
     * @param organizationId L'ID de l'organisation
     * @return Le DTO du conducteur mis à jour, ou empty si non trouvé
     */
    Optional<DriverDTO> updateDriver(UUID id, Driver driver, UUID organizationId);

    /**
     * Récupère un conducteur par son ID.
     *
     * @param id             L'ID du conducteur
     * @param organizationId L'ID de l'organisation
     * @return Le DTO du conducteur, ou empty si non trouvé
     */
    Optional<DriverDTO> getDriverById(UUID id, UUID organizationId);

    /**
     * Récupère un conducteur par son numéro de permis.
     *
     * @param licenseNumber  Le numéro de permis
     * @param organizationId L'ID de l'organisation
     * @return Le DTO du conducteur, ou empty si non trouvé
     */
    Optional<DriverDTO> getDriverByLicenseNumber(String licenseNumber, UUID organizationId);

    /**
     * Liste tous les conducteurs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des DTOs des conducteurs
     */
    List<DriverDTO> getAllDrivers(UUID organizationId);

    /**
     * Liste tous les conducteurs d'un client.
     *
     * @param customerId     L'ID du client
     * @param organizationId L'ID de l'organisation
     * @return La liste des DTOs des conducteurs
     */
    List<DriverDTO> getDriversByCustomer(UUID customerId, UUID organizationId);

    /**
     * Récupère le conducteur principal d'un client.
     *
     * @param customerId     L'ID du client
     * @param organizationId L'ID de l'organisation
     * @return Le DTO du conducteur principal, ou empty si non trouvé
     */
    Optional<DriverDTO> getPrimaryDriverByCustomer(UUID customerId, UUID organizationId);

    /**
     * Supprime un conducteur.
     *
     * @param id             L'ID du conducteur à supprimer
     * @param organizationId L'ID de l'organisation
     * @return true si le conducteur a été supprimé, false sinon
     */
    boolean deleteDriver(UUID id, UUID organizationId);
}
