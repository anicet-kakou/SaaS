package com.devolution.saas.insurance.nonlife.auto.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.application.dto.DriverDTO;
import com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceAlreadyExistsException;
import com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.application.mapper.DriverMapper;
import com.devolution.saas.insurance.nonlife.auto.application.service.DriverService;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service d'application pour la gestion des conducteurs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    @Override
    @Transactional
    public DriverDTO createDriver(Driver driver, UUID organizationId) {
        log.debug("Création d'un nouveau conducteur avec numéro de permis: {}", driver.getLicenseNumber());

        // Vérifier si un conducteur avec le même numéro de permis existe déjà
        driverRepository.findByLicenseNumberAndOrganizationId(driver.getLicenseNumber(), organizationId)
                .ifPresent(d -> {
                    throw AutoResourceAlreadyExistsException.forIdentifier("Conducteur", "numéro de permis", driver.getLicenseNumber());
                });

        // Définir l'organisation
        driver.setOrganizationId(organizationId);

        // Sauvegarder le conducteur
        Driver savedDriver = driverRepository.save(driver);

        return driverMapper.toDto(savedDriver);
    }

    @Override
    @Transactional
    public Optional<DriverDTO> updateDriver(UUID id, Driver driver, UUID organizationId) {
        log.debug("Mise à jour du conducteur avec ID: {}", id);

        Driver existingDriver = driverRepository.findById(id)
                .filter(d -> d.getOrganizationId().equals(organizationId))
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Conducteur", id));

        // Si le numéro de permis a changé, vérifier qu'il n'est pas déjà utilisé
        if (!existingDriver.getLicenseNumber().equals(driver.getLicenseNumber())) {
            driverRepository.findByLicenseNumberAndOrganizationId(driver.getLicenseNumber(), organizationId)
                    .ifPresent(d -> {
                        if (!d.getId().equals(id)) {
                            throw AutoResourceAlreadyExistsException.forIdentifier("Conducteur", "numéro de permis", driver.getLicenseNumber());
                        }
                    });
        }

        // Mettre à jour les propriétés du conducteur
        driver.setId(id);
        driver.setOrganizationId(organizationId);

        Driver updatedDriver = driverRepository.save(driver);
        return Optional.of(driverMapper.toDto(updatedDriver));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DriverDTO> getDriverById(UUID id, UUID organizationId) {
        log.debug("Récupération du conducteur avec ID: {}", id);

        return driverRepository.findById(id)
                .filter(driver -> driver.getOrganizationId().equals(organizationId))
                .map(driverMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DriverDTO> getDriverByLicenseNumber(String licenseNumber, UUID organizationId) {
        log.debug("Récupération du conducteur avec numéro de permis: {}", licenseNumber);

        return driverRepository.findByLicenseNumberAndOrganizationId(licenseNumber, organizationId)
                .map(driverMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DriverDTO> getAllDrivers(UUID organizationId) {
        log.debug("Listage de tous les conducteurs pour l'organisation: {}", organizationId);

        return driverRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(driverMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DriverDTO> getDriversByCustomer(UUID customerId, UUID organizationId) {
        log.debug("Listage des conducteurs pour le client: {} dans l'organisation: {}", customerId, organizationId);

        return driverRepository.findAllByCustomerIdAndOrganizationId(customerId, organizationId)
                .stream()
                .map(driverMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DriverDTO> getPrimaryDriverByCustomer(UUID customerId, UUID organizationId) {
        log.debug("Récupération du conducteur principal pour le client: {} dans l'organisation: {}", customerId, organizationId);

        return driverRepository.findByCustomerIdAndIsPrimaryDriverTrueAndOrganizationId(customerId, organizationId)
                .map(driverMapper::toDto);
    }

    @Override
    @Transactional
    public boolean deleteDriver(UUID id, UUID organizationId) {
        log.debug("Suppression du conducteur avec ID: {}", id);

        return driverRepository.findById(id)
                .filter(driver -> driver.getOrganizationId().equals(organizationId))
                .map(driver -> {
                    driverRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
