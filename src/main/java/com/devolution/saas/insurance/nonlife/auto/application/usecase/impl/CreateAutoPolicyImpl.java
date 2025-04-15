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
        log.debug("Création d'une police d'assurance automobile avec numéro: {}", command.policyNumber());

        // Vérifier si une police avec le même numéro existe déjà
        autoPolicyRepository.findByPolicyNumberAndOrganizationId(command.policyNumber(), command.organizationId())
                .ifPresent(p -> {
                    throw AutoResourceAlreadyExistsException.forIdentifier("Police d'assurance", "numéro", command.policyNumber());
                });

        // Vérifier que le véhicule existe
        Vehicle vehicle = vehicleRepository.findById(command.vehicleId())
                .filter(v -> v.getOrganizationId().equals(command.organizationId()))
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Véhicule", command.vehicleId()));

        // Vérifier que le conducteur existe
        Driver driver = driverRepository.findById(command.primaryDriverId())
                .filter(d -> d.getOrganizationId().equals(command.organizationId()))
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Conducteur", command.primaryDriverId()));

        // Calculer la prime
        PremiumCalculationResultDTO premiumCalculation = calculateAutoPremium.calculate(
                command.vehicleId(),
                command.primaryDriverId(),
                command.coverageType().name(),
                command.organizationId()
        );

        // Mapper la commande en entité
        AutoPolicy policy = mapCommandToEntity(command, premiumCalculation.finalPremium());

        // Valider la police
        List<String> validationErrors = policyValidator.validateForCreation(policy, command.organizationId());
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
                .policyNumber(command.policyNumber())
                .status(AutoPolicy.PolicyStatus.ACTIVE) // Par défaut, la police est active
                .startDate(command.startDate())
                .endDate(command.endDate())
                .premiumAmount(premiumAmount)
                .coverageType(command.coverageType())
                .bonusMalusCoefficient(command.bonusMalusCoefficient())
                .annualMileage(command.annualMileage())
                .parkingType(command.parkingType())
                .hasAntiTheftDevice(command.hasAntiTheftDevice())
                .claimHistoryCategoryId(command.claimHistoryCategoryId())
                .organizationId(command.organizationId())
                .build();

        // Utiliser les setters pour définir les IDs
        policy.setVehicleId(command.vehicleId());
        policy.setPrimaryDriverId(command.primaryDriverId());

        return policy;
    }
}
