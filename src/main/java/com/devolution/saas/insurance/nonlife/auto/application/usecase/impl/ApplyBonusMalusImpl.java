package com.devolution.saas.insurance.nonlife.auto.application.usecase.impl;

import com.devolution.saas.common.util.Validation;
import com.devolution.saas.insurance.nonlife.auto.application.dto.BonusMalusDTO;
import com.devolution.saas.insurance.nonlife.auto.application.usecase.ApplyBonusMalus;
import com.devolution.saas.insurance.nonlife.auto.domain.model.BonusMalus;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.BonusMalusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implémentation du cas d'utilisation pour l'application du bonus-malus.
 */
@Service
@RequiredArgsConstructor
public class ApplyBonusMalusImpl implements ApplyBonusMalus {

    private final BonusMalusRepository bonusMalusRepository;

    @Override
    @Transactional
    public BonusMalusDTO execute(UUID customerId, int claimCount, UUID organizationId) {
        // Validate input parameters
        Validation.validateNotNull(customerId, "ID du client");
        Validation.validateNotNegative(claimCount, "nombre de sinistres");
        Validation.validateNotNull(organizationId, "ID de l'organisation");

        // Récupérer le bonus-malus actuel du client ou en créer un nouveau
        BonusMalus bonusMalus = bonusMalusRepository.findActiveByCustomerId(customerId, organizationId)
                .orElseGet(() -> BonusMalus.createInitial(customerId, organizationId));

        // Utiliser la méthode du domaine pour mettre à jour le bonus-malus
        bonusMalus.update(claimCount);

        // Sauvegarder et retourner le bonus-malus mis à jour
        BonusMalus savedBonusMalus = bonusMalusRepository.save(bonusMalus);

        return mapToDTO(savedBonusMalus);
    }


    /**
     * Convertit une entité BonusMalus en DTO.
     *
     * @param bonusMalus L'entité à convertir
     * @return Le DTO correspondant
     */
    private BonusMalusDTO mapToDTO(BonusMalus bonusMalus) {
        return BonusMalusDTO.builder()
                .id(bonusMalus.getId())
                .customerId(bonusMalus.getCustomerId())
                .coefficient(bonusMalus.getCoefficient())
                .effectiveDate(bonusMalus.getEffectiveDate())
                .expiryDate(bonusMalus.getExpiryDate())
                .previousCoefficient(bonusMalus.getPreviousCoefficient())
                .yearsWithoutClaim(bonusMalus.getYearsWithoutClaim())
                .build();
    }
}
