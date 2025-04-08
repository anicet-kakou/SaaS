package com.devolution.saas.core.security.infrastructure.adapter.out;

import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.domain.port.out.AuthenticationPort;
import com.devolution.saas.core.security.domain.repository.UserRepository;
import com.devolution.saas.core.security.infrastructure.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Adapter pour l'authentification locale.
 * Implémente le port d'authentification pour l'authentification locale.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LocalAuthAdapter implements AuthenticationPort {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> authenticateWithCredentials(String usernameOrEmail, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameOrEmail, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userRepository.findByUsername(userDetails.getUsername());
        } catch (Exception e) {
            log.error("Échec de l'authentification locale", e);
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> authenticateWithToken(String token) {
        if (validateToken(token)) {
            String username = jwtTokenProvider.getUsername(token);
            return userRepository.findByUsername(username);
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> authenticateWithCode(String code, String redirectUri) {
        // Non applicable pour l'authentification locale
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Map<String, Object>> getUserInfo(String token) {
        if (validateToken(token)) {
            String username = jwtTokenProvider.getUsername(token);
            return userRepository.findByUsername(username)
                    .map(user -> {
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("id", user.getId());
                        userInfo.put("username", user.getUsername());
                        userInfo.put("email", user.getEmail());
                        userInfo.put("roles", user.getRoles());
                        return userInfo;
                    });
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> refreshToken(String refreshToken) {
        // Délégué au service de refresh token existant
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean revokeToken(String token) {
        // Délégué au service de révocation de token existant
        return false;
    }
}
