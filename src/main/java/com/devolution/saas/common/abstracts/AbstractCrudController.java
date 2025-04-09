package com.devolution.saas.common.abstracts;

import com.devolution.saas.common.annotation.Auditable;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur générique pour les opérations CRUD.
 *
 * @param <T>  Type de l'entité DTO
 * @param <ID> Type de l'identifiant
 * @param <C>  Type de la commande de création
 * @param <U>  Type de la commande de mise à jour
 */
@Slf4j
public abstract class AbstractCrudController<T, ID, C, U> {

    /**
     * Crée une nouvelle entité.
     *
     * @param command Commande de création
     * @return Entité créée
     */
    protected abstract T create(C command);

    /**
     * Met à jour une entité existante.
     *
     * @param id      ID de l'entité
     * @param command Commande de mise à jour
     * @return Entité mise à jour
     */
    protected abstract T update(ID id, U command);

    /**
     * Récupère une entité par son ID.
     *
     * @param id ID de l'entité
     * @return Entité récupérée
     */
    protected abstract T get(ID id);

    /**
     * Liste toutes les entités.
     *
     * @return Liste des entités
     */
    protected abstract List<T> list();

    /**
     * Supprime une entité.
     *
     * @param id ID de l'entité
     */
    protected abstract void delete(ID id);

    /**
     * Retourne le nom de l'entité pour les logs.
     *
     * @return Nom de l'entité
     */
    protected abstract String getEntityName();

    /**
     * Endpoint pour créer une nouvelle entité.
     *
     * @param command Commande de création
     * @return Réponse HTTP avec l'entité créée
     */
    @PostMapping
    @Operation(summary = "Crée un nouvel élément")
    @Auditable(action = "API_CREATE")
    public ResponseEntity<T> createEntity(@Valid @RequestBody C command) {
        log.debug("REST request pour créer un {}: {}", getEntityName(), command);
        T result = create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Endpoint pour mettre à jour une entité existante.
     *
     * @param id      ID de l'entité
     * @param command Commande de mise à jour
     * @return Réponse HTTP avec l'entité mise à jour
     */
    @PutMapping("/{id}")
    @Operation(summary = "Met à jour un élément existant")
    @Auditable(action = "API_UPDATE")
    public ResponseEntity<T> updateEntity(@PathVariable ID id, @Valid @RequestBody U command) {
        log.debug("REST request pour mettre à jour le {}: {}", getEntityName(), command);

        if (!isValidId(id, command)) {
            return ResponseEntity.badRequest().build();
        }

        T result = update(id, command);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint pour récupérer une entité par son ID.
     *
     * @param id ID de l'entité
     * @return Réponse HTTP avec l'entité récupérée
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupère un élément par son ID")
    @Auditable(action = "API_GET")
    public ResponseEntity<T> getEntity(@PathVariable ID id) {
        log.debug("REST request pour récupérer le {}: {}", getEntityName(), id);
        T result = get(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint pour lister toutes les entités.
     *
     * @return Réponse HTTP avec la liste des entités
     */
    @GetMapping
    @Operation(summary = "Liste tous les éléments")
    @Auditable(action = "API_LIST")
    public ResponseEntity<List<T>> listEntities() {
        log.debug("REST request pour lister les {}", getEntityName());
        List<T> result = list();
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint pour supprimer une entité.
     *
     * @param id ID de l'entité
     * @return Réponse HTTP vide
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime un élément")
    @Auditable(action = "API_DELETE")
    public ResponseEntity<Void> deleteEntity(@PathVariable ID id) {
        log.debug("REST request pour supprimer le {}: {}", getEntityName(), id);
        delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Vérifie si l'ID dans l'URL correspond à l'ID dans la commande.
     * À surcharger dans les classes concrètes si nécessaire.
     *
     * @param id      ID dans l'URL
     * @param command Commande de mise à jour
     * @return true si l'ID est valide, false sinon
     */
    protected boolean isValidId(ID id, U command) {
        return true;
    }
}
