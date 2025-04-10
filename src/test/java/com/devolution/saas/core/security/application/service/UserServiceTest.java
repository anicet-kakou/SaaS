package com.devolution.saas.core.security.application.service;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.domain.exception.ValidationException;
import com.devolution.saas.core.security.application.command.ChangePasswordCommand;
import com.devolution.saas.core.security.application.command.CreateUserCommand;
import com.devolution.saas.core.security.application.command.UpdateUserCommand;
import com.devolution.saas.core.security.application.dto.UserDTO;
import com.devolution.saas.core.security.application.usecase.*;
import com.devolution.saas.core.security.domain.model.UserStatus;
import com.devolution.saas.core.security.infrastructure.service.MockTenantContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private CreateUser createUser;

    @Mock
    private UpdateUser updateUser;

    @Mock
    private GetUser getUser;

    @Mock
    private GetUserByUsername getUserByUsername;

    @Mock
    private GetUserByEmail getUserByEmail;

    @Mock
    private ListUsers listUsers;

    @Mock
    private ListUsersByStatus listUsersByStatus;

    @Mock
    private ListUsersByOrganization listUsersByOrganization;

    @Mock
    private ChangePassword changePassword;

    @Mock
    private ActivateUser activateUser;

    @Mock
    private DeactivateUser deactivateUser;

    @Mock
    private LockUser lockUser;

    @Mock
    private UnlockUser unlockUser;

    @Spy
    private MockTenantContextHolder tenantContextHolder = new MockTenantContextHolder();

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private CreateUserCommand createCommand;
    private UpdateUserCommand updateCommand;
    private ChangePasswordCommand changePasswordCommand;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        // Set up tenant context
        tenantContextHolder.setCurrentTenant(UUID.randomUUID());

        // Set up create command
        createCommand = new CreateUserCommand();
        createCommand.setUsername("testuser");
        createCommand.setEmail("test@example.com");
        createCommand.setFirstName("Test");
        createCommand.setLastName("User");
        createCommand.setPassword("password123");
        createCommand.setRoleIds(Arrays.asList(UUID.randomUUID()));

        // Set up update command
        updateCommand = new UpdateUserCommand();
        updateCommand.setId(userId);
        updateCommand.setFirstName("Updated");
        updateCommand.setLastName("User");
        updateCommand.setEmail("updated@example.com");

        // Set up change password command
        changePasswordCommand = new ChangePasswordCommand();
        changePasswordCommand.setUserId(userId);
        changePasswordCommand.setCurrentPassword("oldPassword");
        changePasswordCommand.setNewPassword("newPassword");

        // Set up user DTO
        userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("Test");
        userDTO.setLastName("User");
        userDTO.setStatus(UserStatus.ACTIVE);
    }

    @Test
    void createUser_Success() {
        // Given
        when(createUser.execute(any(CreateUserCommand.class))).thenReturn(userDTO);

        // When
        UserDTO result = userService.createUser(createCommand);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(createUser).execute(createCommand);
    }

    @Test
    void createUser_ValidationError() {
        // Given
        when(createUser.execute(any(CreateUserCommand.class)))
                .thenThrow(new ValidationException("Le nom d'utilisateur est déjà utilisé"));

        // When & Then
        assertThrows(ValidationException.class, () -> {
            userService.createUser(createCommand);
        });

        verify(createUser).execute(createCommand);
    }

    @Test
    void updateUser_Success() {
        // Given
        UserDTO updatedDTO = new UserDTO();
        updatedDTO.setId(userId);
        updatedDTO.setUsername("testuser");
        updatedDTO.setEmail("updated@example.com");
        updatedDTO.setFirstName("Updated");
        updatedDTO.setLastName("User");

        when(updateUser.execute(any(UpdateUserCommand.class))).thenReturn(updatedDTO);

        // When
        UserDTO result = userService.updateUser(updateCommand);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("Updated", result.getFirstName());
        verify(updateUser).execute(updateCommand);
    }

    @Test
    void getUser_Success() {
        // Given
        when(getUser.execute(any(UUID.class))).thenReturn(userDTO);

        // When
        UserDTO result = userService.getUser(userId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("testuser", result.getUsername());
        verify(getUser).execute(userId);
    }

    @Test
    void getUser_NotFound() {
        // Given
        when(getUser.execute(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("User", userId));

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUser(userId);
        });

        verify(getUser).execute(userId);
    }

    @Test
    void getUserByUsername_Success() {
        // Given
        when(getUserByUsername.execute(anyString())).thenReturn(userDTO);

        // When
        UserDTO result = userService.getUserByUsername("testuser");

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("testuser", result.getUsername());
        verify(getUserByUsername).execute("testuser");
    }

    @Test
    void getUserByEmail_Success() {
        // Given
        when(getUserByEmail.execute(anyString())).thenReturn(userDTO);

        // When
        UserDTO result = userService.getUserByEmail("test@example.com");

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("test@example.com", result.getEmail());
        verify(getUserByEmail).execute("test@example.com");
    }

    @Test
    void listUsers_Success() {
        // Given
        List<UserDTO> expectedUsers = Arrays.asList(userDTO, new UserDTO());
        when(listUsers.execute()).thenReturn(expectedUsers);

        // When
        List<UserDTO> result = userService.listUsers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(listUsers).execute();
    }

    @Test
    void listUsersByStatus_Success() {
        // Given
        List<UserDTO> expectedUsers = Arrays.asList(userDTO, new UserDTO());
        when(listUsersByStatus.execute(any(UserStatus.class))).thenReturn(expectedUsers);

        // When
        List<UserDTO> result = userService.listUsersByStatus(UserStatus.ACTIVE);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(listUsersByStatus).execute(UserStatus.ACTIVE);
    }

    @Test
    void listUsersByOrganization_Success() {
        // Given
        UUID organizationId = UUID.randomUUID();
        List<UserDTO> expectedUsers = Arrays.asList(userDTO, new UserDTO());
        when(listUsersByOrganization.execute(any(UUID.class))).thenReturn(expectedUsers);

        // When
        List<UserDTO> result = userService.listUsersByOrganization(organizationId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(listUsersByOrganization).execute(organizationId);
    }

    @Test
    void changePassword_Success() {
        // Given
        doNothing().when(changePassword).execute(any(ChangePasswordCommand.class));

        // When
        userService.changePassword(changePasswordCommand);

        // Then
        verify(changePassword).execute(changePasswordCommand);
    }

    @Test
    void activateUser_Success() {
        // Given
        when(activateUser.execute(any(UUID.class))).thenReturn(userDTO);

        // When
        UserDTO result = userService.activateUser(userId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(activateUser).execute(userId);
    }

    @Test
    void deactivateUser_Success() {
        // Given
        UserDTO deactivatedUser = new UserDTO();
        deactivatedUser.setId(userId);
        deactivatedUser.setUsername("testuser");
        deactivatedUser.setStatus(UserStatus.INACTIVE);

        when(deactivateUser.execute(any(UUID.class))).thenReturn(deactivatedUser);

        // When
        UserDTO result = userService.deactivateUser(userId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(UserStatus.INACTIVE, result.getStatus());
        verify(deactivateUser).execute(userId);
    }

    @Test
    void lockUser_Success() {
        // Given
        UserDTO lockedUser = new UserDTO();
        lockedUser.setId(userId);
        lockedUser.setUsername("testuser");
        lockedUser.setLocked(true);

        when(lockUser.execute(any(UUID.class))).thenReturn(lockedUser);

        // When
        UserDTO result = userService.lockUser(userId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertTrue(result.isLocked());
        verify(lockUser).execute(userId);
    }

    @Test
    void unlockUser_Success() {
        // Given
        UserDTO unlockedUser = new UserDTO();
        unlockedUser.setId(userId);
        unlockedUser.setUsername("testuser");
        unlockedUser.setLocked(false);

        when(unlockUser.execute(any(UUID.class))).thenReturn(unlockedUser);

        // When
        UserDTO result = userService.unlockUser(userId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertFalse(result.isLocked());
        verify(unlockUser).execute(userId);
    }

    @Test
    void executeDelete_UnsupportedOperation() {
        // When & Then
        assertThrows(UnsupportedOperationException.class, () -> {
            userService.executeDelete(userId);
        });
    }
}
