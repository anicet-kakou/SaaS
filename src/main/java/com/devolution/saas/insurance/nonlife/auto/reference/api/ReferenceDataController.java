package com.devolution.saas.insurance.nonlife.auto.reference.api;

import com.devolution.saas.insurance.nonlife.auto.reference.application.service.ReferenceDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des données de référence.
 */
@RestController
@RequestMapping("/api/v1/auto/reference")
@RequiredArgsConstructor
public class ReferenceDataController {

    private final ReferenceDataService referenceDataService;

    /**
     * Récupère toutes les données de référence pour l'interface utilisateur.
     *
     * @param organizationId L'ID de l'organisation
     * @return Les données de référence
     */
    @GetMapping
    public ResponseEntity<Map<String, List<?>>> getReferenceData(@RequestParam UUID organizationId) {
        Map<String, List<?>> referenceData = referenceDataService.getReferenceDataForUI(organizationId);
        return ResponseEntity.ok(referenceData);
    }

    /**
     * Initialise les données de référence standard pour une nouvelle organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return Réponse vide avec statut 204
     */
    @PostMapping("/initialize")
    public ResponseEntity<Void> initializeReferenceData(@RequestParam UUID organizationId) {
        referenceDataService.initializeStandardReferenceData(organizationId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Vérifie si une référence existe.
     *
     * @param referenceType  Le type de référence
     * @param referenceId    L'ID de la référence
     * @param organizationId L'ID de l'organisation
     * @return true si la référence existe, false sinon
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> referenceExists(@RequestParam String referenceType,
                                                   @RequestParam UUID referenceId,
                                                   @RequestParam UUID organizationId) {
        boolean exists = referenceDataService.referenceExists(referenceType, referenceId, organizationId);
        return ResponseEntity.ok(exists);
    }
}
