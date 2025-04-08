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

import java.util.UUID;

/**
 * Cas d'utilisation pour la récupération d'un utilisateur par son ID.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GetUser {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param id ID de l'utilisateur
     * @return DTO de l'utilisateur
     */
    @Transactional(readOnly = true)
    public UserDTO execute(UUID id) {
        log.debug("Récupération de l'utilisateur: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        return userMapper.toDTO(user);
    }
}
