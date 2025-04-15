package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.common.abstracts.TenantAwareCrudController;
import com.devolution.saas.common.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleModelDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleModelService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des modèles de véhicule.
 */
// @RestController - Disabled to avoid ambiguous mapping issues
// @RequestMapping("/api/v1/auto/reference/vehicle-models")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vehicle Models", description = "API pour la gestion des modèles de véhicule (Deprecated)")
public class VehicleModelReferenceController extends TenantAwareCrudController<VehicleModelDTO, VehicleModel, VehicleModel> {

    private final VehicleModelService vehicleModelService;

    @Override
    protected List<VehicleModelDTO> listActive(UUID organizationId) {
        return vehicleModelService.getAllActiveVehicleModels(organizationId);
    }

    @Override
    protected List<VehicleModelDTO> list(UUID organizationId) {
        return vehicleModelService.getAllVehicleModels(organizationId);
    }

    @Override
    protected VehicleModelDTO get(UUID id, UUID organizationId) {
        return vehicleModelService.getVehicleModelById(id, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Modèle de véhicule", id));
    }

    @Override
    protected VehicleModelDTO getByCode(String code, UUID organizationId) {
        return vehicleModelService.getVehicleModelByCode(code, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forCode("Modèle de véhicule", code));
    }

    @Override
    protected VehicleModelDTO create(VehicleModel command, UUID organizationId) {
        return vehicleModelService.createVehicleModel(command, organizationId);
    }

    @Override
    protected VehicleModelDTO update(UUID id, VehicleModel command, UUID organizationId) {
        return vehicleModelService.updateVehicleModel(id, command, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Modèle de véhicule", id));
    }

    @Override
    protected VehicleModelDTO setActive(UUID id, boolean active, UUID organizationId) {
        return vehicleModelService.setActive(id, active, organizationId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Modèle de véhicule", id));
    }

    @Override
    protected void delete(UUID id, UUID organizationId) {
        boolean deleted = vehicleModelService.deleteVehicleModel(id, organizationId);
        if (!deleted) {
            throw ResourceNotFoundException.forId("Modèle de véhicule", id);
        }
    }

    @Override
    protected String getEntityName() {
        return "modèle de véhicule";
    }

    /**
     * Récupère tous les modèles de véhicule actifs pour une marque donnée.
     *
     * @param makeId         L'ID de la marque
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule actifs pour la marque
     */
    @GetMapping("/make/{makeId}")
    @Operation(summary = "Récupère tous les modèles de véhicule actifs pour une marque")
    public ResponseEntity<List<VehicleModelDTO>> getAllActiveVehicleModelsByMake(
            @PathVariable UUID makeId,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer les modèles de véhicule actifs pour la marque: {} et l'organisation: {}",
                makeId, organizationId);
        List<VehicleModelDTO> models = vehicleModelService.getAllActiveVehicleModelsByMake(makeId, organizationId);
        return ResponseEntity.ok(models);
    }

    /**
     * Récupère tous les modèles de véhicule (actifs et inactifs) pour une marque donnée.
     *
     * @param makeId         L'ID de la marque
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule pour la marque
     */
    @GetMapping("/make/{makeId}/all")
    @Operation(summary = "Récupère tous les modèles de véhicule pour une marque")
    public ResponseEntity<List<VehicleModelDTO>> getAllVehicleModelsByMake(
            @PathVariable UUID makeId,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer tous les modèles de véhicule pour la marque: {} et l'organisation: {}",
                makeId, organizationId);
        List<VehicleModelDTO> models = vehicleModelService.getAllVehicleModelsByMake(makeId, organizationId);
        return ResponseEntity.ok(models);
    }

    /**
     * Récupère un modèle de véhicule par son code et sa marque.
     *
     * @param code           Le code du modèle de véhicule
     * @param makeId         L'ID de la marque
     * @param organizationId L'ID de l'organisation
     * @return Le modèle de véhicule trouvé, ou 404 si non trouvé
     */
    @GetMapping("/code/{code}/make/{makeId}")
    @Operation(summary = "Récupère un modèle de véhicule par son code et sa marque")
    public ResponseEntity<VehicleModelDTO> getVehicleModelByCodeAndMake(
            @PathVariable String code,
            @PathVariable UUID makeId,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le modèle de véhicule avec code: {} pour la marque: {} et l'organisation: {}",
                code, makeId, organizationId);
        return vehicleModelService.getVehicleModelByCodeAndMake(code, makeId, organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
