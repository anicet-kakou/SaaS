package com.devolution.saas.insurance.nonlife.auto.api.controller;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.insurance.nonlife.auto.api.request.CreateAutoInsuranceProductRequest;
import com.devolution.saas.insurance.nonlife.auto.application.dto.AutoInsuranceProductDTO;
import com.devolution.saas.insurance.nonlife.auto.application.service.AutoInsuranceProductService;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoInsuranceProduct;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des produits d'assurance automobile.
 */
@RestController
@RequestMapping("/api/v1/auto/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auto Products", description = "API pour la gestion des produits d'assurance automobile")
public class AutoProductController {

    private final AutoInsuranceProductService autoInsuranceProductService;

    /**
     * Crée un nouveau produit d'assurance auto.
     *
     * @param request        La requête de création
     * @param organizationId L'ID de l'organisation
     * @return Le produit créé
     */
    @PostMapping
    @Operation(summary = "Crée un nouveau produit d'assurance auto")
    @Auditable(action = "API_CREATE_AUTO_PRODUCT")
    @TenantRequired
    public ResponseEntity<AutoInsuranceProductDTO> createProduct(
            @Valid @RequestBody CreateAutoInsuranceProductRequest request,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour créer un nouveau produit d'assurance auto pour l'organisation: {}", organizationId);

        AutoInsuranceProduct product = mapRequestToEntity(request);
        AutoInsuranceProductDTO createdProduct = autoInsuranceProductService.createProduct(product, organizationId);

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * Récupère un produit par son ID.
     *
     * @param id             L'ID du produit
     * @param organizationId L'ID de l'organisation
     * @return Le produit trouvé
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupère un produit par son ID")
    @Auditable(action = "API_GET_AUTO_PRODUCT")
    @TenantRequired
    public ResponseEntity<AutoInsuranceProductDTO> getProductById(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le produit d'assurance auto avec ID: {} pour l'organisation: {}",
                id, organizationId);

        AutoInsuranceProductDTO product = autoInsuranceProductService.getProductById(id, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException
                        .forId("Produit d'assurance auto", id));

        return ResponseEntity.ok(product);
    }

    /**
     * Récupère un produit par son code.
     *
     * @param code           Le code du produit
     * @param organizationId L'ID de l'organisation
     * @return Le produit trouvé
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Récupère un produit par son code")
    @Auditable(action = "API_GET_AUTO_PRODUCT_BY_CODE")
    @TenantRequired
    public ResponseEntity<AutoInsuranceProductDTO> getProductByCode(
            @PathVariable String code,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le produit d'assurance auto avec code: {} pour l'organisation: {}",
                code, organizationId);

        AutoInsuranceProductDTO product = autoInsuranceProductService.getProductByCode(code, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException
                        .forCode("Produit d'assurance auto", code));

        return ResponseEntity.ok(product);
    }

    /**
     * Liste tous les produits d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des produits
     */
    @GetMapping
    @Operation(summary = "Liste tous les produits d'une organisation")
    @Auditable(action = "API_LIST_AUTO_PRODUCTS")
    @TenantRequired
    public ResponseEntity<List<AutoInsuranceProductDTO>> getAllProducts(
            @RequestParam UUID organizationId) {
        log.debug("REST request pour lister tous les produits d'assurance auto pour l'organisation: {}", organizationId);

        List<AutoInsuranceProductDTO> products = autoInsuranceProductService.getAllProducts(organizationId);
        return ResponseEntity.ok(products);
    }

    /**
     * Liste tous les produits actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des produits actifs
     */
    @GetMapping("/active")
    @Operation(summary = "Liste tous les produits actifs d'une organisation")
    @Auditable(action = "API_LIST_ACTIVE_AUTO_PRODUCTS")
    @TenantRequired
    public ResponseEntity<List<AutoInsuranceProductDTO>> getAllActiveProducts(
            @RequestParam UUID organizationId) {
        log.debug("REST request pour lister tous les produits d'assurance auto actifs pour l'organisation: {}",
                organizationId);

        List<AutoInsuranceProductDTO> products = autoInsuranceProductService.getAllActiveProducts(organizationId);
        return ResponseEntity.ok(products);
    }

    /**
     * Liste tous les produits actifs à une date donnée.
     *
     * @param date           La date à laquelle les produits doivent être actifs
     * @param organizationId L'ID de l'organisation
     * @return La liste des produits actifs à la date donnée
     */
    @GetMapping("/active-at-date")
    @Operation(summary = "Liste tous les produits actifs à une date donnée")
    @Auditable(action = "API_LIST_AUTO_PRODUCTS_AT_DATE")
    @TenantRequired
    public ResponseEntity<List<AutoInsuranceProductDTO>> getAllActiveProductsAtDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour lister tous les produits d'assurance auto actifs à la date: {} pour l'organisation: {}",
                date, organizationId);

        List<AutoInsuranceProductDTO> products = autoInsuranceProductService.getAllActiveProductsAtDate(date, organizationId);
        return ResponseEntity.ok(products);
    }

    /**
     * Met à jour un produit.
     *
     * @param id             L'ID du produit
     * @param request        La requête de mise à jour
     * @param organizationId L'ID de l'organisation
     * @return Le produit mis à jour
     */
    @PutMapping("/{id}")
    @Operation(summary = "Met à jour un produit")
    @Auditable(action = "API_UPDATE_AUTO_PRODUCT")
    @TenantRequired
    public ResponseEntity<AutoInsuranceProductDTO> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody CreateAutoInsuranceProductRequest request,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour mettre à jour le produit d'assurance auto avec ID: {} pour l'organisation: {}",
                id, organizationId);

        AutoInsuranceProduct product = mapRequestToEntity(request);
        AutoInsuranceProductDTO updatedProduct = autoInsuranceProductService.updateProduct(id, product, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException
                        .forId("Produit d'assurance auto", id));

        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Supprime un produit.
     *
     * @param id             L'ID du produit
     * @param organizationId L'ID de l'organisation
     * @return Réponse vide avec statut 204 No Content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime un produit")
    @Auditable(action = "API_DELETE_AUTO_PRODUCT")
    @TenantRequired
    public ResponseEntity<Void> deleteProduct(
            @PathVariable UUID id,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour supprimer le produit d'assurance auto avec ID: {} pour l'organisation: {}",
                id, organizationId);

        boolean deleted = autoInsuranceProductService.deleteProduct(id, organizationId);
        if (!deleted) {
            throw com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException
                    .forId("Produit d'assurance auto", id);
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * Convertit une requête en entité.
     *
     * @param request La requête à convertir
     * @return L'entité correspondante
     */
    private AutoInsuranceProduct mapRequestToEntity(CreateAutoInsuranceProductRequest request) {
        return AutoInsuranceProduct.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus())
                .effectiveDate(request.getEffectiveDate())
                .expiryDate(request.getExpiryDate())
                .build();
    }
}
