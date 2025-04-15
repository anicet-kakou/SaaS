package com.devolution.saas.common.abstracts;

import com.devolution.saas.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the TenantAwareCrudController class.
 */
class TenantAwareCrudControllerTest {

    private TestController controller;
    private UUID organizationId;
    private UUID entityId;
    private TestEntity testEntity;
    private TestDTO testDTO;
    private List<TestDTO> activeEntities;
    private List<TestDTO> allEntities;

    @BeforeEach
    void setUp() {
        organizationId = UUID.randomUUID();
        entityId = UUID.randomUUID();
        testEntity = new TestEntity(entityId, "Test Entity", true);
        testDTO = new TestDTO(entityId, "Test Entity", true);

        activeEntities = Arrays.asList(
                new TestDTO(UUID.randomUUID(), "Active Entity 1", true),
                new TestDTO(UUID.randomUUID(), "Active Entity 2", true)
        );

        allEntities = Arrays.asList(
                new TestDTO(UUID.randomUUID(), "Entity 1", true),
                new TestDTO(UUID.randomUUID(), "Entity 2", false),
                new TestDTO(UUID.randomUUID(), "Entity 3", true)
        );

        controller = new TestController(
                activeEntities,
                allEntities,
                testDTO,
                testDTO,
                testDTO,
                testDTO,
                testDTO,
                true
        );
    }

    @Test
    void listActiveEntities_ShouldReturnActiveEntities() {
        // When
        ResponseEntity<List<TestDTO>> response = controller.listActiveEntities(organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activeEntities, response.getBody());
    }

    @Test
    void listAllEntities_ShouldReturnAllEntities() {
        // When
        ResponseEntity<List<TestDTO>> response = controller.listAllEntities(organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(allEntities, response.getBody());
    }

    @Test
    void getEntity_ShouldReturnEntityById() {
        // When
        ResponseEntity<TestDTO> response = controller.getEntity(entityId, organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDTO, response.getBody());
    }

    @Test
    void getEntity_ShouldThrowResourceNotFoundException_WhenEntityNotFound() {
        // Given
        controller = new TestController(
                activeEntities,
                allEntities,
                null,
                testDTO,
                testDTO,
                testDTO,
                testDTO,
                true
        );

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.getEntity(entityId, organizationId));
    }

    @Test
    void getEntityByCode_ShouldReturnEntityByCode() {
        // When
        ResponseEntity<TestDTO> response = controller.getEntityByCode("TEST", organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDTO, response.getBody());
    }

