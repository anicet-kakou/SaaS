package com.devolution.saas.insurance.nonlife.auto.api.request;

import com.devolution.saas.insurance.common.domain.model.InsuranceProduct.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Requête pour la création d'un produit d'assurance auto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAutoInsuranceProductRequest {

    /**
     * Code unique du produit.
     */
    @NotBlank(message = "Le code est obligatoire")
    @Size(min = 2, max = 20, message = "Le code doit contenir entre 2 et 20 caractères")
    private String code;

    /**
     * Nom du produit.
     */
    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String name;

    /**
     * Description du produit.
     */
    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;

    /**
     * Statut du produit.
     */
    @NotNull(message = "Le statut est obligatoire")
    private ProductStatus status;

    /**
     * Date d'effet du produit.
     */
    @NotNull(message = "La date d'effet est obligatoire")
    private LocalDate effectiveDate;

    /**
     * Date d'expiration du produit.
     */
    private LocalDate expiryDate;
}
