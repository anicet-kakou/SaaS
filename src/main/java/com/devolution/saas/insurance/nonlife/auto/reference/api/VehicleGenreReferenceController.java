package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.AbstractReferenceController;
import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleGenreDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleGenreService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleGenre;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des genres de véhicule.
 */
@RestController
@RequestMapping("/api/v1/auto/reference/vehicle-genres")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Genres", description = "API pour la gestion des genres de véhicule")
public class VehicleGenreReferenceController extends AbstractReferenceController<VehicleGenreDTO, VehicleGenre, VehicleGenre> {

    private final VehicleGenreService vehicleGenreService;

    @Override
    protected List<VehicleGenreDTO> getAllActive(UUID organizationId) {
        return vehicleGenreService.getAllActiveVehicleGenres(organizationId);
    }

    @Override
    protected VehicleGenreDTO getById(UUID id, UUID organizationId) {
        return vehicleGenreService.getVehicleGenreById(id, organizationId)
                .orElseThrow(() -> new RuntimeException("Genre de véhicule non trouvé avec ID: " + id));
    }

    @Override
    protected VehicleGenreDTO create(VehicleGenre command, UUID organizationId) {
        return vehicleGenreService.createVehicleGenre(command, organizationId);
    }

    @Override
    protected VehicleGenreDTO update(UUID id, VehicleGenre command, UUID organizationId) {
        return vehicleGenreService.updateVehicleGenre(id, command, organizationId)
                .orElseThrow(() -> new RuntimeException("Genre de véhicule non trouvé avec ID: " + id));
    }

    @Override
    protected VehicleGenreDTO setActive(UUID id, boolean active, UUID organizationId) {
        // Cette méthode n'est pas implémentée dans le service existant
        // Nous devons l'implémenter ou lancer une exception
        throw new UnsupportedOperationException("Méthode non implémentée");
    }

    @Override
    protected String getEntityName() {
        return "genre de véhicule";
    }

    /**
     * Récupère tous les genres de véhicule (actifs et inactifs).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des genres de véhicule
     */
    @GetMapping("/all")
    @Operation(summary = "Récupère tous les genres de véhicule (actifs et inactifs)")
    @Auditable(action = "API_GET_ALL_VEHICLE_GENRES")
    @TenantRequired
    public ResponseEntity<List<VehicleGenreDTO>> getAllVehicleGenres(@RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer tous les genres de véhicule pour l'organisation: {}", organizationId);
        List<VehicleGenreDTO> genres = vehicleGenreService.getAllVehicleGenres(organizationId);
        return ResponseEntity.ok(genres);
    }

    /**
     * Récupère un genre de véhicule par son code.
     *
     * @param code           Le code du genre de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le genre de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Récupère un genre de véhicule par son code")
    @Auditable(action = "API_GET_VEHICLE_GENRE_BY_CODE")
    @TenantRequired
    public ResponseEntity<VehicleGenreDTO> getVehicleGenreByCode(
            @PathVariable String code,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le genre de véhicule avec code: {} pour l'organisation: {}",
                code, organizationId);
        return vehicleGenreService.getVehicleGenreByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
