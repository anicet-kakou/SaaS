package com.devolution.saas.insurance.nonlife.auto.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des conducteurs.
 */
public interface DriverRepository {

    /**
     * Sauvegarde un conducteur.
     *
     * @param driver Le conducteur à sauvegarder
     * @return Le conducteur sauvegardé
     */
    Driver save(Driver driver);

    /**
     * Trouve un conducteur par son ID.
     *
     * @param id L'ID du conducteur
     * @return Le conducteur trouvé, ou empty si non trouvé
     */
    Optional<Driver> findById(UUID id);

    /**
     * Trouve un conducteur par son numéro de permis.
     *
     * @param licenseNumber  Le numéro de permis
     * @param organizationId L'ID de l'organisation
     * @return Le conducteur trouvé, ou empty si non trouvé
     */
    Optional<Driver> findByLicenseNumberAndOrganizationId(String licenseNumber, UUID organizationId);

    /**
     * Liste tous les conducteurs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des conducteurs
     */
    List<Driver> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste tous les conducteurs d'un client.
     *
     * @param customerId     L'ID du client
     * @param organizationId L'ID de l'organisation
     * @return La liste des conducteurs
     */
    List<Driver> findAllByCustomerIdAndOrganizationId(UUID customerId, UUID organizationId);

    /**
     * Trouve le conducteur principal d'un client.
     *
     * @param customerId     L'ID du client
     * @param organizationId L'ID de l'organisation
     * @return Le conducteur principal trouvé, ou empty si non trouvé
     */
    Optional<Driver> findByCustomerIdAndIsPrimaryDriverTrueAndOrganizationId(UUID customerId, UUID organizationId);

    /**
     * Supprime un conducteur.
     *
     * @param id L'ID du conducteur à supprimer
     */
    void deleteById(UUID id);
}
