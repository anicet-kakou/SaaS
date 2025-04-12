package com.devolution.saas.insurance.nonlife.auto.api.controller;

import com.devolution.saas.insurance.nonlife.auto.api.request.CreateAutoInsuranceProductRequest;
import com.devolution.saas.insurance.nonlife.auto.application.dto.AutoInsuranceProductDTO;
import com.devolution.saas.insurance.nonlife.auto.application.service.AutoInsuranceProductService;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoInsuranceProduct;
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
 * Contrôleur REST pour la gestion des produits d'assurance auto.
 *
 * @deprecated Utiliser {@link AutoProductController} à la place.
 * Ce contrôleur sera supprimé dans une version future.
 */
@Deprecated(since = "1.0.0", forRemoval = true)
@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/auto-insurance-products")
@RequiredArgsConstructor
@Slf4j
public class AutoInsuranceProductController {

    private final AutoInsuranceProductService autoInsuranceProductService;

    /**
     * Crée un nouveau produit d'assurance auto.
     *
     * @param organizationId L'ID de l'organisation
     * @param request        La requête de création
     * @return Le produit créé
     */
    @PostMapping
    public ResponseEntity<AutoInsuranceProductDTO> createProduct(
            @PathVariable UUID organizationId,
            @Valid @RequestBody CreateAutoInsuranceProductRequest request) {
        log.info("Création d'un nouveau produit d'assurance auto pour l'organisation: {}", organizationId);

        AutoInsuranceProduct product = mapRequestToEntity(request);
        AutoInsuranceProductDTO createdProduct = autoInsuranceProductService.createProduct(product, organizationId);

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * Récupère un produit par son ID.
     *
     * @param organizationId L'ID de l'organisation
     * @param id             L'ID du produit
     * @return Le produit trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<AutoInsuranceProductDTO> getProductById(
            @PathVariable UUID organizationId,
            @PathVariable UUID id) {
        log.info("Récupération du produit d'assurance auto avec ID: {} pour l'organisation: {}", id, organizationId);

        AutoInsuranceProductDTO product = autoInsuranceProductService.getProductById(id, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException.forId("Produit d'assurance auto", id));

        return ResponseEntity.ok(product);
    }

    /**
     * Récupère un produit par son code.
     *
     * @param organizationId L'ID de l'organisation
     * @param code           Le code du produit
     * @return Le produit trouvé
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<AutoInsuranceProductDTO> getProductByCode(
            @PathVariable UUID organizationId,
            @PathVariable String code) {
        log.info("Récupération du produit d'assurance auto avec code: {} pour l'organisation: {}", code, organizationId);

        AutoInsuranceProductDTO product = autoInsuranceProductService.getProductByCode(code, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException.forCode("Produit d'assurance auto", code));

        return ResponseEntity.ok(product);
    }

    /**
     * Liste tous les produits d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des produits
     */
    @GetMapping
    public ResponseEntity<List<AutoInsuranceProductDTO>> getAllProducts(
            @PathVariable UUID organizationId) {
        log.info("Listage de tous les produits d'assurance auto pour l'organisation: {}", organizationId);

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
    public ResponseEntity<List<AutoInsuranceProductDTO>> getAllActiveProducts(
            @PathVariable UUID organizationId) {
        log.info("Listage de tous les produits d'assurance auto actifs pour l'organisation: {}", organizationId);

        List<AutoInsuranceProductDTO> products = autoInsuranceProductService.getAllActiveProducts(organizationId);
        return ResponseEntity.ok(products);
    }

    /**
     * Liste tous les produits actifs à une date donnée.
     *
     * @param organizationId L'ID de l'organisation
     * @param date           La date à laquelle les produits doivent être actifs
     * @return La liste des produits actifs à la date donnée
     */
    @GetMapping("/active-at-date")
    public ResponseEntity<List<AutoInsuranceProductDTO>> getAllActiveProductsAtDate(
            @PathVariable UUID organizationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Listage de tous les produits d'assurance auto actifs à la date: {} pour l'organisation: {}", date, organizationId);

        List<AutoInsuranceProductDTO> products = autoInsuranceProductService.getAllActiveProductsAtDate(date, organizationId);
        return ResponseEntity.ok(products);
    }

    /**
     * Met à jour un produit.
     *
     * @param organizationId L'ID de l'organisation
     * @param id             L'ID du produit
     * @param request        La requête de mise à jour
     * @return Le produit mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<AutoInsuranceProductDTO> updateProduct(
            @PathVariable UUID organizationId,
            @PathVariable UUID id,
            @Valid @RequestBody CreateAutoInsuranceProductRequest request) {
        log.info("Mise à jour du produit d'assurance auto avec ID: {} pour l'organisation: {}", id, organizationId);

        AutoInsuranceProduct product = mapRequestToEntity(request);
        AutoInsuranceProductDTO updatedProduct = autoInsuranceProductService.updateProduct(id, product, organizationId)
                .orElseThrow(() -> com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException.forId("Produit d'assurance auto", id));

        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Supprime un produit.
     *
     * @param organizationId L'ID de l'organisation
     * @param id             L'ID du produit
     * @return Réponse vide avec statut 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable UUID organizationId,
            @PathVariable UUID id) {
        log.info("Suppression du produit d'assurance auto avec ID: {} pour l'organisation: {}", id, organizationId);

        boolean deleted = autoInsuranceProductService.deleteProduct(id, organizationId);
        if (!deleted) {
            throw com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException.forId("Produit d'assurance auto", id);
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
