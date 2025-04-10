package com.devolution.saas.insurance.nonlife.auto.reference.application.service;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des données de référence.
 */
public interface ReferenceDataService {

    /**
     * Récupère tous les types de carburant actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carburant actifs
     */
    List<FuelTypeDTO> getActiveFuelTypes(UUID organizationId);

    /**
     * Récupère un type de carburant par son ID.
     *
     * @param id             L'ID du type de carburant
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant trouvé, ou empty si non trouvé
     */
    Optional<FuelTypeDTO> getFuelTypeById(UUID id, UUID organizationId);

    /**
     * Récupère un type de carburant par son code.
     *
     * @param code           Le code du type de carburant
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant trouvé, ou empty si non trouvé
     */
    Optional<FuelTypeDTO> getFuelTypeByCode(String code, UUID organizationId);

    /**
     * Récupère toutes les catégories de véhicule actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des catégories de véhicule actives
     */
    List<VehicleCategoryDTO> getActiveVehicleCategories(UUID organizationId);

    /**
     * Récupère une catégorie de véhicule par son ID.
     *
     * @param id             L'ID de la catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La catégorie de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleCategoryDTO> getVehicleCategoryById(UUID id, UUID organizationId);

    /**
     * Récupère une catégorie de véhicule par son code.
     *
     * @param code           Le code de la catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La catégorie de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleCategoryDTO> getVehicleCategoryByCode(String code, UUID organizationId);

    /**
     * Récupère toutes les sous-catégories de véhicule actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des sous-catégories de véhicule actives
     */
    List<VehicleSubcategoryDTO> getActiveVehicleSubcategories(UUID organizationId);

    /**
     * Récupère toutes les sous-catégories de véhicule actives pour une catégorie donnée.
     *
     * @param categoryId     L'ID de la catégorie
     * @param organizationId L'ID de l'organisation
     * @return La liste des sous-catégories de véhicule actives pour la catégorie
     */
    List<VehicleSubcategoryDTO> getActiveVehicleSubcategoriesByCategory(UUID categoryId, UUID organizationId);

    /**
     * Récupère une sous-catégorie de véhicule par son ID.
     *
     * @param id             L'ID de la sous-catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La sous-catégorie de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleSubcategoryDTO> getVehicleSubcategoryById(UUID id, UUID organizationId);

    /**
     * Récupère une sous-catégorie de véhicule par son code.
     *
     * @param code           Le code de la sous-catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La sous-catégorie de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleSubcategoryDTO> getVehicleSubcategoryByCode(String code, UUID organizationId);

    /**
     * Récupère toutes les marques de véhicule actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des marques de véhicule actives
     */
    List<VehicleMakeDTO> getActiveVehicleMakes(UUID organizationId);

    /**
     * Récupère une marque de véhicule par son ID.
     *
     * @param id             L'ID de la marque de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La marque de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleMakeDTO> getVehicleMakeById(UUID id, UUID organizationId);

    /**
     * Récupère une marque de véhicule par son code.
     *
     * @param code           Le code de la marque de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La marque de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleMakeDTO> getVehicleMakeByCode(String code, UUID organizationId);

    /**
     * Récupère tous les modèles de véhicule actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule actifs
     */
    List<VehicleModelDTO> getActiveVehicleModels(UUID organizationId);

    /**
     * Récupère tous les modèles de véhicule actifs pour une marque donnée.
     *
     * @param makeId         L'ID de la marque
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule actifs pour la marque
     */
    List<VehicleModelDTO> getActiveVehicleModelsByMake(UUID makeId, UUID organizationId);

    /**
     * Récupère un modèle de véhicule par son ID.
     *
     * @param id             L'ID du modèle de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le modèle de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleModelDTO> getVehicleModelById(UUID id, UUID organizationId);

    /**
     * Récupère tous les types d'usage de véhicule actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types d'usage de véhicule actifs
     */
    List<VehicleUsageDTO> getActiveVehicleUsages(UUID organizationId);

    /**
     * Récupère un type d'usage de véhicule par son ID.
     *
     * @param id             L'ID du type d'usage de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type d'usage de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleUsageDTO> getVehicleUsageById(UUID id, UUID organizationId);

    /**
     * Récupère un type d'usage de véhicule par son code.
     *
     * @param code           Le code du type d'usage de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type d'usage de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleUsageDTO> getVehicleUsageByCode(String code, UUID organizationId);

    /**
     * Récupère toutes les zones géographiques actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones géographiques actives
     */
    List<GeographicZoneDTO> getActiveGeographicZones(UUID organizationId);

