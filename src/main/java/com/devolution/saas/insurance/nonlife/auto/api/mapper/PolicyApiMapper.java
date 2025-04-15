package com.devolution.saas.insurance.nonlife.auto.api.mapper;

import com.devolution.saas.common.mapper.EntityApiMapper;
import com.devolution.saas.insurance.nonlife.auto.api.dto.request.CreateAutoPolicyRequest;
import com.devolution.saas.insurance.nonlife.auto.application.dto.AutoPolicyDTO;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre les DTOs d'API et les entités/DTOs de police d'assurance auto.
 */
@Component
public class PolicyApiMapper implements EntityApiMapper<AutoPolicy, CreateAutoPolicyRequest, AutoPolicyDTO> {

    /**
     * Convertit une requête de création de police d'assurance auto en entité.
     *
     * @param request La requête à convertir
     * @return L'entité correspondante
     */
    @Override
    public AutoPolicy toEntity(CreateAutoPolicyRequest request) {
        AutoPolicy policy = AutoPolicy.builder()
                .policyNumber(request.policyNumber())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .premiumAmount(request.premiumAmount())
                .coverageType(request.coverageType())
                .bonusMalusCoefficient(request.bonusMalusCoefficient())
                .annualMileage(request.annualMileage())
                .parkingType(request.parkingType())
                .hasAntiTheftDevice(request.hasAntiTheftDevice())
                .build();

        // Utiliser les setters pour définir les IDs
        policy.setVehicleId(request.vehicleId());
        policy.setPrimaryDriverId(request.primaryDriverId());
        policy.setClaimHistoryCategoryId(request.claimHistoryCategoryId());

        return policy;
    }

    /**
     * Convertit un DTO de police d'assurance auto en réponse.
     * <p>
     * Note: Dans ce cas, le DTO d'application est déjà utilisé comme réponse API,
     * donc cette méthode retourne simplement le DTO tel quel.
     *
     * @param entity L'entité à convertir
     * @return La réponse correspondante
     */
    @Override
    public AutoPolicyDTO toResponse(AutoPolicy entity) {
        throw new UnsupportedOperationException("Cette méthode n'est pas implémentée. Le DTO d'application est utilisé directement comme réponse API.");
    }
}
