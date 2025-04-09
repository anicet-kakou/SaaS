package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.FuelTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.FuelTypeService;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.ReferenceDataService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.FuelType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implémentation du service de gestion des données de référence.
 */
@Service
@RequiredArgsConstructor
public class ReferenceDataServiceImpl implements ReferenceDataService {

    private final FuelTypeService fuelTypeService;

    @Override
    public List<FuelTypeDTO> getActiveFuelTypes(UUID organizationId) {
        return fuelTypeService.getAllActiveFuelTypes(organizationId);
    }

    @Override
    public Optional<FuelTypeDTO> getFuelTypeById(UUID id, UUID organizationId) {
        return fuelTypeService.getFuelTypeById(id, organizationId);
    }

    @Override
    public Optional<FuelTypeDTO> getFuelTypeByCode(String code, UUID organizationId) {
        return fuelTypeService.getFuelTypeByCode(code, organizationId);
    }

    @Override
    public void initializeStandardReferenceData(UUID organizationId) {
        // Initialisation des types de carburant standard
        initializeStandardFuelTypes(organizationId);

        // Autres initialisations de données de référence standard
        // ...
    }

    @Override
    public boolean referenceExists(String referenceType, UUID referenceId, UUID organizationId) {
        switch (referenceType) {
            case "FUEL_TYPE":
                return fuelTypeService.getFuelTypeById(referenceId, organizationId).isPresent();
            // Autres types de référence
            // ...
            default:
                return false;
        }
    }

    @Override
    public Map<String, List<?>> getReferenceDataForUI(UUID organizationId) {
        Map<String, List<?>> referenceData = new HashMap<>();

        // Ajout des types de carburant
        referenceData.put("fuelTypes", fuelTypeService.getAllActiveFuelTypes(organizationId));

        // Ajout d'autres données de référence
        // ...

        return referenceData;
    }

    /**
     * Initialise les types de carburant standard pour une nouvelle organisation.
     *
     * @param organizationId L'ID de l'organisation
     */
    private void initializeStandardFuelTypes(UUID organizationId) {
        // Liste des types de carburant standard
        List<FuelType> standardFuelTypes = Arrays.asList(
                FuelType.builder()
                        .code("GASOLINE")
                        .name("Essence")
                        .description("Carburant à base d'essence")
                        .isActive(true)
                        .organizationId(organizationId)
                        .build(),
                FuelType.builder()
                        .code("DIESEL")
                        .name("Diesel")
                        .description("Carburant à base de diesel")
                        .isActive(true)
                        .organizationId(organizationId)
                        .build(),
                FuelType.builder()
                        .code("ELECTRIC")
                        .name("Électrique")
                        .description("Véhicule électrique")
                        .isActive(true)
                        .organizationId(organizationId)
                        .build(),
                FuelType.builder()
                        .code("HYBRID")
                        .name("Hybride")
                        .description("Véhicule hybride (essence/électrique)")
                        .isActive(true)
                        .organizationId(organizationId)
                        .build(),
                FuelType.builder()
                        .code("LPG")
                        .name("GPL")
                        .description("Gaz de pétrole liquéfié")
                        .isActive(true)
                        .organizationId(organizationId)
                        .build(),
                FuelType.builder()
                        .code("CNG")
                        .name("GNV")
                        .description("Gaz naturel pour véhicules")
                        .isActive(true)
                        .organizationId(organizationId)
                        .build()
        );

        // Sauvegarde des types de carburant standard
        for (FuelType fuelType : standardFuelTypes) {
            // Vérifier si le type de carburant existe déjà
            if (fuelTypeService.getFuelTypeByCode(fuelType.getCode(), organizationId).isEmpty()) {
                fuelTypeService.createFuelType(fuelType, organizationId);
            }
        }
    }
}
