package com.devolution.saas.core.security.domain.model;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * Classe représentant la clé primaire composée de l'entité UserOrganization.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserOrganizationId implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID de l'utilisateur.
     */
    private UUID userId;

    /**
     * ID de l'organisation.
     */
    private UUID organizationId;
}
