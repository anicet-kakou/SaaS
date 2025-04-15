package com.devolution.saas.common.abstracts;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur générique pour les opérations CRUD tenant-aware.
 * Étend AbstractCrudController en ajoutant la gestion des tenants/organisations.
 *
 * @param <T> Type de l'entité DTO
 * @param <C> Type de la commande de création
 * @param <U> Type de la commande de mise à jour
 */
public abstract class TenantAwareCrudController<T, C, U> extends AbstractCrudController<T, UUID, C, U> {

    /**
     * Crée une nouvelle entité pour une organisation.
     *
     * @param command        Commande de création
     * @param organizationId ID de l'organisation
     * @return Entité créée
     */
    protected abstract T create(C command, UUID organizationId);

    /**
     * Met à jour une entité existante pour une organisation.
     *
     * @param id             ID de l'entité
     * @param command        Commande de mise à jour
     * @param organizationId ID de l'organisation
     * @return Entité mise à jour
     */
    protected abstract T update(UUID id, U command, UUID organizationId);

    /**
     * Récupère une entité par son ID pour une organisation.
     *
     * @param id             ID de l'entité
     * @param organizationId ID de l'organisation
     * @return Entité récupérée
     */
    protected abstract T get(UUID id, UUID organizationId);

    /**
     * Liste toutes les entités actives pour une organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des entités actives
     */
    protected abstract List<T> listActive(UUID organizationId);

    /**
     * Liste toutes les entités pour une organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des entités
     */
    protected abstract List<T> list(UUID organizationId);

    /**
     * Active ou désactive une entité.
     *
     * @param id             ID de l'entité
     * @param active         Statut d'activation
     * @param organizationId ID de l'organisation
     * @return Entité mise à jour
     */
    protected abstract T setActive(UUID id, boolean active, UUID organizationId);

    /**
     * Supprime une entité pour une organisation.
     *
     * @param id             ID de l'entité
     * @param organizationId ID de l'organisation
     */
    protected abstract void delete(UUID id, UUID organizationId);

    /**
     * Récupère une entité par son code pour une organisation.
     *
     * @param code           Code de l'entité
     * @param organizationId ID de l'organisation
     * @return Entité récupérée
     */
    protected abstract T getByCode(String code, UUID organizationId);

    /**
     * Implémentation des méthodes abstraites de AbstractCrudController
     * Ces méthodes ne devraient pas être utilisées directement, mais sont nécessaires
     * pour l'héritage. Elles lèvent une exception si elles sont appelées.
     */
    @Override
    protected T create(C command) {
        throw new UnsupportedOperationException("Cette méthode ne devrait pas être appelée directement. Utilisez create(command, organizationId) à la place.");
    }

    @Override
    protected T update(UUID id, U command) {
        throw new UnsupportedOperationException("Cette méthode ne devrait pas être appelée directement. Utilisez update(id, command, organizationId) à la place.");
    }

    @Override
    protected T get(UUID id) {
        throw new UnsupportedOperationException("Cette méthode ne devrait pas être appelée directement. Utilisez get(id, organizationId) à la place.");
    }

    @Override
    protected List<T> list() {
        throw new UnsupportedOperationException("Cette méthode ne devrait pas être appelée directement. Utilisez list(organizationId) à la place.");
    }

    @Override
    protected void delete(UUID id) {
        throw new UnsupportedOperationException("Cette méthode ne devrait pas être appelée directement. Utilisez delete(id, organizationId) à la place.");
    }

