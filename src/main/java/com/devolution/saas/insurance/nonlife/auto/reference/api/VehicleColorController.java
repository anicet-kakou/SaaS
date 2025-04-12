package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleColorDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleColorService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleColor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des couleurs de véhicule.
 *
 * @deprecated Utiliser {@link VehicleColorReferenceController} à la place.
 * Ce contrôleur sera supprimé dans une version future.
 */
@Deprecated(since = "1.0.0", forRemoval = true)
@RestController
@RequestMapping("/api/v1/auto/reference/vehicle-colors-management")
@RequiredArgsConstructor
public class VehicleColorController {

    private final VehicleColorService vehicleColorService;

    /**
     * Récupère toutes les couleurs de véhicule actives.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des couleurs de véhicule actives
     */
    @GetMapping
    public ResponseEntity<List<VehicleColorDTO>> getAllActiveVehicleColors(@RequestParam UUID organizationId) {
        List<VehicleColorDTO> colors = vehicleColorService.getAllActiveVehicleColors(organizationId);
        return ResponseEntity.ok(colors);
    }

    /**
     * Récupère toutes les couleurs de véhicule (actives et inactives).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des couleurs de véhicule
     */
    @GetMapping("/all")
    public ResponseEntity<List<VehicleColorDTO>> getAllVehicleColors(@RequestParam UUID organizationId) {
        List<VehicleColorDTO> colors = vehicleColorService.getAllVehicleColors(organizationId);
        return ResponseEntity.ok(colors);
    }

    /**
     * Récupère une couleur de véhicule par son ID.
     *
     * @param id             L'ID de la couleur de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La couleur de véhicule trouvée, ou 404 si non trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleColorDTO> getVehicleColorById(@PathVariable UUID id, @RequestParam UUID organizationId) {
        return vehicleColorService.getVehicleColorById(id, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère une couleur de véhicule par son code.
     *
     * @param code           Le code de la couleur de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La couleur de véhicule trouvée, ou 404 si non trouvée
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<VehicleColorDTO> getVehicleColorByCode(@PathVariable String code, @RequestParam UUID organizationId) {
        return vehicleColorService.getVehicleColorByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée une nouvelle couleur de véhicule.
     *
     * @param vehicleColor   La couleur de véhicule à créer
     * @param organizationId L'ID de l'organisation
     * @return La couleur de véhicule créée
     */
    @PostMapping
    public ResponseEntity<VehicleColorDTO> createVehicleColor(@RequestBody VehicleColor vehicleColor, @RequestParam UUID organizationId) {
        VehicleColorDTO createdColor = vehicleColorService.createVehicleColor(vehicleColor, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdColor);
    }

    /**
     * Met à jour une couleur de véhicule.
     *
     * @param id             L'ID de la couleur de véhicule
     * @param vehicleColor   La couleur de véhicule mise à jour
     * @param organizationId L'ID de l'organisation
     * @return La couleur de véhicule mise à jour, ou 404 si non trouvée
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleColorDTO> updateVehicleColor(@PathVariable UUID id, @RequestBody VehicleColor vehicleColor, @RequestParam UUID organizationId) {
        return vehicleColorService.updateVehicleColor(id, vehicleColor, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime une couleur de véhicule.
     *
     * @param id             L'ID de la couleur de véhicule
     * @param organizationId L'ID de l'organisation
     * @return 204 si la suppression a réussi, 404 si non trouvée
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleColor(@PathVariable UUID id, @RequestParam UUID organizationId) {
        boolean deleted = vehicleColorService.deleteVehicleColor(id, organizationId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
