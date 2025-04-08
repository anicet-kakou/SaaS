package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.common.domain.exception.BusinessException;
import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.core.security.application.command.ChangePasswordCommand;
import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Cas d'utilisation pour le changement de mot de passe d'un utilisateur.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ChangePassword {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param command Commande de changement de mot de passe
     */
    @Transactional
    public void execute(ChangePasswordCommand command) {
        log.debug("Changement de mot de passe pour l'utilisateur: {}", command.getUserId());

        // Récupération de l'utilisateur
        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", command.getUserId()));

        // Vérification de l'ancien mot de passe
        if (!passwordEncoder.matches(command.getOldPassword(), user.getPasswordHash())) {
            throw new BusinessException("user.password.incorrect", "L'ancien mot de passe est incorrect");
        }

        // Mise à jour du mot de passe
        user.setPasswordHash(passwordEncoder.encode(command.getNewPassword()));
        user.setLastPasswordChangeDate(LocalDateTime.now());
        userRepository.save(user);
    }
}
