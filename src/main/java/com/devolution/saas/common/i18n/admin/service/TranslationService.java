package com.devolution.saas.common.i18n.admin.service;

import com.devolution.saas.common.i18n.admin.dto.TranslationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Interface pour le service de gestion des traductions.
 */
public interface TranslationService {

    /**
     * Récupère une traduction par son ID.
     *
     * @param id ID de la traduction
     * @return DTO de la traduction
     */
    TranslationDTO getTranslationById(Long id);

    /**
     * Récupère une traduction par module, type de message, clé et locale.
     *
     * @param module      Module
     * @param messageType Type de message
     * @param messageKey  Clé du message
     * @param locale      Locale
     * @return DTO de la traduction
     */
    TranslationDTO getTranslation(String module, String messageType, String messageKey, String locale);

    /**
     * Recherche paginée des traductions avec filtrage.
     *
     * @param module      Module (optionnel)
     * @param messageType Type de message (optionnel)
     * @param locale      Locale (optionnel)
     * @param searchTerm  Terme de recherche (optionnel)
     * @param pageable    Pagination
     * @return Page de DTOs de traductions
     */
    Page<TranslationDTO> searchTranslations(
            String module, String messageType, String locale, String searchTerm, Pageable pageable);

    /**
     * Crée une nouvelle traduction.
     *
     * @param translationDTO DTO de la traduction à créer
     * @return DTO de la traduction créée
     */
    TranslationDTO createTranslation(TranslationDTO translationDTO);

    /**
     * Met à jour une traduction existante.
     *
     * @param id             ID de la traduction à mettre à jour
     * @param translationDTO DTO avec les nouvelles valeurs
     * @return DTO de la traduction mise à jour
     */
    TranslationDTO updateTranslation(Long id, TranslationDTO translationDTO);

    /**
     * Supprime une traduction.
     *
     * @param id ID de la traduction à supprimer
     */
    void deleteTranslation(Long id);

    /**
     * Importe des traductions à partir d'un fichier de propriétés.
     *
     * @param module      Module
     * @param messageType Type de message
     * @param locale      Locale
     * @param properties  Map des propriétés à importer
     * @return Nombre de traductions importées
     */
    int importTranslations(String module, String messageType, String locale, Map<String, String> properties);

    /**
     * Exporte des traductions vers un fichier de propriétés.
     *
     * @param module      Module
     * @param messageType Type de message
     * @param locale      Locale
     * @return Map des propriétés exportées
     */
    Map<String, String> exportTranslations(String module, String messageType, String locale);

    /**
     * Synchronise les traductions avec les fichiers de propriétés.
     * Charge les traductions des fichiers de propriétés dans la base de données.
     *
     * @return Nombre de traductions synchronisées
     */
    int synchronizeTranslations();

    /**
     * Récupère la liste des modules disponibles.
     *
     * @return Liste des modules
     */
    List<String> getAvailableModules();

    /**
     * Récupère la liste des types de messages disponibles.
     *
     * @return Liste des types de messages
     */
    List<String> getAvailableMessageTypes();

    /**
     * Récupère la liste des locales disponibles.
     *
     * @return Liste des locales
     */
    List<String> getAvailableLocales();
}
