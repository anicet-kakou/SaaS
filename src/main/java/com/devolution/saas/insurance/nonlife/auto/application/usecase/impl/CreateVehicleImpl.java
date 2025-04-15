package com.devolution.saas.insurance.nonlife.auto.application.usecase.impl;

import com.devolution.saas.insurance.nonlife.auto.application.command.CreateVehicleCommand;
import com.devolution.saas.insurance.nonlife.auto.application.dto.VehicleDTO;
import com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceAlreadyExistsException;
import com.devolution.saas.insurance.nonlife.auto.application.exception.BusinessRuleViolationException;
import com.devolution.saas.insurance.nonlife.auto.application.mapper.VehicleMapper;
import com.devolution.saas.insurance.nonlife.auto.application.usecase.CreateVehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.VehicleRepository;
import com.devolution.saas.insurance.nonlife.auto.domain.service.VehicleValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implémentation du cas d'utilisation pour la création d'un véhicule.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateVehicleImpl implements CreateVehicle {

    private final VehicleRepository vehicleRepository;
    private final VehicleValidator vehicleValidator;
    private final VehicleMapper vehicleMapper;

    @Override
    @Transactional
    public VehicleDTO execute(CreateVehicleCommand command) {
        log.debug("Création d'un véhicule avec immatriculation: {}", command.registrationNumber());

        // Vérifier si un véhicule avec la même immatriculation existe déjà
        vehicleRepository.findByRegistrationNumber(command.registrationNumber(), command.organizationId())
                .ifPresent(v -> {
                    throw AutoResourceAlreadyExistsException.forIdentifier("Véhicule", "immatriculation", command.registrationNumber());
                });

        // Mapper la commande en entité
        Vehicle vehicle = mapCommandToEntity(command);

        // Valider le véhicule
        List<String> validationErrors = vehicleValidator.validateForCreation(vehicle, command.organizationId());
        if (!validationErrors.isEmpty()) {
            throw new BusinessRuleViolationException("Erreurs de validation: " + String.join(", ", validationErrors));
        }

        // Sauvegarder le véhicule
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // Mapper l'entité en DTO
        return vehicleMapper.toDto(savedVehicle);
    }

    /**
     * Mappe une commande de création en entité Vehicle.
     *
     * @param command La commande à mapper
     * @return L'entité Vehicle correspondante
     */
    private Vehicle mapCommandToEntity(CreateVehicleCommand command) {
        Vehicle vehicle = Vehicle.builder()
                .registrationNumber(command.registrationNumber())
                .modelVariant(command.modelVariant())
                .year(command.year())
                .enginePower(command.enginePower())
                .engineSize(command.engineSize())
                .purchaseDate(command.purchaseDate())
                .purchaseValue(command.purchaseValue())
                .currentValue(command.currentValue())
                .mileage(command.mileage())
                .vin(command.vin())
                .ownerId(command.ownerId())
                .organizationId(command.organizationId())
                .build();

        // Utiliser les setters pour définir les IDs
        vehicle.setManufacturerId(command.makeId());
        vehicle.setModelId(command.modelId());
        vehicle.setFuelTypeId(command.fuelTypeId());
        vehicle.setCategoryId(command.categoryId());
        vehicle.setSubcategoryId(command.subcategoryId());
        vehicle.setUsageId(command.usageId());
        vehicle.setGeographicZoneId(command.geographicZoneId());
        vehicle.setColorId(command.colorId());

        return vehicle;
    }
}
