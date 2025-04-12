package com.devolution.saas.insurance.nonlife.auto.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.application.dto.DriverDTO;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre les entités Driver et les DTOs DriverDTO.
 */
@Component
public class DriverMapper {

    /**
     * Convertit une entité Driver en DTO DriverDTO.
     *
     * @param driver L'entité à convertir
     * @return Le DTO correspondant
     */
    public DriverDTO toDto(Driver driver) {
        if (driver == null) {
            return null;
        }

        return DriverDTO.builder()
                .id(driver.getId())
                .customerId(driver.getCustomerId())
                .licenseNumber(driver.getLicenseNumber())
                .licenseTypeId(driver.getLicenseTypeId())
                .licenseIssueDate(driver.getLicenseIssueDate())
                .licenseExpiryDate(driver.getLicenseExpiryDate())
                .isPrimaryDriver(driver.isPrimaryDriver())
                .yearsOfDrivingExperience(driver.getYearsOfDrivingExperience())
                .organizationId(driver.getOrganizationId())
                .build();
    }

    /**
     * Convertit un DTO DriverDTO en entité Driver.
     *
     * @param dto Le DTO à convertir
     * @return L'entité correspondante
     */
    public Driver toEntity(DriverDTO dto) {
        if (dto == null) {
            return null;
        }

        Driver driver = Driver.builder()
                .id(dto.id())
                .customerId(dto.customerId())
                .licenseNumber(dto.licenseNumber())
                .licenseIssueDate(dto.licenseIssueDate())
                .licenseExpiryDate(dto.licenseExpiryDate())
                .isPrimaryDriver(dto.isPrimaryDriver())
                .yearsOfDrivingExperience(dto.yearsOfDrivingExperience())
                .organizationId(dto.organizationId())
                .build();

        // Utiliser les setters pour définir les IDs
        driver.setLicenseTypeId(dto.licenseTypeId());

        return driver;
    }
}
