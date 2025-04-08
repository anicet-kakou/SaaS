package com.devolution.saas.core.organization.application.mapper;

import com.devolution.saas.core.organization.application.dto.OrganizationDTO;
import com.devolution.saas.core.organization.domain.model.Organization;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir les entités Organization en DTOs et vice-versa.
 */
@Component
public class OrganizationMapper {

    /**
     * Convertit une entité Organization en DTO.
     *
     * @param organization Entité Organization
     * @return DTO OrganizationDTO
     */
    public OrganizationDTO toDTO(Organization organization) {
        return OrganizationDTO.builder()
                .id(organization.getId())
                .name(organization.getName())
                .code(organization.getCode())
                .type(organization.getType())
                .status(organization.getStatus())
                .parentId(organization.getParent() != null ? organization.getParent().getId() : null)
                .parentName(organization.getParent() != null ? organization.getParent().getName() : null)
                .address(organization.getAddress())
                .phone(organization.getPhone())
                .email(organization.getEmail())
                .website(organization.getWebsite())
                .logoUrl(organization.getLogoUrl())
                .primaryContactName(organization.getPrimaryContactName())
                .description(organization.getDescription())
                .settings(organization.getSettings())
                .createdAt(organization.getCreatedAt())
                .updatedAt(organization.getUpdatedAt())
                .active(organization.isActive())
                .build();
    }
}
