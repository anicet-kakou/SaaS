package com.devolution.saas.common.i18n.admin.repository;

import com.devolution.saas.common.i18n.admin.domain.Translation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Translation.
 */
@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

    /**
     * Recherche une traduction par module, type de message, clé et locale.
     *
     * @param module      Module
     * @param messageType Type de message
     * @param messageKey  Clé du message
     * @param locale      Locale
     * @return Traduction optionnelle
     */
    Optional<Translation> findByModuleAndMessageTypeAndMessageKeyAndLocale(
            String module, String messageType, String messageKey, String locale);

    /**
     * Recherche toutes les traductions pour un module et une locale donnés.
     *
     * @param module Module
     * @param locale Locale
     * @return Liste des traductions
     */
    List<Translation> findByModuleAndLocale(String module, String locale);

    /**
     * Recherche toutes les traductions pour un module, un type de message et une locale donnés.
     *
     * @param module      Module
     * @param messageType Type de message
     * @param locale      Locale
     * @return Liste des traductions
     */
    List<Translation> findByModuleAndMessageTypeAndLocale(String module, String messageType, String locale);

    /**
     * Recherche toutes les traductions pour une locale donnée.
     *
     * @param locale Locale
     * @return Liste des traductions
     */
    List<Translation> findByLocale(String locale);

    /**
     * Recherche paginée des traductions avec filtrage.
     *
     * @param module      Module (optionnel)
     * @param messageType Type de message (optionnel)
     * @param locale      Locale (optionnel)
     * @param searchTerm  Terme de recherche (optionnel)
     * @param pageable    Pagination
     * @return Page de traductions
     */
    @Query("SELECT t FROM Translation t WHERE " +
            "(:module IS NULL OR t.module = :module) AND " +
            "(:messageType IS NULL OR t.messageType = :messageType) AND " +
            "(:locale IS NULL OR t.locale = :locale) AND " +
            "(:searchTerm IS NULL OR " +
            "t.messageKey LIKE %:searchTerm% OR " +
            "t.messageText LIKE %:searchTerm%)")
    Page<Translation> findTranslations(
            @Param("module") String module,
            @Param("messageType") String messageType,
            @Param("locale") String locale,
            @Param("searchTerm") String searchTerm,
            Pageable pageable);

    /**
     * Vérifie si une traduction existe pour un module, un type de message, une clé et une locale donnés.
     *
     * @param module      Module
     * @param messageType Type de message
     * @param messageKey  Clé du message
     * @param locale      Locale
     * @return true si la traduction existe, false sinon
     */
    boolean existsByModuleAndMessageTypeAndMessageKeyAndLocale(
            String module, String messageType, String messageKey, String locale);

    /**
     * Supprime toutes les traductions pour un module, un type de message et une locale donnés.
     *
     * @param module      Module
     * @param messageType Type de message
     * @param locale      Locale
     */
    void deleteByModuleAndMessageTypeAndLocale(String module, String messageType, String locale);
}
