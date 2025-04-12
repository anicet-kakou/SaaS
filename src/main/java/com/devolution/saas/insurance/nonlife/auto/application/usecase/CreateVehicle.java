package com.devolution.saas.insurance.nonlife.auto.application.usecase;

import com.devolution.saas.insurance.nonlife.auto.application.command.CreateVehicleCommand;
import com.devolution.saas.insurance.nonlife.auto.application.dto.VehicleDTO;

/**
 * Cas d'utilisation pour la création d'un véhicule.
 */
public interface CreateVehicle {

    /**
     * Crée un nouveau véhicule.
     *
     * @param command La commande de création du véhicule
     * @return Le DTO du véhicule créé
     */
    VehicleDTO execute(CreateVehicleCommand command);
}
