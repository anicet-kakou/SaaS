package com.devolution.saas.insurance.nonlife.auto.infrastructure.adapter;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.port.PolicyProvider;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.AutoPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adaptateur qui implémente le port PolicyProvider en utilisant le repository JPA.
 */
@Component
@RequiredArgsConstructor
public class PolicyProviderAdapter implements PolicyProvider {

    private final AutoPolicyRepository autoPolicyRepository;

    @Override
    public Optional<AutoPolicy> findPolicyById(UUID id, UUID organizationId) {
        return autoPolicyRepository.findById(id)
                .filter(policy -> policy.getOrganizationId().equals(organizationId));
    }

    @Override
    public boolean policyNumberExists(String policyNumber, UUID organizationId) {
        return autoPolicyRepository.findByPolicyNumberAndOrganizationId(policyNumber, organizationId)
                .isPresent();
    }
}
