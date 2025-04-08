package com.devolution.saas.core.organization.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Requête pour obtenir une organisation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOrganizationQuery {

    /**
     * ID de l'organisation à récupérer.
     */
    private UUID id;

    /**
     * Code de l'organisation à récupérer.
     */
    private String code;

    /**
     * Vérifie si la requête est valide.
     *
     * @return true si la requête est valide, false sinon
     */
    public boolean isValid() {
        return id != null || (code != null && !code.trim().isEmpty());
    }
}
