package com.devolution.saas.core.organization.application.dto;



import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO pour la hiérarchie des organisations.
 */
public record OrganizationHierarchyDTO(
    /**
     * ID de l'organisation.
     */
    UUID id,

    /**
     * Nom de l'organisation.
     */
    String name,

    /**
     * Code de l'organisation.
     */
    String code,

    /**
     * Type de l'organisation.
     */
    String type,

    /**
     * Niveau dans la hiérarchie (0 = racine).
     */
    int level,

    /**
     * Organisations enfants.
     */
    List<OrganizationHierarchyDTO> children
) {
    /**
     * Constructeur par défaut avec des valeurs par défaut pour certains champs.
     */
    public OrganizationHierarchyDTO {
        if (children == null) {
            children = new ArrayList<>();
        }
    }

    /**
     * Builder pour OrganizationHierarchyDTO.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Vérifie si l'organisation a des enfants.
     *
     * @return true si l'organisation a des enfants, false sinon
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    /**
     * Ajoute une organisation enfant.
     *
     * @param child Organisation enfant à ajouter
     * @return Une nouvelle instance avec l'enfant ajouté
     */
    public OrganizationHierarchyDTO addChild(OrganizationHierarchyDTO child) {
        List<OrganizationHierarchyDTO> newChildren = new ArrayList<>(this.children);
        newChildren.add(child);
        return new OrganizationHierarchyDTO(id, name, code, type, level, newChildren);
    }

    /**
     * Classe Builder pour OrganizationHierarchyDTO.
     */
    public static class Builder {
        private UUID id;
        private String name;
        private String code;
        private String type;
        private int level;
        private List<OrganizationHierarchyDTO> children = new ArrayList<>();

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder level(int level) {
            this.level = level;
            return this;
        }

        public Builder children(List<OrganizationHierarchyDTO> children) {
            this.children = children;
            return this;
        }

        public OrganizationHierarchyDTO build() {
            return new OrganizationHierarchyDTO(id, name, code, type, level, children);
        }
    }
}
