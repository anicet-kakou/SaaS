package com.devolution.saas.core.organization.application.dto;

import com.devolution.saas.core.organization.domain.model.OrganizationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les types d'organisations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationTypeDTO {

    /**
     * Code du type d'organisation.
     */
    private String code;

    /**
     * Nom du type d'organisation.
     */
    private String name;

    /**
     * Description du type d'organisation.
     */
    private String description;

    /**
     * Constructeur à partir de l'énumération OrganizationType.
     *
     * @param type Type d'organisation
     */
    public OrganizationTypeDTO(OrganizationType type) {
        this.code = type.name();
        this.name = formatName(type.name());
        this.description = generateDescription(type);
    }

    /**
     * Formate le nom du type d'organisation.
     *
     * @param name Nom à formater
     * @return Nom formaté
     */
    private String formatName(String name) {
        return name.replace("_", " ");
    }

    /**
     * Génère une description pour le type d'organisation.
     *
     * @param type Type d'organisation
     * @return Description
     */
    private String generateDescription(OrganizationType type) {
        switch (type) {
            case INSURANCE_COMPANY:
                return "Compagnie d'assurance qui souscrit des polices d'assurance et assume les risques.";
            case BROKER:
                return "Intermédiaire qui représente le client et négocie avec plusieurs compagnies d'assurance.";
            case AGENT:
                return "Représentant d'une compagnie d'assurance spécifique.";
            case REINSURER:
                return "Compagnie qui assure d'autres compagnies d'assurance.";
            case CLAIMS_MANAGER:
                return "Entité spécialisée dans la gestion des sinistres.";
            case RISK_MANAGER:
                return "Entité spécialisée dans l'évaluation et la gestion des risques.";
            case BANCASSURANCE:
                return "Institution financière qui offre des produits d'assurance.";
            case MUTUAL:
                return "Organisation à but non lucratif détenue par ses membres.";
            case UNDERWRITING_AGENCY:
                return "Entité qui souscrit des polices au nom d'une compagnie d'assurance.";
            case OTHER:
                return "Autre type d'organisation non spécifié.";
            default:
                return "";
        }
    }
}
