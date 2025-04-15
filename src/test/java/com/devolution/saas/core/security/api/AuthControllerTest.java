package com.devolution.saas.core.security.api;

import com.devolution.saas.common.domain.exception.BusinessException;
import com.devolution.saas.core.security.application.command.LoginCommand;
import com.devolution.saas.core.security.application.command.RefreshTokenCommand;
import com.devolution.saas.core.security.application.command.RegisterCommand;
import com.devolution.saas.core.security.application.dto.JwtAuthenticationResponse;
import com.devolution.saas.core.security.application.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    private LoginCommand loginCommand;
    private RegisterCommand registerCommand;
    private RefreshTokenCommand refreshTokenCommand;
    private JwtAuthenticationResponse authResponse;

    @BeforeEach
    void setUp() {
        // Set up login command
        loginCommand = LoginCommand.builder()
                .usernameOrEmail("testuser")
                .password("password123")
                .build();

        // Set up register command
        registerCommand = new RegisterCommand();
        registerCommand.setUsername("newuser");
        registerCommand.setEmail("newuser@example.com");
        registerCommand.setPassword("password123");
        registerCommand.setFirstName("New");
        registerCommand.setLastName("User");

        // Set up refresh token command
        refreshTokenCommand = RefreshTokenCommand.builder()
                .refreshToken("refresh-token-value")
                .build();

        // Set up authentication response
        authResponse = new JwtAuthenticationResponse();
        authResponse.setAccessToken("access-token-value");
        authResponse.setRefreshToken("refresh-token-value");
        authResponse.setTokenType("Bearer");
        authResponse.setExpiresIn(3600);
        authResponse.setUserId(UUID.randomUUID());
        authResponse.setUsername("testuser");
        authResponse.setRoles(Arrays.asList("USER"));
    }

    @Test
    void login_Success() throws Exception {
        when(authenticationService.login(any(LoginCommand.class)))
                .thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginCommand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", is("access-token-value")))
                .andExpect(jsonPath("$.refreshToken", is("refresh-token-value")))
                .andExpect(jsonPath("$.tokenType", is("Bearer")))
                .andExpect(jsonPath("$.expiresIn", is(3600)))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.roles", hasSize(1)))
                .andExpect(jsonPath("$.roles[0]", is("USER")));
    }

    @Test
    void login_InvalidCredentials() throws Exception {
        when(authenticationService.login(any(LoginCommand.class)))
                .thenThrow(new BusinessException("auth.invalid.credentials", "Invalid username or password"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_Success() throws Exception {
        when(authenticationService.register(any(RegisterCommand.class)))
                .thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerCommand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", is("access-token-value")))
                .andExpect(jsonPath("$.refreshToken", is("refresh-token-value")))
                .andExpect(jsonPath("$.tokenType", is("Bearer")))
                .andExpect(jsonPath("$.expiresIn", is(3600)))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.roles", hasSize(1)))
                .andExpect(jsonPath("$.roles[0]", is("USER")));
    }

    @Test
    void register_UserAlreadyExists() throws Exception {
        when(authenticationService.register(any(RegisterCommand.class)))
                .thenThrow(new BusinessException("auth.user.exists", "Username or email already exists"));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refreshToken_Success() throws Exception {
        when(authenticationService.refreshToken(any(RefreshTokenCommand.class)))
                .thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenCommand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", is("access-token-value")))
                .andExpect(jsonPath("$.refreshToken", is("refresh-token-value")))
                .andExpect(jsonPath("$.tokenType", is("Bearer")))
                .andExpect(jsonPath("$.expiresIn", is(3600)))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.roles", hasSize(1)))
                .andExpect(jsonPath("$.roles[0]", is("USER")));
    }

    @Test
    void refreshToken_InvalidToken() throws Exception {
        when(authenticationService.refreshToken(any(RefreshTokenCommand.class)))
                .thenThrow(new BusinessException("auth.refresh.invalid", "Invalid refresh token"));

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void logout_Success() throws Exception {
        doNothing().when(authenticationService).logout(anyString());

        mockMvc.perform(post("/api/v1/auth/logout")
                        .param("refreshToken", "refresh-token-value"))
                .andExpect(status().isOk());

        verify(authenticationService).logout("refresh-token-value");
    }
}
