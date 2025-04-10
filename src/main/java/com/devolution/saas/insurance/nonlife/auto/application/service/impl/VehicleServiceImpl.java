package com.devolution.saas.insurance.nonlife.auto.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.application.command.CreateVehicleCommand;
import com.devolution.saas.insurance.nonlife.auto.application.dto.VehicleDTO;
import com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceAlreadyExistsException;
import com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.application.service.VehicleService;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service d'application pour la gestion des véhicules.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    @Transactional
    public VehicleDTO createVehicle(CreateVehicleCommand command) {
        log.debug("Création d'un nouveau véhicule avec immatriculation: {}", command.getRegistrationNumber());

        // Vérifier si un véhicule avec la même immatriculation existe déjà
        vehicleRepository.findByRegistrationNumber(command.getRegistrationNumber(), command.getOrganizationId())
                .ifPresent(v -> {
                    throw AutoResourceAlreadyExistsException.forIdentifier("Véhicule", "immatriculation", command.getRegistrationNumber());
                });

        // Créer et sauvegarder le véhicule
        Vehicle vehicle = mapCommandToEntity(command);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return mapEntityToDTO(savedVehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleDTO getVehicleById(UUID id, UUID organizationId) {
        log.debug("Récupération du véhicule avec ID: {}", id);

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Véhicule", id));

        // Vérifier que le véhicule appartient à l'organisation
        if (!vehicle.getOrganizationId().equals(organizationId)) {
            throw AutoResourceNotFoundException.forId("Véhicule", id);
        }

        return mapEntityToDTO(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleDTO getVehicleByRegistrationNumber(String registrationNumber, UUID organizationId) {
        log.debug("Récupération du véhicule avec immatriculation: {}", registrationNumber);

        Vehicle vehicle = vehicleRepository.findByRegistrationNumber(registrationNumber, organizationId)
                .orElseThrow(() -> AutoResourceNotFoundException.forIdentifier("Véhicule", "immatriculation", registrationNumber));

        return mapEntityToDTO(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getAllVehicles(UUID organizationId) {
        log.debug("Listage de tous les véhicules pour l'organisation: {}", organizationId);

        List<Vehicle> vehicles = vehicleRepository.findAllByOrganizationId(organizationId);

        return vehicles.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByOwner(UUID ownerId, UUID organizationId) {
        log.debug("Listage des véhicules pour le propriétaire: {} dans l'organisation: {}", ownerId, organizationId);

        List<Vehicle> vehicles = vehicleRepository.findAllByOwnerIdAndOrganizationId(ownerId, organizationId);

        return vehicles.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VehicleDTO updateVehicle(UUID id, CreateVehicleCommand command) {
        log.debug("Mise à jour du véhicule avec ID: {}", id);

        // Vérifier que le véhicule existe
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Véhicule", id));

        // Vérifier que le véhicule appartient à l'organisation
        if (!existingVehicle.getOrganizationId().equals(command.getOrganizationId())) {
            throw AutoResourceNotFoundException.forId("Véhicule", id);
        }

        // Si l'immatriculation a changé, vérifier qu'elle n'est pas déjà utilisée
        if (!existingVehicle.getRegistrationNumber().equals(command.getRegistrationNumber())) {
            vehicleRepository.findByRegistrationNumber(command.getRegistrationNumber(), command.getOrganizationId())
                    .ifPresent(v -> {
                        if (!v.getId().equals(id)) {
                            throw AutoResourceAlreadyExistsException.forIdentifier("Véhicule", "immatriculation", command.getRegistrationNumber());
                        }
                    });
        }

        // Mettre à jour les propriétés du véhicule
        Vehicle updatedVehicle = mapCommandToEntity(command);
        updatedVehicle.setId(id);

        Vehicle savedVehicle = vehicleRepository.save(updatedVehicle);

        return mapEntityToDTO(savedVehicle);
    }

    @Override
    @Transactional
    public void deleteVehicle(UUID id, UUID organizationId) {
        log.debug("Suppression du véhicule avec ID: {}", id);

        // Vérifier que le véhicule existe et appartient à l'organisation
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Véhicule", id));

        if (!vehicle.getOrganizationId().equals(organizationId)) {
            throw AutoResourceNotFoundException.forId("Véhicule", id);
        }

        vehicleRepository.deleteById(id);
    }

    /**
     * Convertit une commande en entité.
     *
     * @param command La commande à convertir
     * @return L'entité correspondante
     */
    private Vehicle mapCommandToEntity(CreateVehicleCommand command) {
        return Vehicle.builder()
                .registrationNumber(command.getRegistrationNumber())
                .makeId(command.getMakeId())
                .modelId(command.getModelId())
                .version(command.getVersion())
                .year(command.getYear())
                .enginePower(command.getEnginePower())
                .engineSize(command.getEngineSize())
                .fuelTypeId(command.getFuelTypeId())
                .categoryId(command.getCategoryId())
                .subcategoryId(command.getSubcategoryId())
                .usageId(command.getUsageId())
                .geographicZoneId(command.getGeographicZoneId())
                .purchaseDate(command.getPurchaseDate())
                .purchaseValue(command.getPurchaseValue())
                .currentValue(command.getCurrentValue())
                .mileage(command.getMileage())
                .vin(command.getVin())
                .colorId(command.getColorId())
                .ownerId(command.getOwnerId())
                .organizationId(command.getOrganizationId())
                .build();
    }

    /**
     * Convertit une entité en DTO.
     *
     * @param vehicle L'entité à convertir
     * @return Le DTO correspondant
     */
    private VehicleDTO mapEntityToDTO(Vehicle vehicle) {
        return VehicleDTO.builder()
                .id(vehicle.getId())
                .registrationNumber(vehicle.getRegistrationNumber())
                .makeId(vehicle.getMakeId())
                .modelId(vehicle.getModelId())
                .version(vehicle.getVersion())
                .year(vehicle.getYear())
                .enginePower(vehicle.getEnginePower())
                .engineSize(vehicle.getEngineSize())
                .fuelTypeId(vehicle.getFuelTypeId())
                .categoryId(vehicle.getCategoryId())
                .subcategoryId(vehicle.getSubcategoryId())
                .usageId(vehicle.getUsageId())
                .geographicZoneId(vehicle.getGeographicZoneId())
                .purchaseDate(vehicle.getPurchaseDate())
                .purchaseValue(vehicle.getPurchaseValue())
                .currentValue(vehicle.getCurrentValue())
                .mileage(vehicle.getMileage())
                .vin(vehicle.getVin())
                .colorId(vehicle.getColorId())
                .ownerId(vehicle.getOwnerId())
                .build();
    }
}
