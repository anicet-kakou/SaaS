package com.devolution.saas.core.organization.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO pour la hiérarchie des organisations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationHierarchyDTO {

    /**
     * ID de l'organisation.
     */
    private UUID id;

    /**
     * Nom de l'organisation.
     */
    private String name;

    /**
     * Code de l'organisation.
     */
    private String code;

    /**
     * Type de l'organisation.
     */
    private String type;

    /**
     * Niveau dans la hiérarchie (0 = racine).
     */
    private int level;

    /**
     * Organisations enfants.
     */
    @Builder.Default
    private List<OrganizationHierarchyDTO> children = new ArrayList<>();

    /**
     * Ajoute une organisation enfant.
     *
     * @param child Organisation enfant à ajouter
     */
    public void addChild(OrganizationHierarchyDTO child) {
        this.children.add(child);
    }

    /**
     * Vérifie si l'organisation a des enfants.
     *
     * @return true si l'organisation a des enfants, false sinon
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
