package com.devolution.saas.insurance.nonlife.auto.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.application.dto.AutoPolicyDTO;
import com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceAlreadyExistsException;
import com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.application.mapper.AutoPolicyMapper;
import com.devolution.saas.insurance.nonlife.auto.application.service.AutoPolicyService;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.AutoPolicyRepository;
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
 * Implémentation du service d'application pour la gestion des polices d'assurance automobile.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AutoPolicyServiceImpl implements AutoPolicyService {

    private final AutoPolicyRepository autoPolicyRepository;
    private final AutoPolicyMapper autoPolicyMapper;

    @Override
    @Transactional
    public AutoPolicyDTO createPolicy(AutoPolicy policy, UUID organizationId) {
        log.debug("Création d'une nouvelle police d'assurance automobile avec numéro: {}", policy.getPolicyNumber());

        // Vérifier si une police avec le même numéro existe déjà
        autoPolicyRepository.findByPolicyNumberAndOrganizationId(policy.getPolicyNumber(), organizationId)
                .ifPresent(p -> {
                    throw AutoResourceAlreadyExistsException.forIdentifier("Police d'assurance", "numéro", policy.getPolicyNumber());
                });

        // Définir l'organisation
        policy.setOrganizationId(organizationId);

        // Sauvegarder la police
        AutoPolicy savedPolicy = autoPolicyRepository.save(policy);

        return autoPolicyMapper.toDto(savedPolicy);
    }

    @Override
    @Transactional
    public Optional<AutoPolicyDTO> updatePolicy(UUID id, AutoPolicy policy, UUID organizationId) {
        log.debug("Mise à jour de la police d'assurance automobile avec ID: {}", id);

        AutoPolicy existingPolicy = autoPolicyRepository.findById(id)
                .filter(p -> p.getOrganizationId().equals(organizationId))
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Police d'assurance", id));

        // Si le numéro de police a changé, vérifier qu'il n'est pas déjà utilisé
        if (!existingPolicy.getPolicyNumber().equals(policy.getPolicyNumber())) {
            autoPolicyRepository.findByPolicyNumberAndOrganizationId(policy.getPolicyNumber(), organizationId)
                    .ifPresent(p -> {
                        if (!p.getId().equals(id)) {
                            throw AutoResourceAlreadyExistsException.forIdentifier("Police d'assurance", "numéro", policy.getPolicyNumber());
                        }
                    });
        }

        // Mettre à jour les propriétés de la police
        policy.setId(id);
        policy.setOrganizationId(organizationId);

        AutoPolicy updatedPolicy = autoPolicyRepository.save(policy);
        return Optional.of(autoPolicyMapper.toDto(updatedPolicy));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AutoPolicyDTO> getPolicyById(UUID id, UUID organizationId) {
        log.debug("Récupération de la police d'assurance automobile avec ID: {}", id);

        return autoPolicyRepository.findById(id)
                .filter(policy -> policy.getOrganizationId().equals(organizationId))
                .map(autoPolicyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AutoPolicyDTO> getPolicyByNumber(String policyNumber, UUID organizationId) {
        log.debug("Récupération de la police d'assurance automobile avec numéro: {}", policyNumber);

        return autoPolicyRepository.findByPolicyNumberAndOrganizationId(policyNumber, organizationId)
                .map(autoPolicyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutoPolicyDTO> getAllPolicies(UUID organizationId) {
        log.debug("Listage de toutes les polices d'assurance automobile pour l'organisation: {}", organizationId);

        return autoPolicyRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(autoPolicyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutoPolicyDTO> getAllActivePolicies(UUID organizationId) {
        log.debug("Listage de toutes les polices d'assurance automobile actives pour l'organisation: {}", organizationId);

        return autoPolicyRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(autoPolicyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutoPolicyDTO> getPoliciesByVehicle(UUID vehicleId, UUID organizationId) {
        log.debug("Listage des polices d'assurance automobile pour le véhicule: {} dans l'organisation: {}", vehicleId, organizationId);

        return autoPolicyRepository.findAllByVehicleIdAndOrganizationId(vehicleId, organizationId)
                .stream()
                .map(autoPolicyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutoPolicyDTO> getPoliciesByPrimaryDriver(UUID driverId, UUID organizationId) {
        log.debug("Listage des polices d'assurance automobile pour le conducteur principal: {} dans l'organisation: {}", driverId, organizationId);

        return autoPolicyRepository.findAllByPrimaryDriverIdAndOrganizationId(driverId, organizationId)
                .stream()
                .map(autoPolicyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutoPolicyDTO> getPoliciesExpiringBetween(LocalDate startDate, LocalDate endDate, UUID organizationId) {
        log.debug("Listage des polices d'assurance automobile expirant entre: {} et {} pour l'organisation: {}", startDate, endDate, organizationId);

        return autoPolicyRepository.findAllByEndDateBetweenAndOrganizationId(startDate, endDate, organizationId)
                .stream()
                .map(autoPolicyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deletePolicy(UUID id, UUID organizationId) {
        log.debug("Suppression de la police d'assurance automobile avec ID: {}", id);

        return autoPolicyRepository.findById(id)
                .filter(policy -> policy.getOrganizationId().equals(organizationId))
                .map(policy -> {
                    autoPolicyRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
