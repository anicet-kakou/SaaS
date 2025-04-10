package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.*;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.*;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des données de référence.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReferenceDataServiceImpl implements ReferenceDataService {

    private final FuelTypeService fuelTypeService;
    private final VehicleCategoryService vehicleCategoryService;
    // Les autres services seront injectés au fur et à mesure de leur implémentation

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
    public List<VehicleCategoryDTO> getActiveVehicleCategories(UUID organizationId) {
        return vehicleCategoryService.getAllActiveVehicleCategories(organizationId);
    }

    @Override
    public Optional<VehicleCategoryDTO> getVehicleCategoryById(UUID id, UUID organizationId) {
        return vehicleCategoryService.getVehicleCategoryById(id, organizationId);
    }

    @Override
    public Optional<VehicleCategoryDTO> getVehicleCategoryByCode(String code, UUID organizationId) {
        return vehicleCategoryService.getVehicleCategoryByCode(code, organizationId);
    }

    // Les autres méthodes seront implémentées au fur et à mesure de l'implémentation des services correspondants

    @Override
    public List<VehicleSubcategoryDTO> getActiveVehicleSubcategories(UUID organizationId) {
        // À implémenter lorsque VehicleSubcategoryService sera disponible
        return Collections.emptyList();
    }

    @Override
    public List<VehicleSubcategoryDTO> getActiveVehicleSubcategoriesByCategory(UUID categoryId, UUID organizationId) {
        // À implémenter lorsque VehicleSubcategoryService sera disponible
        return Collections.emptyList();
    }

    @Override
    public Optional<VehicleSubcategoryDTO> getVehicleSubcategoryById(UUID id, UUID organizationId) {
        // À implémenter lorsque VehicleSubcategoryService sera disponible
        return Optional.empty();
    }

    @Override
    public Optional<VehicleSubcategoryDTO> getVehicleSubcategoryByCode(String code, UUID organizationId) {
        // À implémenter lorsque VehicleSubcategoryService sera disponible
        return Optional.empty();
    }

    @Override
    public List<VehicleMakeDTO> getActiveVehicleMakes(UUID organizationId) {
        // À implémenter lorsque VehicleMakeService sera disponible
        return Collections.emptyList();
    }

    @Override
    public Optional<VehicleMakeDTO> getVehicleMakeById(UUID id, UUID organizationId) {
        // À implémenter lorsque VehicleMakeService sera disponible
        return Optional.empty();
    }

    @Override
    public Optional<VehicleMakeDTO> getVehicleMakeByCode(String code, UUID organizationId) {
        // À implémenter lorsque VehicleMakeService sera disponible
        return Optional.empty();
    }

    @Override
    public List<VehicleModelDTO> getActiveVehicleModels(UUID organizationId) {
        // À implémenter lorsque VehicleModelService sera disponible
        return Collections.emptyList();
    }

    @Override
    public List<VehicleModelDTO> getActiveVehicleModelsByMake(UUID makeId, UUID organizationId) {
        // À implémenter lorsque VehicleModelService sera disponible
        return Collections.emptyList();
    }

    @Override
    public Optional<VehicleModelDTO> getVehicleModelById(UUID id, UUID organizationId) {
        // À implémenter lorsque VehicleModelService sera disponible
        return Optional.empty();
    }

    @Override
    public List<VehicleUsageDTO> getActiveVehicleUsages(UUID organizationId) {
        // À implémenter lorsque VehicleUsageService sera disponible
        return Collections.emptyList();
    }

    @Override
    public Optional<VehicleUsageDTO> getVehicleUsageById(UUID id, UUID organizationId) {
        // À implémenter lorsque VehicleUsageService sera disponible
        return Optional.empty();
    }

    @Override
    public Optional<VehicleUsageDTO> getVehicleUsageByCode(String code, UUID organizationId) {
        // À implémenter lorsque VehicleUsageService sera disponible
        return Optional.empty();
    }

    @Override
    public List<GeographicZoneDTO> getActiveGeographicZones(UUID organizationId) {
        // À implémenter lorsque GeographicZoneService sera disponible
        return Collections.emptyList();
    }

    @Override
    public Optional<GeographicZoneDTO> getGeographicZoneById(UUID id, UUID organizationId) {
        // À implémenter lorsque GeographicZoneService sera disponible
        return Optional.empty();
    }

    @Override
    public Optional<GeographicZoneDTO> getGeographicZoneByCode(String code, UUID organizationId) {
        // À implémenter lorsque GeographicZoneService sera disponible
        return Optional.empty();
    }

    @Override
    public List<VehicleBodyTypeDTO> getActiveVehicleBodyTypes(UUID organizationId) {
        // À implémenter lorsque VehicleBodyTypeService sera disponible
        return Collections.emptyList();
    }

    @Override
    public Optional<VehicleBodyTypeDTO> getVehicleBodyTypeById(UUID id, UUID organizationId) {
        // À implémenter lorsque VehicleBodyTypeService sera disponible
        return Optional.empty();
    }

    @Override
    public Optional<VehicleBodyTypeDTO> getVehicleBodyTypeByCode(String code, UUID organizationId) {
        // À implémenter lorsque VehicleBodyTypeService sera disponible
        return Optional.empty();
    }

    @Override
    public List<VehicleGenreDTO> getActiveVehicleGenres(UUID organizationId) {
        // À implémenter lorsque VehicleGenreService sera disponible
        return Collections.emptyList();
    }

    @Override
    public Optional<VehicleGenreDTO> getVehicleGenreById(UUID id, UUID organizationId) {
        // À implémenter lorsque VehicleGenreService sera disponible
        return Optional.empty();
    }

    @Override
    public Optional<VehicleGenreDTO> getVehicleGenreByCode(String code, UUID organizationId) {
        // À implémenter lorsque VehicleGenreService sera disponible
        return Optional.empty();
    }

    @Override
    public List<CirculationZoneDTO> getActiveCirculationZones(UUID organizationId) {
        // À implémenter lorsque CirculationZoneService sera disponible
        return Collections.emptyList();
    }

    @Override
    public Optional<CirculationZoneDTO> getCirculationZoneById(UUID id, UUID organizationId) {
        // À implémenter lorsque CirculationZoneService sera disponible
        return Optional.empty();
    }

    @Override
    public Optional<CirculationZoneDTO> getCirculationZoneByCode(String code, UUID organizationId) {
        // À implémenter lorsque CirculationZoneService sera disponible
        return Optional.empty();
    }

    @Override
    public List<VehicleColorDTO> getActiveVehicleColors(UUID organizationId) {
        // À implémenter lorsque VehicleColorService sera disponible
        return Collections.emptyList();
    }

    @Override
    public Optional<VehicleColorDTO> getVehicleColorById(UUID id, UUID organizationId) {
        // À implémenter lorsque VehicleColorService sera disponible
        return Optional.empty();
    }

    @Override
    public Optional<VehicleColorDTO> getVehicleColorByCode(String code, UUID organizationId) {
        // À implémenter lorsque VehicleColorService sera disponible
        return Optional.empty();
    }

    @Override
    public void initializeStandardReferenceData(UUID organizationId) {
        // Initialisation des types de carburant standard
        initializeStandardFuelTypes(organizationId);

        // Initialisation des catégories de véhicule standard
        initializeStandardVehicleCategories(organizationId);

        // Les autres initialisations seront ajoutées au fur et à mesure de l'implémentation des services correspondants
    }

    @Override
    public boolean referenceExists(String referenceType, UUID referenceId, UUID organizationId) {
        switch (referenceType) {
            case "FUEL_TYPE":
                return fuelTypeService.getFuelTypeById(referenceId, organizationId).isPresent();
            case "VEHICLE_CATEGORY":
                return vehicleCategoryService.getVehicleCategoryById(referenceId, organizationId).isPresent();
            // Les autres types de référence seront ajoutés au fur et à mesure de l'implémentation des services correspondants
            default:
                return false;
        }
    }

    @Override
    public Map<String, List<?>> getReferenceDataForUI(UUID organizationId) {
        Map<String, List<?>> referenceData = new HashMap<>();

        // Ajout des types de carburant
        referenceData.put("fuelTypes", fuelTypeService.getAllActiveFuelTypes(organizationId));

        // Ajout des catégories de véhicule
        referenceData.put("vehicleCategories", vehicleCategoryService.getAllActiveVehicleCategories(organizationId));

        // Les autres données de référence seront ajoutées au fur et à mesure de l'implémentation des services correspondants

        return referenceData;
    }

    /**
     * Initialise les types de carburant standard.
     *
     * @param organizationId L'ID de l'organisation
     */
    private void initializeStandardFuelTypes(UUID organizationId) {
        List<FuelType> standardFuelTypes = Arrays.asList(
                FuelType.builder()
                        .code("PETROL")
                        .name("Essence")
                        .description("Carburant essence standard")
                        .isActive(true)
                        .organizationId(organizationId)
                        .build(),
                FuelType.builder()
                        .code("DIESEL")
                        .name("Diesel")
                        .description("Carburant diesel standard")
                        .isActive(true)
                        .organizationId(organizationId)
                        .build(),
                FuelType.builder()
                        .code("ELECTRIC")
                        .name("Électrique")
                        .description("Véhicule 100% électrique")
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

    /**
     * Initialise les catégories de véhicule standard.
     *
     * @param organizationId L'ID de l'organisation
     */
    private void initializeStandardVehicleCategories(UUID organizationId) {
        List<VehicleCategory> standardCategories = Arrays.asList(
                VehicleCategory.builder()
                        .code("CAT1")
                        .name("Véhicules de tourisme")
                        .description("Véhicules particuliers et commerciaux à usage privé")
                        .tariffCoefficient(BigDecimal.valueOf(1.00))
                        .isActive(true)
                        .organizationId(organizationId)
                        .build(),
                VehicleCategory.builder()
                        .code("CAT2")
                        .name("Véhicules de transport de marchandises")
                        .description("Camions, camionnettes et fourgonnettes")
                        .tariffCoefficient(BigDecimal.valueOf(1.50))
                        .isActive(true)
                        .organizationId(organizationId)
                        .build(),
                VehicleCategory.builder()
                        .code("CAT3")
                        .name("Véhicules de transport en commun")
                        .description("Autobus, autocars et minibus")
                        .tariffCoefficient(BigDecimal.valueOf(1.75))
                        .isActive(true)
                        .organizationId(organizationId)
                        .build(),
                VehicleCategory.builder()
                        .code("CAT4")
                        .name("Engins de chantier")
                        .description("Tracteurs, bulldozers et autres engins de chantier")
                        .tariffCoefficient(BigDecimal.valueOf(2.00))
                        .isActive(true)
                        .organizationId(organizationId)
                        .build(),
                VehicleCategory.builder()
                        .code("CAT5")
                        .name("Véhicules agricoles")
                        .description("Tracteurs agricoles et machines agricoles automotrices")
                        .tariffCoefficient(BigDecimal.valueOf(1.25))
                        .isActive(true)
                        .organizationId(organizationId)
                        .build(),
                VehicleCategory.builder()
                        .code("CAT6")
                        .name("Deux-roues")
                        .description("Motocyclettes, scooters et cyclomoteurs")
                        .tariffCoefficient(BigDecimal.valueOf(1.80))
                        .isActive(true)
                        .organizationId(organizationId)
                        .build(),
                VehicleCategory.builder()
                        .code("CAT7")
                        .name("Remorques")
                        .description("Remorques et semi-remorques")
                        .tariffCoefficient(BigDecimal.valueOf(0.75))
                        .isActive(true)
                        .organizationId(organizationId)
                        .build()
        );

        // Sauvegarde des catégories de véhicule standard
        for (VehicleCategory category : standardCategories) {
            // Vérifier si la catégorie existe déjà
            if (vehicleCategoryService.getVehicleCategoryByCode(category.getCode(), organizationId).isEmpty()) {
                vehicleCategoryService.createVehicleCategory(category, organizationId);
            }
        }
    }
}
