package com.devolution.saas.insurance.nonlife.auto.infrastructure.adapter;

import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;
import com.devolution.saas.insurance.nonlife.auto.domain.port.DriverProvider;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adaptateur qui implémente le port DriverProvider en utilisant le repository JPA.
 */
@Component
@RequiredArgsConstructor
public class DriverProviderAdapter implements DriverProvider {

    private final DriverRepository driverRepository;

    @Override
    public Optional<Driver> findDriverById(UUID id, UUID organizationId) {
        return driverRepository.findById(id)
                .filter(driver -> driver.getOrganizationId().equals(organizationId));
    }

    @Override
    public boolean driverExists(UUID id, UUID organizationId) {
        return driverRepository.findById(id)
                .filter(driver -> driver.getOrganizationId().equals(organizationId))
                .isPresent();
    }

    @Override
    public boolean licenseNumberExists(String licenseNumber, UUID organizationId) {
        return driverRepository.findByLicenseNumberAndOrganizationId(licenseNumber, organizationId)
                .isPresent();
    }
}
