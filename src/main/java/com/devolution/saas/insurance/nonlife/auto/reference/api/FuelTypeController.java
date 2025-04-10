package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.FuelTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.FuelTypeService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.FuelType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des types de carburant.
 */
@RestController
@RequestMapping("/api/v1/auto/reference/fuel-types")
@RequiredArgsConstructor
public class FuelTypeController {

    private final FuelTypeService fuelTypeService;

    /**
     * Récupère tous les types de carburant actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carburant actifs
     */
    @GetMapping
    public ResponseEntity<List<FuelTypeDTO>> getAllActiveFuelTypes(@RequestParam UUID organizationId) {
        List<FuelTypeDTO> fuelTypes = fuelTypeService.getAllActiveFuelTypes(organizationId);
        return ResponseEntity.ok(fuelTypes);
    }

    /**
     * Récupère tous les types de carburant (actifs et inactifs).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carburant
     */
    @GetMapping("/all")
    public ResponseEntity<List<FuelTypeDTO>> getAllFuelTypes(@RequestParam UUID organizationId) {
        List<FuelTypeDTO> fuelTypes = fuelTypeService.getAllFuelTypes(organizationId);
        return ResponseEntity.ok(fuelTypes);
    }

    /**
     * Récupère un type de carburant par son ID.
     *
     * @param id             L'ID du type de carburant
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant trouvé, ou 404 si non trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<FuelTypeDTO> getFuelTypeById(@PathVariable UUID id, @RequestParam UUID organizationId) {
        return fuelTypeService.getFuelTypeById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère un type de carburant par son code.
     *
     * @param code           Le code du type de carburant
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant trouvé, ou 404 si non trouvé
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<FuelTypeDTO> getFuelTypeByCode(@PathVariable String code, @RequestParam UUID organizationId) {
        return fuelTypeService.getFuelTypeByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouveau type de carburant.
     *
     * @param fuelType       Le type de carburant à créer
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant créé
     */
    @PostMapping
    public ResponseEntity<FuelTypeDTO> createFuelType(@RequestBody FuelType fuelType, @RequestParam UUID organizationId) {
        FuelTypeDTO createdFuelType = fuelTypeService.createFuelType(fuelType, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFuelType);
    }

    /**
     * Met à jour un type de carburant.
     *
     * @param id             L'ID du type de carburant
     * @param fuelType       Le type de carburant mis à jour
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant mis à jour, ou 404 si non trouvé
     */
    @PutMapping("/{id}")
    public ResponseEntity<FuelTypeDTO> updateFuelType(@PathVariable UUID id, @RequestBody FuelType fuelType, @RequestParam UUID organizationId) {
        return fuelTypeService.updateFuelType(id, fuelType, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un type de carburant.
     *
     * @param id             L'ID du type de carburant
     * @param organizationId L'ID de l'organisation
     * @return 204 si la suppression a réussi, 404 si non trouvé
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuelType(@PathVariable UUID id, @RequestParam UUID organizationId) {
        boolean deleted = fuelTypeService.deleteFuelType(id, organizationId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
