package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.AbstractReferenceController;
import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleColorDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleColorService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleColor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des couleurs de véhicule.
 */
@RestController
@RequestMapping("/api/v1/auto/reference/vehicle-colors")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Colors", description = "API pour la gestion des couleurs de véhicule")
public class VehicleColorReferenceController extends AbstractReferenceController<VehicleColorDTO, VehicleColor, VehicleColor> {

    private final VehicleColorService vehicleColorService;

    @Override
    protected List<VehicleColorDTO> getAllActive(UUID organizationId) {
        return vehicleColorService.getAllActiveVehicleColors(organizationId);
    }

    @Override
    protected VehicleColorDTO getById(UUID id, UUID organizationId) {
        return vehicleColorService.getVehicleColorById(id, organizationId)
                .orElseThrow(() -> new RuntimeException("Couleur de véhicule non trouvée avec ID: " + id));
    }

    @Override
    protected VehicleColorDTO create(VehicleColor command, UUID organizationId) {
        return vehicleColorService.createVehicleColor(command, organizationId);
    }

    @Override
    protected VehicleColorDTO update(UUID id, VehicleColor command, UUID organizationId) {
        return vehicleColorService.updateVehicleColor(id, command, organizationId)
                .orElseThrow(() -> new RuntimeException("Couleur de véhicule non trouvée avec ID: " + id));
    }

    @Override
    protected VehicleColorDTO setActive(UUID id, boolean active, UUID organizationId) {
        // Cette méthode n'est pas implémentée dans le service existant
        // Nous devons l'implémenter ou lancer une exception
        throw new UnsupportedOperationException("Méthode non implémentée");
    }

    @Override
    protected String getEntityName() {
        return "couleur de véhicule";
    }

    /**
     * Récupère toutes les couleurs de véhicule (actives et inactives).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des couleurs de véhicule
     */
    @GetMapping("/all")
    @Operation(summary = "Récupère toutes les couleurs de véhicule (actives et inactives)")
    @Auditable(action = "API_GET_ALL_VEHICLE_COLORS")
    @TenantRequired
    public ResponseEntity<List<VehicleColorDTO>> getAllVehicleColors(@RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer toutes les couleurs de véhicule pour l'organisation: {}", organizationId);
        List<VehicleColorDTO> colors = vehicleColorService.getAllVehicleColors(organizationId);
        return ResponseEntity.ok(colors);
    }

    /**
     * Récupère une couleur de véhicule par son code.
     *
     * @param code           Le code de la couleur de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La couleur de véhicule trouvée, ou 404 si non trouvée
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Récupère une couleur de véhicule par son code")
    @Auditable(action = "API_GET_VEHICLE_COLOR_BY_CODE")
    @TenantRequired
    public ResponseEntity<VehicleColorDTO> getVehicleColorByCode(
            @PathVariable String code,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer la couleur de véhicule avec code: {} pour l'organisation: {}",
                code, organizationId);
        return vehicleColorService.getVehicleColorByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
