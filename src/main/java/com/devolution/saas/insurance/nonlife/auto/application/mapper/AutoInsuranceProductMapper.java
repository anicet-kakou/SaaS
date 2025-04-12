package com.devolution.saas.insurance.nonlife.auto.application.mapper;

import com.devolution.saas.insurance.nonlife.auto.application.dto.AutoInsuranceProductDTO;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoInsuranceProduct;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre les entités AutoInsuranceProduct et les DTOs AutoInsuranceProductDTO.
 */
@Component
public class AutoInsuranceProductMapper {

    /**
     * Convertit une entité AutoInsuranceProduct en DTO AutoInsuranceProductDTO.
     *
     * @param product L'entité à convertir
     * @return Le DTO correspondant
     */
    public AutoInsuranceProductDTO toDto(AutoInsuranceProduct product) {
        if (product == null) {
            return null;
        }

        return AutoInsuranceProductDTO.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .status(product.getStatus())
                .effectiveDate(product.getEffectiveDate())
                .expiryDate(product.getExpiryDate())
                .organizationId(product.getOrganizationId())
                .build();
    }

    /**
     * Convertit un DTO AutoInsuranceProductDTO en entité AutoInsuranceProduct.
     *
     * @param dto Le DTO à convertir
     * @return L'entité correspondante
     */
    public AutoInsuranceProduct toEntity(AutoInsuranceProductDTO dto) {
        if (dto == null) {
            return null;
        }

        return AutoInsuranceProduct.builder()
                .id(dto.id())
                .code(dto.code())
                .name(dto.name())
                .description(dto.description())
                .status(dto.status())
                .effectiveDate(dto.effectiveDate())
                .expiryDate(dto.expiryDate())
                .organizationId(dto.organizationId())
                .build();
    }
}
