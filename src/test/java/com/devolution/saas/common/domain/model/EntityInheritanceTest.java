package com.devolution.saas.common.domain.model;

import com.devolution.saas.core.organization.domain.model.Organization;
import com.devolution.saas.core.security.domain.model.Role;
import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.model.BonusMalus;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour vérifier la hiérarchie d'héritage des entités.
 */
public class EntityInheritanceTest {

    /**
     * Vérifie que les champs hérités ne sont pas redéfinis dans les classes enfants.
     */
    @Test
    void testNoFieldRedefinitionInChildClasses() {
        // Champs de BaseEntity
        List<String> baseEntityFields = getFieldNames(BaseEntity.class);

        // Champs de AuditableEntity
        List<String> auditableEntityFields = getFieldNames(AuditableEntity.class);

        // Champs de TenantAwareEntity
        List<String> tenantAwareEntityFields = getFieldNames(TenantAwareEntity.class);

        // Vérifier Vehicle (TenantAwareEntity)
        List<String> vehicleFields = getFieldNames(Vehicle.class);
        for (String field : tenantAwareEntityFields) {
            assertFalse(vehicleFields.contains(field),
                    "Vehicle ne devrait pas redéfinir le champ '" + field + "' de TenantAwareEntity");
        }
        for (String field : auditableEntityFields) {
            assertFalse(vehicleFields.contains(field),
                    "Vehicle ne devrait pas redéfinir le champ '" + field + "' de AuditableEntity");
        }
        for (String field : baseEntityFields) {
            assertFalse(vehicleFields.contains(field),
                    "Vehicle ne devrait pas redéfinir le champ '" + field + "' de BaseEntity");
        }

        // Vérifier AutoPolicy (TenantAwareEntity)
        List<String> autoPolicyFields = getFieldNames(AutoPolicy.class);
        for (String field : tenantAwareEntityFields) {
            assertFalse(autoPolicyFields.contains(field),
                    "AutoPolicy ne devrait pas redéfinir le champ '" + field + "' de TenantAwareEntity");
        }

        // Vérifier BonusMalus (TenantAwareEntity)
        List<String> bonusMalusFields = getFieldNames(BonusMalus.class);
        for (String field : tenantAwareEntityFields) {
            assertFalse(bonusMalusFields.contains(field),
                    "BonusMalus ne devrait pas redéfinir le champ '" + field + "' de TenantAwareEntity");
        }

        // Vérifier Organization (AuditableEntity)
        List<String> organizationFields = getFieldNames(Organization.class);
        for (String field : auditableEntityFields) {
            assertFalse(organizationFields.contains(field),
                    "Organization ne devrait pas redéfinir le champ '" + field + "' de AuditableEntity");
        }
        for (String field : baseEntityFields) {
            assertFalse(organizationFields.contains(field),
                    "Organization ne devrait pas redéfinir le champ '" + field + "' de BaseEntity");
        }

        // Vérifier User (TenantAwareEntity)
        List<String> userFields = getFieldNames(User.class);
        for (String field : tenantAwareEntityFields) {
            assertFalse(userFields.contains(field),
                    "User ne devrait pas redéfinir le champ '" + field + "' de TenantAwareEntity");
        }

        // Vérifier Role (TenantAwareEntity)
        List<String> roleFields = getFieldNames(Role.class);
        for (String field : tenantAwareEntityFields) {
            assertFalse(roleFields.contains(field),
                    "Role ne devrait pas redéfinir le champ '" + field + "' de TenantAwareEntity");
        }
    }

    /**
     * Vérifie que les getters et setters pour les champs hérités fonctionnent correctement.
     */
    @Test
    void testInheritedGettersAndSetters() {
        // Tester Vehicle (TenantAwareEntity)
        Vehicle vehicle = new Vehicle();
        UUID vehicleId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();

        vehicle.setId(vehicleId);
        vehicle.setOrganizationId(organizationId);
        vehicle.setActive(true);

        assertEquals(vehicleId, vehicle.getId(), "L'ID du véhicule devrait être correctement défini");
        assertEquals(organizationId, vehicle.getOrganizationId(), "L'ID de l'organisation devrait être correctement défini");
        assertTrue(vehicle.isActive(), "Le statut actif devrait être correctement défini");

        // Tester AutoPolicy (TenantAwareEntity)
        AutoPolicy policy = new AutoPolicy();
        UUID policyId = UUID.randomUUID();

        policy.setId(policyId);
        policy.setOrganizationId(organizationId);
        policy.setActive(false);

        assertEquals(policyId, policy.getId(), "L'ID de la police devrait être correctement défini");
        assertEquals(organizationId, policy.getOrganizationId(), "L'ID de l'organisation devrait être correctement défini");
        assertFalse(policy.isActive(), "Le statut actif devrait être correctement défini");

        // Tester Organization (AuditableEntity)
        Organization organization = new Organization();
        UUID orgId = UUID.randomUUID();

        organization.setId(orgId);
        organization.setActive(true);

        assertEquals(orgId, organization.getId(), "L'ID de l'organisation devrait être correctement défini");
        assertTrue(organization.isActive(), "Le statut actif devrait être correctement défini");
    }

    /**
     * Récupère les noms des champs déclarés dans une classe.
     *
     * @param clazz La classe à analyser
     * @return Liste des noms de champs
     */
    private List<String> getFieldNames(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());
    }
}
