package com.devolution.saas.core.organization.infrastructure.messaging;

import com.devolution.saas.core.organization.domain.event.OrganizationCreatedEvent;
import com.devolution.saas.core.organization.domain.event.OrganizationUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publieur d'événements pour les organisations.
 * Utilise le mécanisme d'événements de Spring pour publier des événements liés aux organisations.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrganizationEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * Publie un événement de création d'organisation.
     *
     * @param event Événement de création d'organisation
     */
    public void publishOrganizationCreatedEvent(OrganizationCreatedEvent event) {
        log.debug("Publication d'un événement de création d'organisation: {}", event.getOrganizationId());
        eventPublisher.publishEvent(event);
    }

    /**
     * Publie un événement de mise à jour d'organisation.
     *
     * @param event Événement de mise à jour d'organisation
     */
    public void publishOrganizationUpdatedEvent(OrganizationUpdatedEvent event) {
        log.debug("Publication d'un événement de mise à jour d'organisation: {}", event.getOrganizationId());
        eventPublisher.publishEvent(event);
    }
}
