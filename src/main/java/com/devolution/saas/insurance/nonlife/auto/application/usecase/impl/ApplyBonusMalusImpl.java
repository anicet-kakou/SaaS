package com.devolution.saas.insurance.nonlife.auto.application.usecase.impl;

import com.devolution.saas.insurance.nonlife.auto.application.dto.BonusMalusDTO;
import com.devolution.saas.insurance.nonlife.auto.application.usecase.ApplyBonusMalus;
import com.devolution.saas.insurance.nonlife.auto.domain.model.BonusMalus;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.BonusMalusRepository;
import com.devolution.saas.insurance.nonlife.auto.domain.service.BonusMalusCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Implémentation du cas d'utilisation pour l'application du bonus-malus.
 */
@Service
@RequiredArgsConstructor
public class ApplyBonusMalusImpl implements ApplyBonusMalus {

    private final BonusMalusRepository bonusMalusRepository;
    private final BonusMalusCalculator bonusMalusCalculator;

    @Override
    @Transactional
    public BonusMalusDTO execute(UUID customerId, int claimCount, UUID organizationId) {
        // Récupérer le bonus-malus actuel du client ou en créer un nouveau
        BonusMalus bonusMalus = bonusMalusRepository.findActiveByCustomerId(customerId, organizationId)
                .orElseGet(() -> createInitialBonusMalus(customerId, organizationId));

        // Sauvegarder le coefficient actuel comme précédent
        BigDecimal previousCoefficient = bonusMalus.getCoefficient();

        // Calculer le nouveau coefficient
        BigDecimal newCoefficient = bonusMalusCalculator.calculateNewCoefficient(previousCoefficient, claimCount);

        // Mettre à jour le bonus-malus
        bonusMalus.setPreviousCoefficient(previousCoefficient);
        bonusMalus.setCoefficient(newCoefficient);
        bonusMalus.setEffectiveDate(LocalDate.now());
        bonusMalus.setExpiryDate(LocalDate.now().plusYears(1));

        // Mettre à jour les années sans sinistre
        if (claimCount == 0) {
            bonusMalus.setYearsWithoutClaim(bonusMalus.getYearsWithoutClaim() + 1);
        } else {
            bonusMalus.setYearsWithoutClaim(0);
        }

        // Sauvegarder et retourner le bonus-malus mis à jour
        BonusMalus savedBonusMalus = bonusMalusRepository.save(bonusMalus);

        return mapToDTO(savedBonusMalus);
    }

    /**
     * Crée un bonus-malus initial pour un nouveau client.
     *
     * @param customerId     L'ID du client
     * @param organizationId L'ID de l'organisation
     * @return Le bonus-malus initial
     */
    private BonusMalus createInitialBonusMalus(UUID customerId, UUID organizationId) {
        return BonusMalus.builder()
                .customerId(customerId)
                .coefficient(new BigDecimal("1.00"))
                .previousCoefficient(new BigDecimal("1.00"))
                .effectiveDate(LocalDate.now())
                .expiryDate(LocalDate.now().plusYears(1))
                .yearsWithoutClaim(0)
                .organizationId(organizationId)
                .build();
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
