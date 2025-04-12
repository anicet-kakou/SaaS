package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleGenreDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleGenreService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleGenre;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des genres de véhicule.
 *
 * @deprecated Utiliser {@link VehicleGenreReferenceController} à la place.
 * Ce contrôleur sera supprimé dans une version future.
 */
@Deprecated(since = "1.0.0", forRemoval = true)
@RestController
@RequestMapping("/api/v1/auto/reference/vehicle-genres-management")
@RequiredArgsConstructor
public class VehicleGenreController {

    private final VehicleGenreService vehicleGenreService;

    /**
     * Récupère tous les genres de véhicule actifs.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des genres de véhicule actifs
     */
    @GetMapping
    public ResponseEntity<List<VehicleGenreDTO>> getAllActiveVehicleGenres(@RequestParam UUID organizationId) {
        List<VehicleGenreDTO> genres = vehicleGenreService.getAllActiveVehicleGenres(organizationId);
        return ResponseEntity.ok(genres);
    }

    /**
     * Récupère tous les genres de véhicule (actifs et inactifs).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des genres de véhicule
     */
    @GetMapping("/all")
    public ResponseEntity<List<VehicleGenreDTO>> getAllVehicleGenres(@RequestParam UUID organizationId) {
        List<VehicleGenreDTO> genres = vehicleGenreService.getAllVehicleGenres(organizationId);
        return ResponseEntity.ok(genres);
    }

    /**
     * Récupère un genre de véhicule par son ID.
     *
     * @param id             L'ID du genre de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le genre de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleGenreDTO> getVehicleGenreById(@PathVariable UUID id, @RequestParam UUID organizationId) {
        return vehicleGenreService.getVehicleGenreById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère un genre de véhicule par son code.
     *
     * @param code           Le code du genre de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le genre de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<VehicleGenreDTO> getVehicleGenreByCode(@PathVariable String code, @RequestParam UUID organizationId) {
        return vehicleGenreService.getVehicleGenreByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouveau genre de véhicule.
     *
     * @param vehicleGenre   Le genre de véhicule à créer
     * @param organizationId L'ID de l'organisation
     * @return Le genre de véhicule créé
     */
    @PostMapping
    public ResponseEntity<VehicleGenreDTO> createVehicleGenre(@RequestBody VehicleGenre vehicleGenre, @RequestParam UUID organizationId) {
        VehicleGenreDTO createdGenre = vehicleGenreService.createVehicleGenre(vehicleGenre, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGenre);
    }

    /**
     * Met à jour un genre de véhicule.
     *
     * @param id             L'ID du genre de véhicule
     * @param vehicleGenre   Le genre de véhicule mis à jour
     * @param organizationId L'ID de l'organisation
     * @return Le genre de véhicule mis à jour, ou 404 si non trouvé
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleGenreDTO> updateVehicleGenre(@PathVariable UUID id, @RequestBody VehicleGenre vehicleGenre, @RequestParam UUID organizationId) {
        return vehicleGenreService.updateVehicleGenre(id, vehicleGenre, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un genre de véhicule.
     *
     * @param id             L'ID du genre de véhicule
     * @param organizationId L'ID de l'organisation
     * @return 204 si la suppression a réussi, 404 si non trouvé
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleGenre(@PathVariable UUID id, @RequestParam UUID organizationId) {
        boolean deleted = vehicleGenreService.deleteVehicleGenre(id, organizationId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
