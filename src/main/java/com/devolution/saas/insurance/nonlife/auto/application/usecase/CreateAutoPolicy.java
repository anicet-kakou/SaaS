package com.devolution.saas.insurance.nonlife.auto.application.usecase;

import com.devolution.saas.insurance.nonlife.auto.application.command.CreateAutoPolicyCommand;
import com.devolution.saas.insurance.nonlife.auto.application.dto.AutoPolicyDTO;

/**
 * Cas d'utilisation pour la création d'une police d'assurance automobile.
 */
public interface CreateAutoPolicy {

    /**
     * Crée une nouvelle police d'assurance automobile.
     *
     * @param command La commande de création de la police
     * @return Le DTO de la police créée
     */
    AutoPolicyDTO execute(CreateAutoPolicyCommand command);
}
