package com.devolution.saas.core.security.api;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.domain.exception.ValidationException;
import com.devolution.saas.core.security.application.command.ChangePasswordCommand;
import com.devolution.saas.core.security.application.command.CreateUserCommand;
import com.devolution.saas.core.security.application.command.UpdateUserCommand;
import com.devolution.saas.core.security.application.dto.UserDTO;
import com.devolution.saas.core.security.application.service.SecurityService;
import com.devolution.saas.core.security.application.service.UserService;
import com.devolution.saas.core.security.domain.model.UserStatus;
import com.devolution.saas.core.security.infrastructure.service.TenantContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private TenantContextHolder tenantContextHolder;

    private UUID userId;
    private UserDTO userDTO;
    private CreateUserCommand createCommand;
    private UpdateUserCommand updateCommand;
    private ChangePasswordCommand changePasswordCommand;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        // Set up tenant context
        when(tenantContextHolder.getCurrentTenant()).thenReturn(UUID.randomUUID());
        when(tenantContextHolder.hasTenant()).thenReturn(true);

        // Set up security service for authorization checks
        when(securityService.isCurrentUser(any(UUID.class))).thenReturn(true);
        when(securityService.isCurrentUsername(anyString())).thenReturn(true);
        when(securityService.isCurrentUserEmail(anyString())).thenReturn(true);

        // Set up user DTO
        userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("Test");
        userDTO.setLastName("User");
        userDTO.setStatus(UserStatus.ACTIVE);

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
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_Success() throws Exception {
        when(userService.createUser(any(CreateUserCommand.class)))
                .thenReturn(userDTO);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(userId.toString())))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.lastName", is("User")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_ValidationError() throws Exception {
        when(userService.createUser(any(CreateUserCommand.class)))
                .thenThrow(new ValidationException("Le nom d'utilisateur est déjà utilisé"));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUser_Success() throws Exception {
        when(userService.getUser(any(UUID.class)))
                .thenReturn(userDTO);

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.toString())))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.lastName", is("User")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUser_NotFound() throws Exception {
        when(userService.getUser(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("User", userId));

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserByUsername_Success() throws Exception {
        when(userService.getUserByUsername(anyString()))
                .thenReturn(userDTO);

        mockMvc.perform(get("/api/v1/users/username/{username}", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.toString())))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserByEmail_Success() throws Exception {
        when(userService.getUserByEmail(anyString()))
                .thenReturn(userDTO);

        mockMvc.perform(get("/api/v1/users/email/{email}", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.toString())))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser_Success() throws Exception {
        UserDTO updatedDTO = new UserDTO();
        updatedDTO.setId(userId);
        updatedDTO.setUsername("testuser");
        updatedDTO.setEmail("updated@example.com");
        updatedDTO.setFirstName("Updated");
        updatedDTO.setLastName("User");

        when(userService.updateUser(any(UpdateUserCommand.class)))
                .thenReturn(updatedDTO);

        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCommand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.toString())))
                .andExpect(jsonPath("$.firstName", is("Updated")))
                .andExpect(jsonPath("$.email", is("updated@example.com")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listUsers_Success() throws Exception {
        List<UserDTO> users = Arrays.asList(
                userDTO,
                new UserDTO()
        );

        when(userService.listUsers())
                .thenReturn(users);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(userId.toString())))
                .andExpect(jsonPath("$[0].username", is("testuser")))
                .andExpect(jsonPath("$[0].email", is("test@example.com")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listUsersByStatus_Success() throws Exception {
        List<UserDTO> users = Arrays.asList(
                userDTO,
                new UserDTO()
        );

        when(userService.listUsersByStatus(any(UserStatus.class)))
                .thenReturn(users);

        mockMvc.perform(get("/api/v1/users/status/{status}", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(userId.toString())))
                .andExpect(jsonPath("$[0].username", is("testuser")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listUsersByOrganization_Success() throws Exception {
        UUID organizationId = UUID.randomUUID();
        List<UserDTO> users = Arrays.asList(
                userDTO,
                new UserDTO()
        );

        when(userService.listUsersByOrganization(any(UUID.class)))
                .thenReturn(users);

        mockMvc.perform(get("/api/v1/users/organization/{organizationId}", organizationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(userId.toString())))
                .andExpect(jsonPath("$[0].username", is("testuser")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void changePassword_Success() throws Exception {
        doNothing().when(userService).changePassword(any(ChangePasswordCommand.class));

        mockMvc.perform(post("/api/v1/users/{id}/change-password", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordCommand)))
                .andExpect(status().isNoContent());

        verify(userService).changePassword(any(ChangePasswordCommand.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void activateUser_Success() throws Exception {
        when(userService.activateUser(any(UUID.class)))
                .thenReturn(userDTO);

        mockMvc.perform(put("/api/v1/users/{id}/activate", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.toString())))
                .andExpect(jsonPath("$.username", is("testuser")));

        verify(userService).activateUser(userId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deactivateUser_Success() throws Exception {
        UserDTO deactivatedUser = new UserDTO();
        deactivatedUser.setId(userId);
        deactivatedUser.setUsername("testuser");
        deactivatedUser.setStatus(UserStatus.INACTIVE);

        when(userService.deactivateUser(any(UUID.class)))
                .thenReturn(deactivatedUser);

        mockMvc.perform(put("/api/v1/users/{id}/deactivate", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.toString())))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.status", is("INACTIVE")));

        verify(userService).deactivateUser(userId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void lockUser_Success() throws Exception {
        UserDTO lockedUser = new UserDTO();
        lockedUser.setId(userId);
        lockedUser.setUsername("testuser");
        lockedUser.setLocked(true);

        when(userService.lockUser(any(UUID.class)))
                .thenReturn(lockedUser);

        mockMvc.perform(put("/api/v1/users/{id}/lock", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.toString())))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.locked", is(true)));

        verify(userService).lockUser(userId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void unlockUser_Success() throws Exception {
        UserDTO unlockedUser = new UserDTO();
        unlockedUser.setId(userId);
        unlockedUser.setUsername("testuser");
        unlockedUser.setLocked(false);

        when(userService.unlockUser(any(UUID.class)))
                .thenReturn(unlockedUser);

        mockMvc.perform(put("/api/v1/users/{id}/unlock", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.toString())))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.locked", is(false)));

        verify(userService).unlockUser(userId);
    }
}
