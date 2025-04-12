package com.devolution.saas.insurance.nonlife.auto.application.usecase.impl;

import com.devolution.saas.insurance.nonlife.auto.application.command.CreateAutoPolicyCommand;
import com.devolution.saas.insurance.nonlife.auto.application.dto.AutoPolicyDTO;
import com.devolution.saas.insurance.nonlife.auto.application.dto.PremiumCalculationResultDTO;
import com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceAlreadyExistsException;
import com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.application.exception.BusinessRuleViolationException;
import com.devolution.saas.insurance.nonlife.auto.application.mapper.AutoPolicyMapper;
import com.devolution.saas.insurance.nonlife.auto.application.usecase.CalculateAutoPremium;
import com.devolution.saas.insurance.nonlife.auto.application.usecase.CreateAutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.AutoPolicyRepository;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.DriverRepository;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.VehicleRepository;
import com.devolution.saas.insurance.nonlife.auto.domain.service.PolicyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implémentation du cas d'utilisation pour la création d'une police d'assurance automobile.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateAutoPolicyImpl implements CreateAutoPolicy {

    private final AutoPolicyRepository autoPolicyRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final PolicyValidator policyValidator;
    private final AutoPolicyMapper autoPolicyMapper;
    private final CalculateAutoPremium calculateAutoPremium;

    @Override
    @Transactional
    public AutoPolicyDTO execute(CreateAutoPolicyCommand command) {
        log.debug("Création d'une police d'assurance automobile avec numéro: {}", command.getPolicyNumber());

        // Vérifier si une police avec le même numéro existe déjà
        autoPolicyRepository.findByPolicyNumberAndOrganizationId(command.getPolicyNumber(), command.getOrganizationId())
                .ifPresent(p -> {
                    throw AutoResourceAlreadyExistsException.forIdentifier("Police d'assurance", "numéro", command.getPolicyNumber());
                });

        // Vérifier que le véhicule existe
        Vehicle vehicle = vehicleRepository.findById(command.getVehicleId())
                .filter(v -> v.getOrganizationId().equals(command.getOrganizationId()))
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Véhicule", command.getVehicleId()));

        // Vérifier que le conducteur existe
        Driver driver = driverRepository.findById(command.getPrimaryDriverId())
                .filter(d -> d.getOrganizationId().equals(command.getOrganizationId()))
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Conducteur", command.getPrimaryDriverId()));

        // Calculer la prime
        PremiumCalculationResultDTO premiumCalculation = calculateAutoPremium.calculate(
                command.getVehicleId(),
                command.getPrimaryDriverId(),
                command.getCoverageType().name(),
                command.getOrganizationId()
        );

        // Mapper la commande en entité
        AutoPolicy policy = mapCommandToEntity(command, premiumCalculation.finalPremium());

        // Valider la police
        List<String> validationErrors = policyValidator.validateForCreation(policy, command.getOrganizationId());
        if (!validationErrors.isEmpty()) {
            throw new BusinessRuleViolationException("Erreurs de validation: " + String.join(", ", validationErrors));
        }

        // Sauvegarder la police
        AutoPolicy savedPolicy = autoPolicyRepository.save(policy);

        // Mapper l'entité en DTO
        return autoPolicyMapper.toDto(savedPolicy);
    }

    /**
     * Mappe une commande de création en entité AutoPolicy.
     *
     * @param command       La commande à mapper
     * @param premiumAmount Le montant de la prime calculée
     * @return L'entité AutoPolicy correspondante
     */
    private AutoPolicy mapCommandToEntity(CreateAutoPolicyCommand command, java.math.BigDecimal premiumAmount) {
        AutoPolicy policy = AutoPolicy.builder()
                .policyNumber(command.getPolicyNumber())
                .status(AutoPolicy.PolicyStatus.ACTIVE) // Par défaut, la police est active
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .premiumAmount(premiumAmount)
                .coverageType(command.getCoverageType())
                .bonusMalusCoefficient(command.getBonusMalusCoefficient())
                .annualMileage(command.getAnnualMileage())
                .parkingType(command.getParkingType())
                .hasAntiTheftDevice(command.isHasAntiTheftDevice())
                .claimHistoryCategoryId(command.getClaimHistoryCategoryId())
                .organizationId(command.getOrganizationId())
                .build();

        // Utiliser les setters pour définir les IDs
        policy.setVehicleId(command.getVehicleId());
        policy.setPrimaryDriverId(command.getPrimaryDriverId());

        return policy;
    }
}
