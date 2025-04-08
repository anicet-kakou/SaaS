package com.devolution.saas.core.organization.domain.model;

/**
 * Énumération des types d'organisations dans le système.
 */
public enum OrganizationType {
    /**
     * Compagnie d'assurance - Entité qui souscrit des polices d'assurance et assume les risques.
     */
    INSURANCE_COMPANY,

    /**
     * Courtier - Intermédiaire qui représente le client et négocie avec plusieurs compagnies d'assurance.
     */
    BROKER,

    /**
     * Agent - Représentant d'une compagnie d'assurance spécifique.
     */
    AGENT,

    /**
     * Réassureur - Compagnie qui assure d'autres compagnies d'assurance.
     */
    REINSURER,

    /**
     * Gestionnaire de sinistres - Entité spécialisée dans la gestion des sinistres.
     */
    CLAIMS_MANAGER,

    /**
     * Gestionnaire de risques - Entité spécialisée dans l'évaluation et la gestion des risques.
     */
    RISK_MANAGER,

    /**
     * Banque-assurance - Institution financière qui offre des produits d'assurance.
     */
    BANCASSURANCE,

    /**
     * Mutuelle - Organisation à but non lucratif détenue par ses membres.
     */
    MUTUAL,

    /**
     * Agence de souscription - Entité qui souscrit des polices au nom d'une compagnie d'assurance.
     */
    UNDERWRITING_AGENCY,

    /**
     * Autre - Autre type d'organisation non spécifié.
     */
    OTHER
}