    /**
     * Récupère une zone géographique par son ID.
     *
     * @param id             L'ID de la zone géographique
     * @param organizationId L'ID de l'organisation
     * @return La zone géographique trouvée, ou empty si non trouvée
     */
    Optional<GeographicZoneDTO> getGeographicZoneById(UUID id, UUID organizationId);

    /**
     * Récupère une zone géographique par son code.
     *
     * @param code           Le code de la zone géographique
     * @param organizationId L'ID de l'organisation
     * @return La zone géographique trouvée, ou empty si non trouvée
     */
    Optional<GeographicZoneDTO> getGeographicZoneByCode(String code, UUID organizationId);

    /**
     * Récupère tous les types de carrosserie actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carrosserie actifs
     */
    List<VehicleBodyTypeDTO> getActiveVehicleBodyTypes(UUID organizationId);

    /**
     * Récupère un type de carrosserie par son ID.
     *
     * @param id             L'ID du type de carrosserie
     * @param organizationId L'ID de l'organisation
     * @return Le type de carrosserie trouvé, ou empty si non trouvé
     */
    Optional<VehicleBodyTypeDTO> getVehicleBodyTypeById(UUID id, UUID organizationId);

    /**
     * Récupère un type de carrosserie par son code.
     *
     * @param code           Le code du type de carrosserie
     * @param organizationId L'ID de l'organisation
     * @return Le type de carrosserie trouvé, ou empty si non trouvé
     */
    Optional<VehicleBodyTypeDTO> getVehicleBodyTypeByCode(String code, UUID organizationId);

    /**
     * Récupère tous les genres de véhicule actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des genres de véhicule actifs
     */
    List<VehicleGenreDTO> getActiveVehicleGenres(UUID organizationId);

    /**
     * Récupère un genre de véhicule par son ID.
     *
     * @param id             L'ID du genre de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le genre de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleGenreDTO> getVehicleGenreById(UUID id, UUID organizationId);

    /**
     * Récupère un genre de véhicule par son code.
     *
     * @param code           Le code du genre de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le genre de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleGenreDTO> getVehicleGenreByCode(String code, UUID organizationId);

    /**
     * Récupère toutes les zones de circulation actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones de circulation actives
     */
    List<CirculationZoneDTO> getActiveCirculationZones(UUID organizationId);

    /**
     * Récupère une zone de circulation par son ID.
     *
     * @param id             L'ID de la zone de circulation
     * @param organizationId L'ID de l'organisation
     * @return La zone de circulation trouvée, ou empty si non trouvée
     */
    Optional<CirculationZoneDTO> getCirculationZoneById(UUID id, UUID organizationId);

    /**
     * Récupère une zone de circulation par son code.
     *
     * @param code           Le code de la zone de circulation
     * @param organizationId L'ID de l'organisation
     * @return La zone de circulation trouvée, ou empty si non trouvée
     */
    Optional<CirculationZoneDTO> getCirculationZoneByCode(String code, UUID organizationId);

    /**
     * Récupère toutes les couleurs de véhicule actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des couleurs de véhicule actives
     */
    List<VehicleColorDTO> getActiveVehicleColors(UUID organizationId);

    /**
     * Récupère une couleur de véhicule par son ID.
     *
     * @param id             L'ID de la couleur de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La couleur de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleColorDTO> getVehicleColorById(UUID id, UUID organizationId);

    /**
     * Récupère une couleur de véhicule par son code.
     *
     * @param code           Le code de la couleur de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La couleur de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleColorDTO> getVehicleColorByCode(String code, UUID organizationId);

    /**
     * Initialise les données de référence standard pour une nouvelle organisation.
     *
     * @param organizationId L'ID de l'organisation
     */
    void initializeStandardReferenceData(UUID organizationId);

    /**
     * Vérifie si une référence existe.
     *
     * @param referenceType  Le type de référence
     * @param referenceId    L'ID de la référence
     * @param organizationId L'ID de l'organisation
     * @return true si la référence existe, false sinon
     */
    boolean referenceExists(String referenceType, UUID referenceId, UUID organizationId);

    /**
     * Récupère toutes les données de référence pour l'interface utilisateur.
     *
     * @param organizationId L'ID de l'organisation
     * @return Les données de référence
     */
    Map<String, List<?>> getReferenceDataForUI(UUID organizationId);
}
