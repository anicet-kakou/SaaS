package com.devolution.saas.insurance.nonlife.auto.infrastructure.adapter;

import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.port.VehicleProvider;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adaptateur qui implémente le port VehicleProvider en utilisant le repository JPA.
 */
@Component
@RequiredArgsConstructor
public class VehicleProviderAdapter implements VehicleProvider {

    private final VehicleRepository vehicleRepository;

    @Override
    public Optional<Vehicle> findVehicleById(UUID id, UUID organizationId) {
        return vehicleRepository.findById(id)
                .filter(vehicle -> vehicle.getOrganizationId().equals(organizationId));
    }

    @Override
    public boolean vehicleExists(UUID id, UUID organizationId) {
        return vehicleRepository.findById(id)
                .filter(vehicle -> vehicle.getOrganizationId().equals(organizationId))
                .isPresent();
    }
}
