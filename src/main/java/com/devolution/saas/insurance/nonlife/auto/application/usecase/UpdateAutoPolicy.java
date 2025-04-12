package com.devolution.saas.insurance.nonlife.auto.application.usecase;

import com.devolution.saas.insurance.nonlife.auto.application.command.CreateAutoPolicyCommand;
import com.devolution.saas.insurance.nonlife.auto.application.dto.AutoPolicyDTO;

import java.util.UUID;

/**
 * Cas d'utilisation pour la mise à jour d'une police d'assurance automobile.
 */
public interface UpdateAutoPolicy {

    /**
     * Met à jour une police d'assurance automobile existante.
     *
     * @param id      L'ID de la police à mettre à jour
     * @param command La commande de mise à jour de la police
     * @return Le DTO de la police mise à jour
     */
    AutoPolicyDTO execute(UUID id, CreateAutoPolicyCommand command);
}
