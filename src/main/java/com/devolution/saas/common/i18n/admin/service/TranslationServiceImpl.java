package com.devolution.saas.common.i18n.admin.service;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.i18n.ModuleMessageProvider;
import com.devolution.saas.common.i18n.admin.domain.Translation;
import com.devolution.saas.common.i18n.admin.dto.TranslationDTO;
import com.devolution.saas.common.i18n.admin.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des traductions.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TranslationServiceImpl implements TranslationService {

    private final TranslationRepository translationRepository;
    private final List<ModuleMessageProvider> messageProviders;

    @Override
    public TranslationDTO getTranslationById(Long id) {
        Translation translation = translationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Translation", id));
        return mapToDTO(translation);
    }

    @Override
    public TranslationDTO getTranslation(String module, String messageType, String messageKey, String locale) {
        Translation translation = translationRepository.findByModuleAndMessageTypeAndMessageKeyAndLocale(
                        module, messageType, messageKey, locale)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Translation not found for module=" + module +
                                ", messageType=" + messageType +
                                ", messageKey=" + messageKey +
                                ", locale=" + locale));
        return mapToDTO(translation);
    }

    @Override
    public Page<TranslationDTO> searchTranslations(
            String module, String messageType, String locale, String searchTerm, Pageable pageable) {
        return translationRepository.findTranslations(module, messageType, locale, searchTerm, pageable)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional
    public TranslationDTO createTranslation(TranslationDTO translationDTO) {
        // Vérifier si la traduction existe déjà
        if (translationRepository.existsByModuleAndMessageTypeAndMessageKeyAndLocale(
                translationDTO.module(),
                translationDTO.messageType(),
                translationDTO.messageKey(),
                translationDTO.locale())) {
            throw new IllegalArgumentException("Translation already exists");
        }

        Translation translation = mapToEntity(translationDTO);
        translation.setCreatedAt(LocalDateTime.now());
        translation.setUpdatedAt(LocalDateTime.now());
        translation.setCreatedBy(getCurrentUsername());
        translation.setUpdatedBy(getCurrentUsername());

        Translation savedTranslation = translationRepository.save(translation);
        return mapToDTO(savedTranslation);
    }

    @Override
    @Transactional
    public TranslationDTO updateTranslation(Long id, TranslationDTO translationDTO) {
        Translation translation = translationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Translation", id));

        translation.setMessageText(translationDTO.messageText());
        translation.setDefault(translationDTO.isDefault());
        translation.setUpdatedAt(LocalDateTime.now());
        translation.setUpdatedBy(getCurrentUsername());

        Translation updatedTranslation = translationRepository.save(translation);
        return mapToDTO(updatedTranslation);
    }

    @Override
    @Transactional
    public void deleteTranslation(Long id) {
        if (!translationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Translation", id);
        }
        translationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public int importTranslations(String module, String messageType, String locale, Map<String, String> properties) {
        int count = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            String messageKey = entry.getKey();
            String messageText = entry.getValue();

            // Vérifier si la traduction existe déjà
            Optional<Translation> existingTranslation = translationRepository
                    .findByModuleAndMessageTypeAndMessageKeyAndLocale(module, messageType, messageKey, locale);

            if (existingTranslation.isPresent()) {
                // Mettre à jour la traduction existante
                Translation translation = existingTranslation.get();
                translation.setMessageText(messageText);
                translation.setUpdatedAt(LocalDateTime.now());
                translation.setUpdatedBy(getCurrentUsername());
                translationRepository.save(translation);
            } else {
                // Créer une nouvelle traduction
                Translation translation = Translation.builder()
                        .module(module)
                        .messageType(messageType)
                        .messageKey(messageKey)
                        .locale(locale)
                        .messageText(messageText)
                        .isDefault(false)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .createdBy(getCurrentUsername())
                        .updatedBy(getCurrentUsername())
                        .build();
                translationRepository.save(translation);
            }
            count++;
        }
        return count;
    }

    @Override
    public Map<String, String> exportTranslations(String module, String messageType, String locale) {
        List<Translation> translations = translationRepository
                .findByModuleAndMessageTypeAndLocale(module, messageType, locale);

        Map<String, String> properties = new LinkedHashMap<>();
        for (Translation translation : translations) {
            properties.put(translation.getMessageKey(), translation.getMessageText());
        }
        return properties;
    }

    @Override
    @Transactional
    public int synchronizeTranslations() {
        int count = 0;
        for (ModuleMessageProvider provider : messageProviders) {
            String module = provider.getModuleName();
            for (String messageType : provider.getMessageTypes()) {
                for (Locale locale : getSupportedLocales()) {
                    Properties properties = provider.getMessages(messageType, locale);
                    if (properties != null && !properties.isEmpty()) {
                        Map<String, String> propertiesMap = new HashMap<>();
                        for (String key : properties.stringPropertyNames()) {
                            propertiesMap.put(key, properties.getProperty(key));
                        }
                        count += importTranslations(module, messageType, locale.toString(), propertiesMap);
                    }
                }
            }
        }
        return count;
    }

    @Override
    public List<String> getAvailableModules() {
        return messageProviders.stream()
                .map(ModuleMessageProvider::getModuleName)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAvailableMessageTypes() {
        Set<String> messageTypes = new HashSet<>();
        for (ModuleMessageProvider provider : messageProviders) {
            messageTypes.addAll(provider.getMessageTypes());
        }
        return new ArrayList<>(messageTypes);
    }

    @Override
    public List<String> getAvailableLocales() {
        return Arrays.stream(getSupportedLocales())
                .map(Locale::toString)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une entité Translation en DTO.
     *
     * @param translation Entité Translation
     * @return DTO TranslationDTO
     */
    private TranslationDTO mapToDTO(Translation translation) {
        return TranslationDTO.builder()
                .id(translation.getId())
                .module(translation.getModule())
                .messageType(translation.getMessageType())
                .messageKey(translation.getMessageKey())
                .locale(translation.getLocale())
                .messageText(translation.getMessageText())
                .isDefault(translation.isDefault())
                .createdAt(translation.getCreatedAt())
                .updatedAt(translation.getUpdatedAt())
                .createdBy(translation.getCreatedBy())
                .updatedBy(translation.getUpdatedBy())
                .build();
    }

    /**
     * Convertit un DTO TranslationDTO en entité Translation.
     *
     * @param dto DTO TranslationDTO
     * @return Entité Translation
     */
    private Translation mapToEntity(TranslationDTO dto) {
        return Translation.builder()
                .id(dto.id())
                .module(dto.module())
                .messageType(dto.messageType())
                .messageKey(dto.messageKey())
                .locale(dto.locale())
                .messageText(dto.messageText())
                .isDefault(dto.isDefault())
                .build();
    }

    /**
     * Récupère le nom d'utilisateur courant.
     *
     * @return Nom d'utilisateur ou "system" si non authentifié
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "system";
        }
        return authentication.getName();
    }

    /**
     * Récupère les locales supportées.
     *
     * @return Tableau des locales supportées
     */
    private Locale[] getSupportedLocales() {
        return new Locale[]{Locale.FRENCH, Locale.ENGLISH};
    }
}
