package com.devolution.saas.insurance.nonlife.auto.api.mapper;

import com.devolution.saas.common.mapper.EntityApiMapper;
import com.devolution.saas.insurance.nonlife.auto.api.dto.request.CreateAutoInsuranceProductRequest;
import com.devolution.saas.insurance.nonlife.auto.application.dto.AutoInsuranceProductDTO;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoInsuranceProduct;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre les DTOs d'API et les entités/DTOs de produit d'assurance auto.
 */
@Component
public class ProductApiMapper implements EntityApiMapper<AutoInsuranceProduct, CreateAutoInsuranceProductRequest, AutoInsuranceProductDTO> {

    /**
     * Convertit une requête de création de produit d'assurance auto en entité.
     *
     * @param request La requête à convertir
     * @return L'entité correspondante
     */
    @Override
    public AutoInsuranceProduct toEntity(CreateAutoInsuranceProductRequest request) {
        return AutoInsuranceProduct.builder()
                .code(request.code())
                .name(request.name())
                .description(request.description())
                .status(request.status())
                .effectiveDate(request.effectiveDate())
                .expiryDate(request.expiryDate())
                .build();
    }

    /**
     * Convertit un DTO de produit d'assurance auto en réponse.
     * <p>
     * Note: Dans ce cas, le DTO d'application est déjà utilisé comme réponse API,
     * donc cette méthode retourne simplement le DTO tel quel.
     *
     * @param entity L'entité à convertir
     * @return La réponse correspondante
     */
    @Override
    public AutoInsuranceProductDTO toResponse(AutoInsuranceProduct entity) {
        throw new UnsupportedOperationException("Cette méthode n'est pas implémentée. Le DTO d'application est utilisé directement comme réponse API.");
    }
}