    /**
     * Endpoint pour créer une nouvelle entité.
     *
     * @param command        Commande de création
     * @param organizationId ID de l'organisation
     * @return Réponse HTTP avec l'entité créée
     */
    @PostMapping
    @Operation(summary = "Crée un nouvel élément")
    @Auditable(action = "API_CREATE")
    @TenantRequired
    public ResponseEntity<T> createEntity(@Valid @RequestBody C command, @RequestParam UUID organizationId) {
        log.debug("REST request pour créer un {} pour l'organisation: {}", getEntityName(), organizationId);
        T result = create(command, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Endpoint pour mettre à jour une entité existante.
     *
     * @param id             ID de l'entité
     * @param command        Commande de mise à jour
     * @param organizationId ID de l'organisation
     * @return Réponse HTTP avec l'entité mise à jour
     */
    @PutMapping("/{id}")
    @Operation(summary = "Met à jour un élément existant")
    @Auditable(action = "API_UPDATE")
    @TenantRequired
    public ResponseEntity<T> updateEntity(@PathVariable UUID id, @Valid @RequestBody U command, @RequestParam UUID organizationId) {
        log.debug("REST request pour mettre à jour le {} avec ID: {} pour l'organisation: {}",
                getEntityName(), id, organizationId);
        T result = update(id, command, organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint pour récupérer une entité par son ID.
     *
     * @param id             ID de l'entité
     * @param organizationId ID de l'organisation
     * @return Réponse HTTP avec l'entité
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupère un élément par son ID")
    @Auditable(action = "API_GET")
    @TenantRequired
    public ResponseEntity<T> getEntity(@PathVariable UUID id, @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le {} avec ID: {} pour l'organisation: {}",
                getEntityName(), id, organizationId);
        T result = get(id, organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint pour lister toutes les entités actives.
     *
     * @param organizationId ID de l'organisation
     * @return Réponse HTTP avec la liste des entités actives
     */
    @GetMapping
    @Operation(summary = "Liste tous les éléments actifs")
    @Auditable(action = "API_LIST_ACTIVE")
    @TenantRequired
    public ResponseEntity<List<T>> listActiveEntities(@RequestParam UUID organizationId) {
        log.debug("REST request pour lister les {} actifs pour l'organisation: {}",
                getEntityName(), organizationId);
        List<T> result = listActive(organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint pour lister toutes les entités (actives et inactives).
     *
     * @param organizationId ID de l'organisation
     * @return Réponse HTTP avec la liste des entités
     */
    @GetMapping("/all")
    @Operation(summary = "Liste tous les éléments (actifs et inactifs)")
    @Auditable(action = "API_LIST_ALL")
    @TenantRequired
    public ResponseEntity<List<T>> listAllEntities(@RequestParam UUID organizationId) {
        log.debug("REST request pour lister tous les {} pour l'organisation: {}",
                getEntityName(), organizationId);
        List<T> result = list(organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint pour récupérer une entité par son code.
     *
     * @param code           Code de l'entité
     * @param organizationId ID de l'organisation
     * @return Réponse HTTP avec l'entité
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Récupère un élément par son code")
    @Auditable(action = "API_GET_BY_CODE")
    @TenantRequired
    public ResponseEntity<T> getEntityByCode(@PathVariable String code, @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le {} avec code: {} pour l'organisation: {}",
                getEntityName(), code, organizationId);
        T result = getByCode(code, organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint pour activer une entité.
     *
     * @param id             ID de l'entité
     * @param organizationId ID de l'organisation
     * @return Réponse HTTP avec l'entité activée
     */
    @PutMapping("/{id}/activate")
    @Operation(summary = "Active un élément")
    @Auditable(action = "API_ACTIVATE")
    @TenantRequired
    public ResponseEntity<T> activateEntity(@PathVariable UUID id, @RequestParam UUID organizationId) {
        log.debug("REST request pour activer le {} avec ID: {} pour l'organisation: {}",
                getEntityName(), id, organizationId);
        T result = setActive(id, true, organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint pour désactiver une entité.
     *
     * @param id             ID de l'entité
     * @param organizationId ID de l'organisation
     * @return Réponse HTTP avec l'entité désactivée
     */
    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Désactive un élément")
    @Auditable(action = "API_DEACTIVATE")
    @TenantRequired
    public ResponseEntity<T> deactivateEntity(@PathVariable UUID id, @RequestParam UUID organizationId) {
        log.debug("REST request pour désactiver le {} avec ID: {} pour l'organisation: {}",
                getEntityName(), id, organizationId);
        T result = setActive(id, false, organizationId);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint pour supprimer une entité.
     *
     * @param id             ID de l'entité
     * @param organizationId ID de l'organisation
     * @return Réponse HTTP vide
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime un élément")
    @Auditable(action = "API_DELETE")
    @TenantRequired
    public ResponseEntity<Void> deleteEntity(@PathVariable UUID id, @RequestParam UUID organizationId) {
        log.debug("REST request pour supprimer le {} avec ID: {} pour l'organisation: {}",
                getEntityName(), id, organizationId);
        delete(id, organizationId);
        return ResponseEntity.noContent().build();
    }
}
