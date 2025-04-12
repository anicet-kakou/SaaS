package com.devolution.saas.insurance.nonlife.auto.application.service;

import com.devolution.saas.insurance.nonlife.auto.application.dto.AutoInsuranceProductDTO;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoInsuranceProduct;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service d'application pour la gestion des produits d'assurance auto.
 */
public interface AutoInsuranceProductService {

    /**
     * Crée un nouveau produit d'assurance auto.
     *
     * @param product        Le produit à créer
     * @param organizationId L'ID de l'organisation
     * @return Le DTO du produit créé
     */
    AutoInsuranceProductDTO createProduct(AutoInsuranceProduct product, UUID organizationId);

    /**
     * Met à jour un produit d'assurance auto existant.
     *
     * @param id             L'ID du produit à mettre à jour
     * @param product        Le produit avec les nouvelles valeurs
     * @param organizationId L'ID de l'organisation
     * @return Le DTO du produit mis à jour, ou empty si non trouvé
     */
    Optional<AutoInsuranceProductDTO> updateProduct(UUID id, AutoInsuranceProduct product, UUID organizationId);

    /**
     * Récupère un produit par son ID.
     *
     * @param id             L'ID du produit
     * @param organizationId L'ID de l'organisation
     * @return Le DTO du produit, ou empty si non trouvé
     */
    Optional<AutoInsuranceProductDTO> getProductById(UUID id, UUID organizationId);

    /**
     * Récupère un produit par son code.
     *
     * @param code           Le code du produit
     * @param organizationId L'ID de l'organisation
     * @return Le DTO du produit, ou empty si non trouvé
     */
    Optional<AutoInsuranceProductDTO> getProductByCode(String code, UUID organizationId);

    /**
     * Liste tous les produits d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des DTOs des produits
     */
    List<AutoInsuranceProductDTO> getAllProducts(UUID organizationId);

    /**
     * Liste tous les produits actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des DTOs des produits actifs
     */
    List<AutoInsuranceProductDTO> getAllActiveProducts(UUID organizationId);

    /**
     * Liste tous les produits actifs à une date donnée.
     *
     * @param date           La date à laquelle les produits doivent être actifs
     * @param organizationId L'ID de l'organisation
     * @return La liste des DTOs des produits actifs à la date donnée
     */
    List<AutoInsuranceProductDTO> getAllActiveProductsAtDate(LocalDate date, UUID organizationId);

    /**
     * Supprime un produit.
     *
     * @param id             L'ID du produit à supprimer
     * @param organizationId L'ID de l'organisation
     * @return true si le produit a été supprimé, false sinon
     */
    boolean deleteProduct(UUID id, UUID organizationId);
}
