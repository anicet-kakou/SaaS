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
        log.debug("Création d'un nouveau véhicule avec immatriculation: {}", command.registrationNumber());

        // Vérifier si un véhicule avec la même immatriculation existe déjà
        vehicleRepository.findByRegistrationNumber(command.registrationNumber(), command.organizationId())
                .ifPresent(v -> {
                    throw AutoResourceAlreadyExistsException.forIdentifier("Véhicule", "immatriculation", command.registrationNumber());
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

        // Utiliser toList() de Java 16+ au lieu de collect(Collectors.toList())
        return vehicles.stream()
                .map(this::mapEntityToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByOwner(UUID ownerId, UUID organizationId) {
        log.debug("Listage des véhicules pour le propriétaire: {} dans l'organisation: {}", ownerId, organizationId);

        List<Vehicle> vehicles = vehicleRepository.findAllByOwnerIdAndOrganizationId(ownerId, organizationId);

        // Utiliser toList() de Java 16+ au lieu de collect(Collectors.toList())
        return vehicles.stream()
                .map(this::mapEntityToDTO)
                .toList();
    }

    @Override
    @Transactional
    public VehicleDTO updateVehicle(UUID id, CreateVehicleCommand command) {
        log.debug("Mise à jour du véhicule avec ID: {}", id);

        // Vérifier que le véhicule existe
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Véhicule", id));

        // Vérifier que le véhicule appartient à l'organisation
        if (!existingVehicle.getOrganizationId().equals(command.organizationId())) {
            throw AutoResourceNotFoundException.forId("Véhicule", id);
        }

        // Si l'immatriculation a changé, vérifier qu'elle n'est pas déjà utilisée
        if (!existingVehicle.getRegistrationNumber().equals(command.registrationNumber())) {
            vehicleRepository.findByRegistrationNumber(command.registrationNumber(), command.organizationId())
                    .ifPresent(v -> {
                        if (!v.getId().equals(id)) {
                            throw AutoResourceAlreadyExistsException.forIdentifier("Véhicule", "immatriculation", command.registrationNumber());
                        }
                    });
        }

        // Créer une nouvelle instance de Vehicle avec l'ID existant et les nouvelles propriétés
        // Create a new vehicle with basic properties
        Vehicle updatedVehicle = new Vehicle();
        updatedVehicle.setId(id); // Conserver l'ID existant
        updatedVehicle.setRegistrationNumber(command.registrationNumber());
        updatedVehicle.setModelVariant(command.modelVariant());
        updatedVehicle.setYear(command.year());
        updatedVehicle.setEnginePower(command.enginePower());
        updatedVehicle.setEngineSize(command.engineSize());
        updatedVehicle.setPurchaseDate(command.purchaseDate());
        updatedVehicle.setPurchaseValue(command.purchaseValue());
        updatedVehicle.setCurrentValue(command.currentValue());
        updatedVehicle.setMileage(command.mileage());
        updatedVehicle.setVin(command.vin());
        updatedVehicle.setOwnerId(command.ownerId());
        updatedVehicle.setOrganizationId(command.organizationId());

        // Set IDs for related entities
        updatedVehicle.setManufacturerId(command.makeId());
        updatedVehicle.setModelId(command.modelId());
        updatedVehicle.setFuelTypeId(command.fuelTypeId());
        updatedVehicle.setCategoryId(command.categoryId());
        updatedVehicle.setSubcategoryId(command.subcategoryId());
        updatedVehicle.setUsageId(command.usageId());
        updatedVehicle.setGeographicZoneId(command.geographicZoneId());
        updatedVehicle.setColorId(command.colorId());

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
        // Créer une nouvelle instance de Vehicle avec tous les champs
        Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNumber(command.registrationNumber());
        vehicle.setModelVariant(command.modelVariant());
        vehicle.setYear(command.year());
        vehicle.setEnginePower(command.enginePower());
        vehicle.setEngineSize(command.engineSize());
        vehicle.setPurchaseDate(command.purchaseDate());
        vehicle.setPurchaseValue(command.purchaseValue());
        vehicle.setCurrentValue(command.currentValue());
        vehicle.setMileage(command.mileage());
        vehicle.setVin(command.vin());
        vehicle.setOwnerId(command.ownerId());
        vehicle.setOrganizationId(command.organizationId());

        // Ajouter les IDs directement
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
                .manufacturerId(vehicle.getManufacturerId())
                .manufacturerName(null) // manufacturerName - à compléter si nécessaire
                .modelId(vehicle.getModelId())
                .modelName(null) // modelName - à compléter si nécessaire
                .modelVariant(vehicle.getModelVariant())
                .version(vehicle.getVersion())
                .year(vehicle.getYear())
                .enginePower(vehicle.getEnginePower())
                .engineSize(vehicle.getEngineSize())
                .fuelTypeId(vehicle.getFuelTypeId())
                .fuelTypeName(null) // fuelTypeName - à compléter si nécessaire
                .categoryId(vehicle.getCategoryId())
                .categoryName(null) // categoryName - à compléter si nécessaire
                .subcategoryId(vehicle.getSubcategoryId())
                .subcategoryName(null) // subcategoryName - à compléter si nécessaire
                .usageId(vehicle.getUsageId())
                .usageName(null) // usageName - à compléter si nécessaire
                .geographicZoneId(vehicle.getGeographicZoneId())
                .geographicZoneName(null) // geographicZoneName - à compléter si nécessaire
                .purchaseDate(vehicle.getPurchaseDate())
                .purchaseValue(vehicle.getPurchaseValue())
                .currentValue(vehicle.getCurrentValue())
                .mileage(vehicle.getMileage())
                .vin(vehicle.getVin())
                .colorId(vehicle.getColorId())
                .colorName(null) // colorName - à compléter si nécessaire
                .ownerId(vehicle.getOwnerId())
                .ownerName(null) // ownerName - à compléter si nécessaire
                .organizationId(vehicle.getOrganizationId())
                .autoPolicyId(vehicle.getAutoPolicyId())
                .hasAntiTheftDevice(vehicle.isHasAntiTheftDevice())
                .parkingType(vehicle.getParkingType() != null ? vehicle.getParkingType().name() : null)
                .build();
    }
}