    @Test
    void getEntityByCode_ShouldThrowResourceNotFoundException_WhenEntityNotFound() {
        // Given
        controller = new TestController(
                activeEntities,
                allEntities,
                testDTO,
                null,
                testDTO,
                testDTO,
                testDTO,
                true
        );

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.getEntityByCode("TEST", organizationId));
    }

    @Test
    void createEntity_ShouldReturnCreatedEntity() {
        // When
        ResponseEntity<TestDTO> response = controller.createEntity(testEntity, organizationId);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testDTO, response.getBody());
    }

    @Test
    void updateEntity_ShouldReturnUpdatedEntity() {
        // When
        ResponseEntity<TestDTO> response = controller.updateEntity(entityId, testEntity, organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDTO, response.getBody());
    }

    @Test
    void updateEntity_ShouldThrowResourceNotFoundException_WhenEntityNotFound() {
        // Given
        controller = new TestController(
                activeEntities,
                allEntities,
                testDTO,
                testDTO,
                testDTO,
                null,
                testDTO,
                true
        );

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.updateEntity(entityId, testEntity, organizationId));
    }

    @Test
    void activateEntity_ShouldReturnActivatedEntity() {
        // When
        ResponseEntity<TestDTO> response = controller.activateEntity(entityId, organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDTO, response.getBody());
    }

    @Test
    void activateEntity_ShouldThrowResourceNotFoundException_WhenEntityNotFound() {
        // Given
        controller = new TestController(
                activeEntities,
                allEntities,
                testDTO,
                testDTO,
                testDTO,
                testDTO,
                null,
                true
        );

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.activateEntity(entityId, organizationId));
    }

    @Test
    void deactivateEntity_ShouldReturnDeactivatedEntity() {
        // When
        ResponseEntity<TestDTO> response = controller.deactivateEntity(entityId, organizationId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDTO, response.getBody());
    }

    @Test
    void deactivateEntity_ShouldThrowResourceNotFoundException_WhenEntityNotFound() {
        // Given
        controller = new TestController(
                activeEntities,
                allEntities,
                testDTO,
                testDTO,
                testDTO,
                testDTO,
                null,
                true
        );

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.deactivateEntity(entityId, organizationId));
    }

    @Test
    void deleteEntity_ShouldReturnNoContent() {
        // When
        ResponseEntity<Void> response = controller.deleteEntity(entityId, organizationId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteEntity_ShouldThrowResourceNotFoundException_WhenEntityNotFound() {
        // Given
        controller = new TestController(
                activeEntities,
                allEntities,
                testDTO,
                testDTO,
                testDTO,
                testDTO,
                testDTO,
                false
        );

        // When/Then
        assertThrows(ResourceNotFoundException.class, () ->
                controller.deleteEntity(entityId, organizationId));
    }

    // Test entity class
    private static class TestEntity {
        private UUID id;
        private String name;
        private boolean active;

        public TestEntity(UUID id, String name, boolean active) {
            this.id = id;
            this.name = name;
            this.active = active;
        }

        public UUID getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public boolean isActive() {
            return active;
        }
    }

    // Test DTO class
    private static class TestDTO {
        private UUID id;
        private String name;
        private boolean active;

        public TestDTO(UUID id, String name, boolean active) {
            this.id = id;
            this.name = name;
            this.active = active;
        }

        public UUID getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public boolean isActive() {
            return active;
        }
    }

    // Test controller implementation
    private static class TestController extends TenantAwareCrudController<TestDTO, TestEntity, TestEntity> {
        private final List<TestDTO> activeEntities;
        private final List<TestDTO> allEntities;
        private final TestDTO entityById;
        private final TestDTO entityByCode;
        private final TestDTO createdEntity;
        private final TestDTO updatedEntity;
        private final TestDTO activatedEntity;
        private boolean deleteSuccess;

        public TestController(List<TestDTO> activeEntities, List<TestDTO> allEntities,
                              TestDTO entityById, TestDTO entityByCode,
                              TestDTO createdEntity, TestDTO updatedEntity,
                              TestDTO activatedEntity, boolean deleteSuccess) {
            this.activeEntities = activeEntities;
            this.allEntities = allEntities;
            this.entityById = entityById;
            this.entityByCode = entityByCode;
            this.createdEntity = createdEntity;
            this.updatedEntity = updatedEntity;
            this.activatedEntity = activatedEntity;
            this.deleteSuccess = deleteSuccess;
        }

        @Override
        protected List<TestDTO> listActive(UUID organizationId) {
            return activeEntities;
        }

        @Override
        protected List<TestDTO> list(UUID organizationId) {
            return allEntities;
        }

        @Override
        protected TestDTO get(UUID id, UUID organizationId) {
            if (entityById == null) {
                throw ResourceNotFoundException.forId("Test entity", id);
            }
            return entityById;
        }

        @Override
        protected TestDTO getByCode(String code, UUID organizationId) {
            if (entityByCode == null) {
                throw ResourceNotFoundException.forCode("Test entity", code);
            }
            return entityByCode;
        }

        @Override
        protected TestDTO create(TestEntity command, UUID organizationId) {
            return createdEntity;
        }

        @Override
        protected TestDTO update(UUID id, TestEntity command, UUID organizationId) {
            if (updatedEntity == null) {
                throw ResourceNotFoundException.forId("Test entity", id);
            }
            return updatedEntity;
        }

        @Override
        protected TestDTO setActive(UUID id, boolean active, UUID organizationId) {
            if (activatedEntity == null) {
                throw ResourceNotFoundException.forId("Test entity", id);
            }
            return activatedEntity;
        }

        @Override
        protected void delete(UUID id, UUID organizationId) {
            if (!deleteSuccess) {
                throw ResourceNotFoundException.forId("Test entity", id);
            }
        }

        @Override
        protected String getEntityName() {
            return "test entity";
        }
    }
}
