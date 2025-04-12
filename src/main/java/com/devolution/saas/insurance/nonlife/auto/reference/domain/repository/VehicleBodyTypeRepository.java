package com.devolution.saas.insurance.nonlife.auto.reference.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleBodyType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des types de carrosserie de véhicule.
 */
public interface VehicleBodyTypeRepository {

    /**
     * Sauvegarde un type de carrosserie.
     *
     * @param vehicleBodyType Le type de carrosserie à sauvegarder
     * @return Le type de carrosserie sauvegardé
     */
    VehicleBodyType save(VehicleBodyType vehicleBodyType);

    /**
     * Trouve un type de carrosserie par son ID.
     *
     * @param id L'ID du type de carrosserie
     * @return Le type de carrosserie trouvé, ou empty si non trouvé
     */
    Optional<VehicleBodyType> findById(UUID id);

    /**
     * Trouve un type de carrosserie par son code.
     *
     * @param code           Le code du type de carrosserie
     * @param organizationId L'ID de l'organisation
     * @return Le type de carrosserie trouvé, ou empty si non trouvé
     */
    Optional<VehicleBodyType> findByCodeAndOrganizationId(String code, UUID organizationId);

    /**
     * Liste tous les types de carrosserie d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carrosserie
     */
    List<VehicleBodyType> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste tous les types de carrosserie actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carrosserie actifs
     */
    List<VehicleBodyType> findAllActiveByOrganizationId(UUID organizationId);

    /**
     * Supprime un type de carrosserie.
     *
     * @param id L'ID du type de carrosserie à supprimer
     */
    void deleteById(UUID id);
}
