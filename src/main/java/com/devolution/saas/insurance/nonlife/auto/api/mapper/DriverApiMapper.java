package com.devolution.saas.insurance.nonlife.auto.api.mapper;

import com.devolution.saas.common.mapper.EntityApiMapper;
import com.devolution.saas.insurance.nonlife.auto.api.dto.request.CreateDriverRequest;
import com.devolution.saas.insurance.nonlife.auto.api.dto.response.DriverResponse;
import com.devolution.saas.insurance.nonlife.auto.application.dto.DriverDTO;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre les DTOs d'API et les entités/DTOs de conducteur.
 */
@Component
public class DriverApiMapper implements EntityApiMapper<Driver, CreateDriverRequest, DriverResponse> {

    /**
     * Convertit une requête de création de conducteur en entité.
     *
     * @param request La requête à convertir
     * @return L'entité correspondante
     */
    @Override
    public Driver toEntity(CreateDriverRequest request) {
        Driver driver = Driver.builder()
                .customerId(request.customerId())
                .licenseNumber(request.licenseNumber())
                .licenseIssueDate(request.licenseIssueDate())
                .licenseExpiryDate(request.licenseExpiryDate())
                .isPrimaryDriver(request.isPrimaryDriver())
                .yearsOfDrivingExperience(request.yearsOfDrivingExperience())
                .build();

        // Utiliser les setters pour définir les IDs
        driver.setLicenseTypeId(request.licenseTypeId());

        return driver;
    }

    /**
     * Convertit un DTO de conducteur en réponse.
     *
     * @param entity L'entité à convertir
     * @return La réponse correspondante
     */
    @Override
    public DriverResponse toResponse(Driver entity) {
        throw new UnsupportedOperationException("Cette méthode n'est pas implémentée. Utilisez toResponse(DriverDTO) à la place.");
    }

    /**
     * Convertit un DTO de conducteur en réponse.
     *
     * @param dto Le DTO à convertir
     * @return La réponse correspondante
     */
    public DriverResponse toResponse(DriverDTO dto) {
        return DriverResponse.builder()
                .id(dto.id())
                .customerId(dto.customerId())
                .customerName(dto.customerName())
                .licenseNumber(dto.licenseNumber())
                .licenseTypeId(dto.licenseTypeId())
                .licenseTypeName(dto.licenseTypeName())
                .licenseIssueDate(dto.licenseIssueDate())
                .licenseExpiryDate(dto.licenseExpiryDate())
                .isPrimaryDriver(dto.isPrimaryDriver())
                .yearsOfDrivingExperience(dto.yearsOfDrivingExperience())
                .organizationId(dto.organizationId())
                .build();
    }
}
