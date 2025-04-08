package com.devolution.saas.core.security.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité représentant la relation entre un utilisateur et une organisation.
 * Cette entité permet de gérer l'appartenance d'un utilisateur à plusieurs organisations.
 */
@Entity
@Table(name = "user_organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(UserOrganizationId.class)
public class UserOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID de l'utilisateur.
     */
    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * ID de l'organisation.
     */
    @Id
    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    /**
     * Date de création de la relation.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Date de dernière modification de la relation.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Constructeur avec les champs clés.
     *
     * @param userId         ID de l'utilisateur
     * @param organizationId ID de l'organisation
     */
    public UserOrganization(UUID userId, UUID organizationId) {
        this.userId = userId;
        this.organizationId = organizationId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
