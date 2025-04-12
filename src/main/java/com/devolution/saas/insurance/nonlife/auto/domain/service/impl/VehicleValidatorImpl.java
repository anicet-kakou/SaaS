package com.devolution.saas.insurance.nonlife.auto.domain.service.impl;

import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.service.VehicleValidator;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implémentation du service de validation des véhicules.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleValidatorImpl implements VehicleValidator {

    private final VehicleModelRepository vehicleModelRepository;
    private final VehicleCategoryRepository vehicleCategoryRepository;
    private final VehicleSubcategoryRepository vehicleSubcategoryRepository;
    private final VehicleUsageRepository vehicleUsageRepository;
    private final VehicleColorRepository vehicleColorRepository;
    private final VehicleManufacturerRepository vehicleManufacturerRepository;
    private final VehicleFuelTypeRepository vehicleFuelTypeRepository;

    @Override
    public List<String> validateForCreation(Vehicle vehicle, UUID organizationId) {
        log.debug("Validation d'un véhicule pour création: {}", vehicle.getRegistrationNumber());

        List<String> errors = new ArrayList<>();

        // Validation des champs obligatoires
        if (vehicle.getRegistrationNumber() == null || vehicle.getRegistrationNumber().isBlank()) {
            errors.add("Le numéro d'immatriculation est obligatoire");
        }

        if (vehicle.getManufacturerId() == null) {
            errors.add("La marque du véhicule est obligatoire");
        }

        if (vehicle.getModelId() == null) {
            errors.add("Le modèle du véhicule est obligatoire");
        }

        if (vehicle.getYear() <= 1900) {
            errors.add("L'année du véhicule doit être supérieure à 1900");
        }

        if (vehicle.getFuelTypeId() == null) {
            errors.add("Le type de carburant est obligatoire");
        }

        if (vehicle.getCategoryId() == null) {
            errors.add("La catégorie du véhicule est obligatoire");
        }

        if (vehicle.getUsageId() == null) {
            errors.add("L'usage du véhicule est obligatoire");
        }

        if (vehicle.getColorId() == null) {
            errors.add("La couleur du véhicule est obligatoire");
        }

        if (vehicle.getOwnerId() == null) {
            errors.add("Le propriétaire du véhicule est obligatoire");
        }

        // Validation des références
        errors.addAll(validateReferences(vehicle, organizationId));

        // Validation des règles métier
        errors.addAll(validateBusinessRules(vehicle));

        return errors;
    }

    @Override
    public List<String> validateForUpdate(Vehicle vehicle, Vehicle existingVehicle, UUID organizationId) {
        log.debug("Validation d'un véhicule pour mise à jour: {}", vehicle.getRegistrationNumber());

        List<String> errors = new ArrayList<>();

        // Validation des champs obligatoires (comme pour la création)
        if (vehicle.getRegistrationNumber() == null || vehicle.getRegistrationNumber().isBlank()) {
            errors.add("Le numéro d'immatriculation est obligatoire");
        }

        if (vehicle.getManufacturerId() == null) {
            errors.add("La marque du véhicule est obligatoire");
        }

        if (vehicle.getModelId() == null) {
            errors.add("Le modèle du véhicule est obligatoire");
        }

        if (vehicle.getYear() <= 1900) {
            errors.add("L'année du véhicule doit être supérieure à 1900");
        }

        if (vehicle.getFuelTypeId() == null) {
            errors.add("Le type de carburant est obligatoire");
        }

        if (vehicle.getCategoryId() == null) {
            errors.add("La catégorie du véhicule est obligatoire");
        }

        if (vehicle.getUsageId() == null) {
            errors.add("L'usage du véhicule est obligatoire");
        }

        if (vehicle.getColorId() == null) {
            errors.add("La couleur du véhicule est obligatoire");
        }

        if (vehicle.getOwnerId() == null) {
            errors.add("Le propriétaire du véhicule est obligatoire");
        }

        // Validation des références
        errors.addAll(validateReferences(vehicle, organizationId));

        // Validation des règles métier
        errors.addAll(validateBusinessRules(vehicle));

        // Validation spécifique à la mise à jour
        if (!existingVehicle.getRegistrationNumber().equals(vehicle.getRegistrationNumber())) {
            errors.add("Le numéro d'immatriculation ne peut pas être modifié");
        }

        if (existingVehicle.getYear() != vehicle.getYear()) {
            errors.add("L'année du véhicule ne peut pas être modifiée");
        }

        return errors;
    }

    @Override
    public List<String> validateReferences(Vehicle vehicle, UUID organizationId) {
        log.debug("Validation des références pour le véhicule: {}", vehicle.getRegistrationNumber());

        List<String> errors = new ArrayList<>();

        // Vérification de l'existence de la marque
        if (vehicle.getManufacturerId() != null &&
                !vehicleManufacturerRepository.findById(vehicle.getManufacturerId())
                        .filter(make -> make.getOrganizationId().equals(organizationId))
                        .isPresent()) {
            errors.add("La marque spécifiée n'existe pas");
        }

        // Vérification de l'existence du modèle
        if (vehicle.getModelId() != null &&
                !vehicleModelRepository.findById(vehicle.getModelId())
                        .filter(model -> model.getOrganizationId().equals(organizationId))
                        .isPresent()) {
            errors.add("Le modèle spécifié n'existe pas");
        }

        // Vérification de l'existence de la catégorie
        if (vehicle.getCategoryId() != null &&
                !vehicleCategoryRepository.findById(vehicle.getCategoryId())
                        .filter(category -> category.getOrganizationId().equals(organizationId))
                        .isPresent()) {
            errors.add("La catégorie spécifiée n'existe pas");
        }

        // Vérification de l'existence de la sous-catégorie
        if (vehicle.getSubcategoryId() != null &&
                !vehicleSubcategoryRepository.findById(vehicle.getSubcategoryId())
                        .filter(subcategory -> subcategory.getOrganizationId().equals(organizationId))
                        .isPresent()) {
            errors.add("La sous-catégorie spécifiée n'existe pas");
        }

        // Vérification de l'existence du type de carburant
        if (vehicle.getFuelTypeId() != null &&
                !vehicleFuelTypeRepository.findById(vehicle.getFuelTypeId())
                        .filter(fuelType -> fuelType.getOrganizationId().equals(organizationId))
                        .isPresent()) {
            errors.add("Le type de carburant spécifié n'existe pas");
        }

        // Vérification de l'existence de l'usage
        if (vehicle.getUsageId() != null &&
                !vehicleUsageRepository.findById(vehicle.getUsageId())
                        .filter(usage -> usage.getOrganizationId().equals(organizationId))
                        .isPresent()) {
            errors.add("L'usage spécifié n'existe pas");
        }

        // Vérification de l'existence de la couleur
        if (vehicle.getColorId() != null &&
                !vehicleColorRepository.findById(vehicle.getColorId())
                        .filter(color -> color.getOrganizationId().equals(organizationId))
                        .isPresent()) {
            errors.add("La couleur spécifiée n'existe pas");
        }

        return errors;
    }

    @Override
    public List<String> validateBusinessRules(Vehicle vehicle) {
        log.debug("Validation des règles métier pour le véhicule: {}", vehicle.getRegistrationNumber());

        List<String> errors = new ArrayList<>();

        // Validation du numéro d'immatriculation (format selon le pays)
        if (vehicle.getRegistrationNumber() != null &&
                !vehicle.getRegistrationNumber().matches("^[A-Z0-9-]+$")) {
            errors.add("Le numéro d'immatriculation n'est pas dans un format valide");
        }

        // Validation de l'année (pas dans le futur)
        if (vehicle.getYear() > LocalDate.now().getYear()) {
            errors.add("L'année du véhicule ne peut pas être dans le futur");
        }

        // Validation de la puissance du moteur
        if (vehicle.getEngineSize() != null && vehicle.getEngineSize() <= 0) {
            errors.add("La cylindrée du moteur doit être positive");
        }

        if (vehicle.getEnginePower() != null && vehicle.getEnginePower() <= 0) {
            errors.add("La puissance du moteur doit être positive");
        }

        // Validation du VIN (Vehicle Identification Number)
        if (vehicle.getVin() != null && !vehicle.getVin().matches("^[A-HJ-NPR-Z0-9]{17}$")) {
            errors.add("Le numéro VIN n'est pas dans un format valide (17 caractères alphanumériques sans I, O, Q)");
        }

        // Validation de la date d'achat (pas dans le futur)
        if (vehicle.getPurchaseDate() != null && vehicle.getPurchaseDate().isAfter(LocalDate.now())) {
            errors.add("La date d'achat ne peut pas être dans le futur");
        }

        // Validation de la cohérence entre la date d'achat et l'année du véhicule
        if (vehicle.getPurchaseDate() != null && vehicle.getPurchaseDate().getYear() < vehicle.getYear()) {
            errors.add("La date d'achat ne peut pas être antérieure à l'année du véhicule");
        }

        // Validation du kilométrage
        if (vehicle.getMileage() != null && vehicle.getMileage() < 0) {
            errors.add("Le kilométrage ne peut pas être négatif");
        }

        return errors;
    }
}
