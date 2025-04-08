package com.devolution.saas.core.organization.domain.model;

import com.devolution.saas.common.domain.model.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant une organisation dans le système.
 * Une organisation peut être une compagnie d'assurance, un courtier, un agent, etc.
 */
@Entity
@Table(name = "organizations")
@Getter
@Setter
public class Organization extends AuditableEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Nom de l'organisation.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Code unique de l'organisation.
     */
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    /**
     * Type d'organisation (INSURANCE_COMPANY, BROKER, AGENT, etc.).
     */
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrganizationType type;

    /**
     * Statut de l'organisation (ACTIVE, INACTIVE, SUSPENDED, etc.).
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrganizationStatus status = OrganizationStatus.ACTIVE;

    /**
     * Organisation parente dans la hiérarchie.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Organization parent;

    /**
     * Adresse de l'organisation.
     */
    @Column(name = "address")
    private String address;

    /**
     * Numéro de téléphone de l'organisation.
     */
    @Column(name = "phone")
    private String phone;

    /**
     * Email de l'organisation.
     */
    @Column(name = "email")
    private String email;

    /**
     * Site web de l'organisation.
     */
    @Column(name = "website")
    private String website;

    /**
     * URL du logo de l'organisation.
     */
    @Column(name = "logo_url")
    private String logoUrl;

    /**
     * Nom du contact principal de l'organisation.
     */
    @Column(name = "primary_contact_name")
    private String primaryContactName;

    /**
     * Description de l'organisation.
     */
    @Column(name = "description")
    private String description;

    /**
     * Paramètres de l'organisation au format JSON.
     */
    @Column(name = "settings", columnDefinition = "jsonb")
    private String settings;

    /**
     * Organisations enfants dans la hiérarchie.
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Organization> children = new HashSet<>();

    /**
     * Ajoute une organisation enfant.
     *
     * @param child Organisation enfant à ajouter
     */
    public void addChild(Organization child) {
        children.add(child);
        child.setParent(this);
    }

    /**
     * Supprime une organisation enfant.
     *
     * @param child Organisation enfant à supprimer
     */
    public void removeChild(Organization child) {
        children.remove(child);
        child.setParent(null);
    }

    /**
     * Vérifie si l'organisation est une organisation racine (sans parent).
     *
     * @return true si l'organisation est une racine, false sinon
     */
    public boolean isRoot() {
        return parent == null;
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
     * Vérifie si l'organisation est active.
     *
     * @return true si l'organisation est active, false sinon
     */
    public boolean isActive() {
        return status == OrganizationStatus.ACTIVE;
    }

    /**
     * Active l'organisation.
     */
    public void activate() {
        this.status = OrganizationStatus.ACTIVE;
        super.activate();
    }

    /**
     * Désactive l'organisation.
     */
    public void deactivate() {
        this.status = OrganizationStatus.INACTIVE;
        super.deactivate();
    }

    /**
     * Suspend l'organisation.
     */
    public void suspend() {
        this.status = OrganizationStatus.SUSPENDED;
    }
}
