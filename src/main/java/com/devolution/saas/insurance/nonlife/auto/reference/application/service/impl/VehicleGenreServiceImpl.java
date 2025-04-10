package com.devolution.saas.insurance.nonlife.auto.reference.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleGenreDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.application.mapper.VehicleGenreMapper;
import com.devolution.saas.insurance.nonlife.auto.reference.application.service.VehicleGenreService;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleGenre;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.repository.VehicleGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des genres de véhicule.
 */
@Service
@RequiredArgsConstructor
public class VehicleGenreServiceImpl implements VehicleGenreService {

    private final VehicleGenreRepository vehicleGenreRepository;
    private final VehicleGenreMapper vehicleGenreMapper;

    @Override
    public VehicleGenreDTO createVehicleGenre(VehicleGenre vehicleGenre, UUID organizationId) {
        vehicleGenre.setOrganizationId(organizationId);
        VehicleGenre savedGenre = vehicleGenreRepository.save(vehicleGenre);
        return vehicleGenreMapper.toDto(savedGenre);
    }

    @Override
    public Optional<VehicleGenreDTO> updateVehicleGenre(UUID id, VehicleGenre vehicleGenre, UUID organizationId) {
        return vehicleGenreRepository.findById(id)
                .filter(genre -> genre.getOrganizationId().equals(organizationId))
                .map(existingGenre -> {
                    vehicleGenre.setId(id);
                    vehicleGenre.setOrganizationId(organizationId);
                    VehicleGenre updatedGenre = vehicleGenreRepository.save(vehicleGenre);
                    return vehicleGenreMapper.toDto(updatedGenre);
                });
    }

    @Override
    public Optional<VehicleGenreDTO> getVehicleGenreById(UUID id, UUID organizationId) {
        return vehicleGenreRepository.findById(id)
                .filter(genre -> genre.getOrganizationId().equals(organizationId))
                .map(vehicleGenreMapper::toDto);
    }

    @Override
    public Optional<VehicleGenreDTO> getVehicleGenreByCode(String code, UUID organizationId) {
        return vehicleGenreRepository.findByCodeAndOrganizationId(code, organizationId)
                .map(vehicleGenreMapper::toDto);
    }

    @Override
    public List<VehicleGenreDTO> getAllVehicleGenres(UUID organizationId) {
        return vehicleGenreRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(vehicleGenreMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleGenreDTO> getAllActiveVehicleGenres(UUID organizationId) {
        return vehicleGenreRepository.findAllActiveByOrganizationId(organizationId)
                .stream()
                .map(vehicleGenreMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteVehicleGenre(UUID id, UUID organizationId) {
        return vehicleGenreRepository.findById(id)
                .filter(genre -> genre.getOrganizationId().equals(organizationId))
                .map(genre -> {
                    vehicleGenreRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
