package com.devolution.saas.common.i18n.admin.api;

import com.devolution.saas.common.i18n.admin.dto.TranslationDTO;
import com.devolution.saas.common.i18n.admin.service.TranslationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des traductions.
 */
@RestController
@RequestMapping("/api/v1/translations")
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;

    /**
     * Récupère une traduction par son ID.
     *
     * @param id ID de la traduction
     * @return Réponse contenant la traduction
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TRANSLATION_READ')")
    public ResponseEntity<TranslationDTO> getTranslationById(@PathVariable Long id) {
        return ResponseEntity.ok(translationService.getTranslationById(id));
    }

    /**
     * Recherche des traductions avec filtrage.
     *
     * @param module      Module (optionnel)
     * @param messageType Type de message (optionnel)
     * @param locale      Locale (optionnel)
     * @param searchTerm  Terme de recherche (optionnel)
     * @param pageable    Pagination
     * @return Page de traductions
     */
    @GetMapping
    @PreAuthorize("hasAuthority('TRANSLATION_READ')")
    public ResponseEntity<Page<TranslationDTO>> searchTranslations(
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String messageType,
            @RequestParam(required = false) String locale,
            @RequestParam(required = false) String searchTerm,
            Pageable pageable) {
        return ResponseEntity.ok(translationService.searchTranslations(
                module, messageType, locale, searchTerm, pageable));
    }

    /**
     * Crée une nouvelle traduction.
     *
     * @param translationDTO DTO de la traduction à créer
     * @return Traduction créée
     */
    @PostMapping
    @PreAuthorize("hasAuthority('TRANSLATION_CREATE')")
    public ResponseEntity<TranslationDTO> createTranslation(@Valid @RequestBody TranslationDTO translationDTO) {
        return new ResponseEntity<>(translationService.createTranslation(translationDTO), HttpStatus.CREATED);
    }

    /**
     * Met à jour une traduction existante.
     *
     * @param id             ID de la traduction à mettre à jour
     * @param translationDTO DTO avec les nouvelles valeurs
     * @return Traduction mise à jour
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TRANSLATION_UPDATE')")
    public ResponseEntity<TranslationDTO> updateTranslation(
            @PathVariable Long id, @Valid @RequestBody TranslationDTO translationDTO) {
        return ResponseEntity.ok(translationService.updateTranslation(id, translationDTO));
    }

    /**
     * Supprime une traduction.
     *
     * @param id ID de la traduction à supprimer
     * @return Réponse vide
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TRANSLATION_DELETE')")
    public ResponseEntity<Void> deleteTranslation(@PathVariable Long id) {
        translationService.deleteTranslation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Importe des traductions à partir d'un fichier de propriétés.
     *
     * @param module      Module
     * @param messageType Type de message
     * @param locale      Locale
     * @param properties  Map des propriétés à importer
     * @return Nombre de traductions importées
     */
    @PostMapping("/import")
    @PreAuthorize("hasAuthority('TRANSLATION_IMPORT')")
    public ResponseEntity<Integer> importTranslations(
            @RequestParam String module,
            @RequestParam String messageType,
            @RequestParam String locale,
            @RequestBody Map<String, String> properties) {
        int count = translationService.importTranslations(module, messageType, locale, properties);
        return ResponseEntity.ok(count);
    }

    /**
     * Exporte des traductions vers un fichier de propriétés.
     *
     * @param module      Module
     * @param messageType Type de message
     * @param locale      Locale
     * @return Map des propriétés exportées
     */
    @GetMapping("/export")
    @PreAuthorize("hasAuthority('TRANSLATION_EXPORT')")
    public ResponseEntity<Map<String, String>> exportTranslations(
            @RequestParam String module,
            @RequestParam String messageType,
            @RequestParam String locale) {
        return ResponseEntity.ok(translationService.exportTranslations(module, messageType, locale));
    }

    /**
     * Synchronise les traductions avec les fichiers de propriétés.
     *
     * @return Nombre de traductions synchronisées
     */
    @PostMapping("/synchronize")
    @PreAuthorize("hasAuthority('TRANSLATION_SYNCHRONIZE')")
    public ResponseEntity<Integer> synchronizeTranslations() {
        int count = translationService.synchronizeTranslations();
        return ResponseEntity.ok(count);
    }

    /**
     * Récupère la liste des modules disponibles.
     *
     * @return Liste des modules
     */
    @GetMapping("/modules")
    @PreAuthorize("hasAuthority('TRANSLATION_READ')")
    public ResponseEntity<List<String>> getAvailableModules() {
        return ResponseEntity.ok(translationService.getAvailableModules());
    }

    /**
     * Récupère la liste des types de messages disponibles.
     *
     * @return Liste des types de messages
     */
    @GetMapping("/message-types")
    @PreAuthorize("hasAuthority('TRANSLATION_READ')")
    public ResponseEntity<List<String>> getAvailableMessageTypes() {
        return ResponseEntity.ok(translationService.getAvailableMessageTypes());
    }

    /**
     * Récupère la liste des locales disponibles.
     *
     * @return Liste des locales
     */
    @GetMapping("/locales")
    @PreAuthorize("hasAuthority('TRANSLATION_READ')")
    public ResponseEntity<List<String>> getAvailableLocales() {
        return ResponseEntity.ok(translationService.getAvailableLocales());
    }
}
