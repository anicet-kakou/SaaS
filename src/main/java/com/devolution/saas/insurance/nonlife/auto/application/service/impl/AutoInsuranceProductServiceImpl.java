package com.devolution.saas.insurance.nonlife.auto.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.application.dto.AutoInsuranceProductDTO;
import com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceAlreadyExistsException;
import com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.application.mapper.AutoInsuranceProductMapper;
import com.devolution.saas.insurance.nonlife.auto.application.service.AutoInsuranceProductService;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoInsuranceProduct;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.AutoInsuranceProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service d'application pour la gestion des produits d'assurance auto.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AutoInsuranceProductServiceImpl implements AutoInsuranceProductService {

    private final AutoInsuranceProductRepository autoInsuranceProductRepository;
    private final AutoInsuranceProductMapper autoInsuranceProductMapper;

    @Override
    @Transactional
    public AutoInsuranceProductDTO createProduct(AutoInsuranceProduct product, UUID organizationId) {
        log.debug("Création d'un nouveau produit d'assurance auto avec code: {}", product.getCode());

        // Vérifier si un produit avec le même code existe déjà
        autoInsuranceProductRepository.findByCodeAndOrganizationId(product.getCode(), organizationId)
                .ifPresent(p -> {
                    throw AutoResourceAlreadyExistsException.forCode("Produit d'assurance auto", product.getCode());
                });

        // Définir l'organisation
        product.setOrganizationId(organizationId);

        // Sauvegarder le produit
        AutoInsuranceProduct savedProduct = autoInsuranceProductRepository.save(product);

        return autoInsuranceProductMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public Optional<AutoInsuranceProductDTO> updateProduct(UUID id, AutoInsuranceProduct product, UUID organizationId) {
        log.debug("Mise à jour du produit d'assurance auto avec ID: {}", id);

        AutoInsuranceProduct existingProduct = autoInsuranceProductRepository.findById(id)
                .filter(p -> p.getOrganizationId().equals(organizationId))
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Produit d'assurance auto", id));

        // Si le code a changé, vérifier qu'il n'est pas déjà utilisé
        if (!existingProduct.getCode().equals(product.getCode())) {
            autoInsuranceProductRepository.findByCodeAndOrganizationId(product.getCode(), organizationId)
                    .ifPresent(p -> {
                        if (!p.getId().equals(id)) {
                            throw AutoResourceAlreadyExistsException.forCode("Produit d'assurance auto", product.getCode());
                        }
                    });
        }

        // Mettre à jour les propriétés du produit
        product.setId(id);
        product.setOrganizationId(organizationId);

        AutoInsuranceProduct updatedProduct = autoInsuranceProductRepository.save(product);
        return Optional.of(autoInsuranceProductMapper.toDto(updatedProduct));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AutoInsuranceProductDTO> getProductById(UUID id, UUID organizationId) {
        log.debug("Récupération du produit d'assurance auto avec ID: {}", id);

        return autoInsuranceProductRepository.findById(id)
                .filter(product -> product.getOrganizationId().equals(organizationId))
                .map(autoInsuranceProductMapper::toDto);
    }

    /**
     * Récupère un produit d'assurance auto par son ID et lève une exception si non trouvé.
     *
     * @param id             ID du produit
     * @param organizationId ID de l'organisation
     * @return Le produit d'assurance auto
     * @throws AutoResourceNotFoundException si le produit n'est pas trouvé
     */
    public AutoInsuranceProductDTO getProductByIdOrThrow(UUID id, UUID organizationId) {
        log.debug("Récupération du produit d'assurance auto avec ID: {} (avec exception si non trouvé)", id);

        return autoInsuranceProductRepository.findById(id)
                .filter(product -> product.getOrganizationId().equals(organizationId))
                .map(autoInsuranceProductMapper::toDto)
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Produit d'assurance auto", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AutoInsuranceProductDTO> getProductByCode(String code, UUID organizationId) {
        log.debug("Récupération du produit d'assurance auto avec code: {}", code);

        return autoInsuranceProductRepository.findByCodeAndOrganizationId(code, organizationId)
                .map(autoInsuranceProductMapper::toDto);
    }

    /**
     * Récupère un produit d'assurance auto par son code et lève une exception si non trouvé.
     *
     * @param code           Code du produit
     * @param organizationId ID de l'organisation
     * @return Le produit d'assurance auto
     * @throws AutoResourceNotFoundException si le produit n'est pas trouvé
     */
    public AutoInsuranceProductDTO getProductByCodeOrThrow(String code, UUID organizationId) {
        log.debug("Récupération du produit d'assurance auto avec code: {} (avec exception si non trouvé)", code);

        return autoInsuranceProductRepository.findByCodeAndOrganizationId(code, organizationId)
                .map(autoInsuranceProductMapper::toDto)
                .orElseThrow(() -> AutoResourceNotFoundException.forCode("Produit d'assurance auto", code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutoInsuranceProductDTO> getAllProducts(UUID organizationId) {
        log.debug("Listage de tous les produits d'assurance auto pour l'organisation: {}", organizationId);

        return autoInsuranceProductRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(autoInsuranceProductMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutoInsuranceProductDTO> getAllActiveProducts(UUID organizationId) {
        log.debug("Listage de tous les produits d'assurance auto actifs pour l'organisation: {}", organizationId);

        return autoInsuranceProductRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(autoInsuranceProductMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutoInsuranceProductDTO> getAllActiveProductsAtDate(LocalDate date, UUID organizationId) {
        log.debug("Listage de tous les produits d'assurance auto actifs à la date: {} pour l'organisation: {}", date, organizationId);

        return autoInsuranceProductRepository.findAllActiveAtDateByOrganizationId(date, organizationId)
                .stream()
                .map(autoInsuranceProductMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteProduct(UUID id, UUID organizationId) {
        log.debug("Suppression du produit d'assurance auto avec ID: {}", id);

        AutoInsuranceProduct product = autoInsuranceProductRepository.findById(id)
                .filter(p -> p.getOrganizationId().equals(organizationId))
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Produit d'assurance auto", id));

        autoInsuranceProductRepository.deleteById(id);
        return true;
    }
}
