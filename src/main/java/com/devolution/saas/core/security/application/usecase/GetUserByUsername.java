package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.core.security.application.dto.UserDTO;
import com.devolution.saas.core.security.application.mapper.UserMapper;
import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cas d'utilisation pour la récupération d'un utilisateur par son nom d'utilisateur.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GetUserByUsername {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param username Nom d'utilisateur
     * @return DTO de l'utilisateur
     */
    @Transactional(readOnly = true)
    public UserDTO execute(String username) {
        log.debug("Récupération de l'utilisateur par nom d'utilisateur: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));

        return userMapper.toDTO(user);
    }
}
