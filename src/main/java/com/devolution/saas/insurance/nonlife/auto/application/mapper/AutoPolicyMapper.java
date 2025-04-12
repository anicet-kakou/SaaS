package com.devolution.saas.insurance.nonlife.auto.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.application.dto.AutoPolicyDTO;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre les entités AutoPolicy et les DTOs AutoPolicyDTO.
 */
@Component
public class AutoPolicyMapper {

    /**
     * Convertit une entité AutoPolicy en DTO AutoPolicyDTO.
     *
     * @param policy L'entité à convertir
     * @return Le DTO correspondant
     */
    public AutoPolicyDTO toDto(AutoPolicy policy) {
        if (policy == null) {
            return null;
        }

        return AutoPolicyDTO.builder()
                .id(policy.getId())
                .policyNumber(policy.getPolicyNumber())
                .status(policy.getStatus())
                .startDate(policy.getStartDate())
                .endDate(policy.getEndDate())
                .premiumAmount(policy.getPremiumAmount())
                .vehicleId(policy.getVehicleId())
                .primaryDriverId(policy.getPrimaryDriverId())
                .coverageType(policy.getCoverageType())
                .bonusMalusCoefficient(policy.getBonusMalusCoefficient())
                .annualMileage(policy.getAnnualMileage())
                .parkingType(policy.getParkingType())
                .hasAntiTheftDevice(policy.isHasAntiTheftDevice())
                .claimHistoryCategoryId(policy.getClaimHistoryCategoryId())
                .organizationId(policy.getOrganizationId())
                .build();
    }

    /**
     * Convertit un DTO AutoPolicyDTO en entité AutoPolicy.
     *
     * @param dto Le DTO à convertir
     * @return L'entité correspondante
     */
    public AutoPolicy toEntity(AutoPolicyDTO dto) {
        if (dto == null) {
            return null;
        }

        AutoPolicy policy = AutoPolicy.builder()
                .id(dto.id())
                .policyNumber(dto.policyNumber())
                .status(dto.status())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .premiumAmount(dto.premiumAmount())
                .coverageType(dto.coverageType())
                .bonusMalusCoefficient(dto.bonusMalusCoefficient())
                .annualMileage(dto.annualMileage())
                .parkingType(dto.parkingType())
                .hasAntiTheftDevice(dto.hasAntiTheftDevice())
                .claimHistoryCategoryId(dto.claimHistoryCategoryId())
                .organizationId(dto.organizationId())
                .build();

        // Utiliser les setters pour définir les IDs
        policy.setVehicleId(dto.vehicleId());
        policy.setPrimaryDriverId(dto.primaryDriverId());

        return policy;
    }
}
