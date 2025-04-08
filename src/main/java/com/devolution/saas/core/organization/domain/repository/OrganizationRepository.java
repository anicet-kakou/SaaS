package com.devolution.saas.core.organization.domain.repository;

import com.devolution.saas.core.organization.domain.model.Organization;
import com.devolution.saas.core.organization.domain.model.OrganizationStatus;
import com.devolution.saas.core.organization.domain.model.OrganizationType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de repository pour les opérations sur les organisations.
 */
public interface OrganizationRepository {

    /**
     * Enregistre une organisation.
     *
     * @param organization Organisation à enregistrer
     * @return Organisation enregistrée
     */
    Organization save(Organization organization);

    /**
     * Trouve une organisation par son ID.
     *
     * @param id ID de l'organisation
     * @return Organisation trouvée ou Optional vide
     */
    Optional<Organization> findById(UUID id);

    /**
     * Trouve une organisation par son code.
     *
     * @param code Code de l'organisation
     * @return Organisation trouvée ou Optional vide
     */
    Optional<Organization> findByCode(String code);

    /**
     * Trouve toutes les organisations.
     *
     * @return Liste des organisations
     */
    List<Organization> findAll();

    /**
     * Trouve toutes les organisations par statut.
     *
     * @param status Statut des organisations
     * @return Liste des organisations
     */
    List<Organization> findAllByStatus(OrganizationStatus status);

    /**
     * Trouve toutes les organisations par type.
     *
     * @param type Type des organisations
     * @return Liste des organisations
     */
    List<Organization> findAllByType(OrganizationType type);

    /**
     * Trouve toutes les organisations enfants d'une organisation.
     *
     * @param parentId ID de l'organisation parente
     * @return Liste des organisations enfants
     */
    List<Organization> findAllByParentId(UUID parentId);

    /**
     * Trouve toutes les organisations racines (sans parent).
     *
     * @return Liste des organisations racines
     */
    List<Organization> findAllRoots();

    /**
     * Vérifie si une organisation existe par son code.
     *
     * @param code Code de l'organisation
     * @return true si l'organisation existe, false sinon
     */
    boolean existsByCode(String code);

    /**
     * Supprime une organisation.
     *
     * @param organization Organisation à supprimer
     */
    void delete(Organization organization);

    /**
     * Compte le nombre d'organisations.
     *
     * @return Nombre d'organisations
     */
    long count();

    /**
     * Vérifie si une organisation existe par son ID.
     *
     * @param id ID de l'organisation
     * @return true si l'organisation existe, false sinon
     */
    boolean existsById(UUID id);
}
