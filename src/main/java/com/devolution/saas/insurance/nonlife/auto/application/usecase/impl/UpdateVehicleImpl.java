package com.devolution.saas.insurance.nonlife.auto.application.usecase.impl;

import com.devolution.saas.insurance.nonlife.auto.application.command.CreateVehicleCommand;
import com.devolution.saas.insurance.nonlife.auto.application.dto.VehicleDTO;
import com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceAlreadyExistsException;
import com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.application.exception.BusinessRuleViolationException;
import com.devolution.saas.insurance.nonlife.auto.application.mapper.VehicleMapper;
import com.devolution.saas.insurance.nonlife.auto.application.usecase.UpdateVehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.VehicleRepository;
import com.devolution.saas.insurance.nonlife.auto.domain.service.VehicleValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implémentation du cas d'utilisation pour la mise à jour d'un véhicule.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateVehicleImpl implements UpdateVehicle {

    private final VehicleRepository vehicleRepository;
    private final VehicleValidator vehicleValidator;
    private final VehicleMapper vehicleMapper;

    @Override
    @Transactional
    public VehicleDTO execute(UUID id, CreateVehicleCommand command) {
        log.debug("Mise à jour du véhicule avec ID: {}", id);

        // Vérifier que le véhicule existe
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Véhicule", id));

        // Vérifier que le véhicule appartient à l'organisation
        if (!existingVehicle.getOrganizationId().equals(command.getOrganizationId())) {
            throw AutoResourceNotFoundException.forId("Véhicule", id);
        }

        // Si le numéro d'immatriculation a changé, vérifier qu'il n'est pas déjà utilisé
        if (!existingVehicle.getRegistrationNumber().equals(command.getRegistrationNumber())) {
            vehicleRepository.findByRegistrationNumber(command.getRegistrationNumber(), command.getOrganizationId())
                    .ifPresent(v -> {
                        if (!v.getId().equals(id)) {
                            throw AutoResourceAlreadyExistsException.forIdentifier("Véhicule", "immatriculation", command.getRegistrationNumber());
                        }
                    });
        }

        // Mapper la commande en entité
        Vehicle vehicle = mapCommandToEntity(command);
        vehicle.setId(id);

        // Valider le véhicule
        List<String> validationErrors = vehicleValidator.validateForUpdate(vehicle, existingVehicle, command.getOrganizationId());
        if (!validationErrors.isEmpty()) {
            throw new BusinessRuleViolationException("Erreurs de validation: " + String.join(", ", validationErrors));
        }

        // Sauvegarder le véhicule
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        // Mapper l'entité en DTO
        return vehicleMapper.toDto(updatedVehicle);
    }

    /**
     * Mappe une commande de mise à jour en entité Vehicle.
     *
     * @param command La commande à mapper
     * @return L'entité Vehicle correspondante
     */
    private Vehicle mapCommandToEntity(CreateVehicleCommand command) {
        Vehicle vehicle = Vehicle.builder()
                .registrationNumber(command.getRegistrationNumber())
                .modelVariant(command.getModelVariant())
                .year(command.getYear())
                .enginePower(command.getEnginePower())
                .engineSize(command.getEngineSize())
                .purchaseDate(command.getPurchaseDate())
                .purchaseValue(command.getPurchaseValue())
                .currentValue(command.getCurrentValue())
                .mileage(command.getMileage())
                .vin(command.getVin())
                .ownerId(command.getOwnerId())
                .organizationId(command.getOrganizationId())
                .build();

        // Utiliser les setters pour définir les IDs
        vehicle.setManufacturerId(command.getMakeId());
        vehicle.setModelId(command.getModelId());
        vehicle.setFuelTypeId(command.getFuelTypeId());
        vehicle.setCategoryId(command.getCategoryId());
        vehicle.setSubcategoryId(command.getSubcategoryId());
        vehicle.setUsageId(command.getUsageId());
        vehicle.setGeographicZoneId(command.getGeographicZoneId());
        vehicle.setColorId(command.getColorId());

        return vehicle;
    }
}
