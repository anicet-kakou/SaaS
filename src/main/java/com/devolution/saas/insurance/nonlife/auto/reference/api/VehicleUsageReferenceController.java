package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.AbstractReferenceController;
import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleUsageDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleUsageService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleUsage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des types d'usage de véhicule.
 */
@RestController
@RequestMapping("/api/v1/auto/reference/vehicle-usages")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Usages", description = "API pour la gestion des types d'usage de véhicule")
public class VehicleUsageReferenceController extends AbstractReferenceController<VehicleUsageDTO, VehicleUsage, VehicleUsage> {

    private final VehicleUsageService vehicleUsageService;

    @Override
    protected List<VehicleUsageDTO> getAllActive(UUID organizationId) {
        return vehicleUsageService.getAllActiveVehicleUsages(organizationId);
    }

    @Override
    protected VehicleUsageDTO getById(UUID id, UUID organizationId) {
        return vehicleUsageService.getVehicleUsageById(id, organizationId)
                .orElseThrow(() -> new RuntimeException("Type d'usage de véhicule non trouvé avec ID: " + id));
    }

    @Override
    protected VehicleUsageDTO create(VehicleUsage command, UUID organizationId) {
        return vehicleUsageService.createVehicleUsage(command, organizationId);
    }

    @Override
    protected VehicleUsageDTO update(UUID id, VehicleUsage command, UUID organizationId) {
        return vehicleUsageService.updateVehicleUsage(id, command, organizationId)
                .orElseThrow(() -> new RuntimeException("Type d'usage de véhicule non trouvé avec ID: " + id));
    }

    @Override
    protected VehicleUsageDTO setActive(UUID id, boolean active, UUID organizationId) {
        // Cette méthode n'est pas implémentée dans le service existant
        // Nous devons l'implémenter ou lancer une exception
        throw new UnsupportedOperationException("Méthode non implémentée");
    }

    @Override
    protected String getEntityName() {
        return "type d'usage de véhicule";
    }

    /**
     * Récupère tous les types d'usage de véhicule (actifs et inactifs).
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types d'usage de véhicule
     */
    @GetMapping("/all")
    @Operation(summary = "Récupère tous les types d'usage de véhicule (actifs et inactifs)")
    @Auditable(action = "API_GET_ALL_VEHICLE_USAGES")
    @TenantRequired
    public ResponseEntity<List<VehicleUsageDTO>> getAllVehicleUsages(@RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer tous les types d'usage de véhicule pour l'organisation: {}", organizationId);
        List<VehicleUsageDTO> usages = vehicleUsageService.getAllVehicleUsages(organizationId);
        return ResponseEntity.ok(usages);
    }

    /**
     * Récupère un type d'usage de véhicule par son code.
     *
     * @param code           Le code du type d'usage de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type d'usage de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Récupère un type d'usage de véhicule par son code")
    @Auditable(action = "API_GET_VEHICLE_USAGE_BY_CODE")
    @TenantRequired
    public ResponseEntity<VehicleUsageDTO> getVehicleUsageByCode(
            @PathVariable String code,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le type d'usage de véhicule avec code: {} pour l'organisation: {}",
                code, organizationId);
        return vehicleUsageService.getVehicleUsageByCode(code, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
