package com.devolution.saas.insurance.nonlife.auto.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoInsuranceProduct;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des produits d'assurance auto.
 */
public interface AutoInsuranceProductRepository {

    /**
     * Sauvegarde un produit d'assurance auto.
     *
     * @param product Le produit à sauvegarder
     * @return Le produit sauvegardé
     */
    AutoInsuranceProduct save(AutoInsuranceProduct product);

    /**
     * Trouve un produit par son ID.
     *
     * @param id L'ID du produit
     * @return Le produit trouvé, ou empty si non trouvé
     */
    Optional<AutoInsuranceProduct> findById(UUID id);

    /**
     * Trouve un produit par son code.
     *
     * @param code           Le code du produit
     * @param organizationId L'ID de l'organisation
     * @return Le produit trouvé, ou empty si non trouvé
     */
    Optional<AutoInsuranceProduct> findByCodeAndOrganizationId(String code, UUID organizationId);

    /**
     * Liste tous les produits d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des produits
     */
    List<AutoInsuranceProduct> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste tous les produits actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des produits actifs
     */
    List<AutoInsuranceProduct> findAllActiveByOrganizationId(UUID organizationId);

    /**
     * Liste tous les produits actifs à une date donnée.
     *
     * @param date           La date à laquelle les produits doivent être actifs
     * @param organizationId L'ID de l'organisation
     * @return La liste des produits actifs à la date donnée
     */
    List<AutoInsuranceProduct> findAllActiveAtDateByOrganizationId(LocalDate date, UUID organizationId);

    /**
     * Supprime un produit.
     *
     * @param id L'ID du produit à supprimer
     */
    void deleteById(UUID id);
}
