package com.devolution.saas.common.abstracts;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur abstrait pour les données de référence.
 * Fournit des endpoints standards pour la gestion des données de référence.
 *
 * @param <T> Type du DTO
 * @param <C> Type de la commande de création
 * @param <U> Type de la commande de mise à jour
 */
@Slf4j
public abstract class AbstractReferenceController<T, C, U> {

    /**
     * Récupère toutes les données de référence actives pour une organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des données de référence actives
     */
    protected abstract List<T> getAllActive(UUID organizationId);

    /**
     * Récupère une donnée de référence par son ID.
     *
     * @param id             ID de la donnée de référence
     * @param organizationId ID de l'organisation
     * @return Donnée de référence
     */
    protected abstract T getById(UUID id, UUID organizationId);

    /**
     * Crée une nouvelle donnée de référence.
     *
     * @param command        Commande de création
     * @param organizationId ID de l'organisation
     * @return Donnée de référence créée
     */
    protected abstract T create(C command, UUID organizationId);

    /**
     * Met à jour une donnée de référence existante.
     *
     * @param id             ID de la donnée de référence
     * @param command        Commande de mise à jour
     * @param organizationId ID de l'organisation
     * @return Donnée de référence mise à jour
     */
    protected abstract T update(UUID id, U command, UUID organizationId);

    /**
     * Active ou désactive une donnée de référence.
     *
     * @param id             ID de la donnée de référence
     * @param active         Statut d'activation
     * @param organizationId ID de l'organisation
     * @return Donnée de référence mise à jour
     */
    protected abstract T setActive(UUID id, boolean active, UUID organizationId);

    /**
     * Retourne le nom de l'entité pour les logs.
     *
     * @return Nom de l'entité
     */
    protected abstract String getEntityName();

    /**
     * Endpoint pour récupérer toutes les données de référence actives.
     *
     * @param organizationId ID de l'organisation
     * @return Réponse HTTP avec la liste des données de référence actives
     */
    @GetMapping
    @Operation(summary = "Récupère toutes les données de référence actives")
    public ResponseEntity<List<T>> getAllActiveEntities(@RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer tous les {} actifs pour l'organisation: {}",
                getEntityName(), organizationId);
        List<T> result = getAllActive(organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint pour récupérer une donnée de référence par son ID.
     *
     * @param id             ID de la donnée de référence
     * @param organizationId ID de l'organisation
     * @return Réponse HTTP avec la donnée de référence
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupère une donnée de référence par son ID")
    public ResponseEntity<T> getEntityById(@PathVariable UUID id, @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le {} avec ID: {} pour l'organisation: {}",
                getEntityName(), id, organizationId);
        T result = getById(id, organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint pour créer une nouvelle donnée de référence.
     *
     * @param command        Commande de création
     * @param organizationId ID de l'organisation
     * @return Réponse HTTP avec la donnée de référence créée
     */
    @PostMapping
    @Operation(summary = "Crée une nouvelle donnée de référence")
    public ResponseEntity<T> createEntity(@Valid @RequestBody C command, @RequestParam UUID organizationId) {
        log.debug("REST request pour créer un nouveau {} pour l'organisation: {}",
                getEntityName(), organizationId);
        T result = create(command, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Endpoint pour mettre à jour une donnée de référence existante.
     *
     * @param id             ID de la donnée de référence
     * @param command        Commande de mise à jour
     * @param organizationId ID de l'organisation
     * @return Réponse HTTP avec la donnée de référence mise à jour
     */
    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une donnée de référence existante")
    public ResponseEntity<T> updateEntity(@PathVariable UUID id, @Valid @RequestBody U command,
                                          @RequestParam UUID organizationId) {
        log.debug("REST request pour mettre à jour le {} avec ID: {} pour l'organisation: {}",
                getEntityName(), id, organizationId);
        T result = update(id, command, organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint pour activer une donnée de référence.
     *
     * @param id             ID de la donnée de référence
     * @param organizationId ID de l'organisation
     * @return Réponse HTTP avec la donnée de référence activée
     */
    @PutMapping("/{id}/activate")
    @Operation(summary = "Active une donnée de référence")
    public ResponseEntity<T> activateEntity(@PathVariable UUID id, @RequestParam UUID organizationId) {
        log.debug("REST request pour activer le {} avec ID: {} pour l'organisation: {}",
                getEntityName(), id, organizationId);
        T result = setActive(id, true, organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint pour désactiver une donnée de référence.
     *
     * @param id             ID de la donnée de référence
     * @param organizationId ID de l'organisation
     * @return Réponse HTTP avec la donnée de référence désactivée
     */
    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Désactive une donnée de référence")
    public ResponseEntity<T> deactivateEntity(@PathVariable UUID id, @RequestParam UUID organizationId) {
        log.debug("REST request pour désactiver le {} avec ID: {} pour l'organisation: {}",
                getEntityName(), id, organizationId);
        T result = setActive(id, false, organizationId);
        return ResponseEntity.ok(result);
    }
}
