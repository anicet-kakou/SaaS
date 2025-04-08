package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.core.security.application.dto.UserDTO;
import com.devolution.saas.core.security.application.mapper.UserMapper;
import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.domain.model.UserStatus;
import com.devolution.saas.core.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Cas d'utilisation pour l'activation d'un utilisateur.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ActivateUser {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param id ID de l'utilisateur
     * @return DTO de l'utilisateur activé
     */
    @Transactional
    public UserDTO execute(UUID id) {
        log.debug("Activation de l'utilisateur: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        user.setStatus(UserStatus.ACTIVE);
        user.enable();
        user = userRepository.save(user);

        return userMapper.toDTO(user);
    }
}
