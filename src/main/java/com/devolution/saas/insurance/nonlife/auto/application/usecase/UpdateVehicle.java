package com.devolution.saas.insurance.nonlife.auto.application.usecase;

import com.devolution.saas.insurance.nonlife.auto.application.command.CreateVehicleCommand;
import com.devolution.saas.insurance.nonlife.auto.application.dto.VehicleDTO;

import java.util.UUID;

/**
 * Cas d'utilisation pour la mise à jour d'un véhicule.
 */
public interface UpdateVehicle {

    /**
     * Met à jour un véhicule existant.
     *
     * @param id      L'ID du véhicule à mettre à jour
     * @param command La commande de mise à jour du véhicule
     * @return Le DTO du véhicule mis à jour
     */
    VehicleDTO execute(UUID id, CreateVehicleCommand command);
}
