package com.devolution.saas.insurance.nonlife.auto.application.usecase.impl;

import com.devolution.saas.insurance.common.application.exception.InvalidPolicyException;
import com.devolution.saas.insurance.nonlife.auto.application.command.CreateAutoPolicyCommand;
import com.devolution.saas.insurance.nonlife.auto.application.dto.AutoPolicyDTO;
import com.devolution.saas.insurance.nonlife.auto.application.dto.PremiumCalculationResultDTO;
import com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceAlreadyExistsException;
import com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.application.mapper.AutoPolicyMapper;
import com.devolution.saas.insurance.nonlife.auto.application.usecase.CalculateAutoPremium;
import com.devolution.saas.insurance.nonlife.auto.application.usecase.UpdateAutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.AutoPolicyRepository;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.DriverRepository;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.VehicleRepository;
import com.devolution.saas.insurance.nonlife.auto.domain.service.AutoPolicyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implémentation du cas d'utilisation pour la mise à jour d'une police d'assurance automobile.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateAutoPolicyImpl implements UpdateAutoPolicy {

    private final AutoPolicyRepository autoPolicyRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final AutoPolicyValidator policyValidator;
    private final AutoPolicyMapper autoPolicyMapper;
    private final CalculateAutoPremium calculateAutoPremium;

    @Override
    @Transactional
    public AutoPolicyDTO execute(UUID id, CreateAutoPolicyCommand command) {
        log.debug("Mise à jour de la police d'assurance automobile avec ID: {}", id);

        // Vérifier que la police existe
        AutoPolicy existingPolicy = autoPolicyRepository.findById(id)
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Police d'assurance", id));

        // Vérifier que la police appartient à l'organisation
        if (!existingPolicy.getOrganizationId().equals(command.organizationId())) {
            throw AutoResourceNotFoundException.forId("Police d'assurance", id);
        }

        // Si le numéro de police a changé, vérifier qu'il n'est pas déjà utilisé
        if (!existingPolicy.getPolicyNumber().equals(command.policyNumber())) {
            autoPolicyRepository.findByPolicyNumberAndOrganizationId(command.policyNumber(), command.organizationId())
                    .ifPresent(p -> {
                        if (!p.getId().equals(id)) {
                            throw AutoResourceAlreadyExistsException.forIdentifier("Police d'assurance", "numéro", command.policyNumber());
                        }
                    });
        }

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
        policy.setId(id);
        policy.setStatus(existingPolicy.getStatus()); // Conserver le statut existant
        policy.setProductId(existingPolicy.getProductId()); // Conserver l'ID du produit
        policy.setCustomerId(existingPolicy.getCustomerId()); // Conserver l'ID du client

        // Valider la police
        List<String> validationErrors = policyValidator.validateForUpdate(policy, existingPolicy, command.organizationId());
        if (!validationErrors.isEmpty()) {
            throw InvalidPolicyException.withValidationErrors(validationErrors);
        }

        // Sauvegarder la police
        AutoPolicy updatedPolicy = autoPolicyRepository.save(policy);

        // Mapper l'entité en DTO
        return autoPolicyMapper.toDto(updatedPolicy);
    }

    /**
     * Mappe une commande de mise à jour en entité AutoPolicy.
     *
     * @param command       La commande à mapper
     * @param premiumAmount Le montant de la prime calculée
     * @return L'entité AutoPolicy correspondante
     */
    private AutoPolicy mapCommandToEntity(CreateAutoPolicyCommand command, java.math.BigDecimal premiumAmount) {
        AutoPolicy policy = AutoPolicy.builder()
                .policyNumber(command.policyNumber())
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
