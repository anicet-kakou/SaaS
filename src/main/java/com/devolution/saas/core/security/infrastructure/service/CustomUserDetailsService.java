package com.devolution.saas.core.security.infrastructure.service;

import com.devolution.saas.core.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service personnalisé pour charger les utilisateurs par nom d'utilisateur ou email.
 * Implémente l'interface UserDetailsService de Spring Security.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Charge un utilisateur par son nom d'utilisateur ou son email.
     *
     * @param usernameOrEmail Nom d'utilisateur ou email
     * @return UserDetails de l'utilisateur trouvé
     * @throws UsernameNotFoundException Si l'utilisateur n'est pas trouvé
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        log.debug("Chargement de l'utilisateur par nom d'utilisateur ou email: {}", usernameOrEmail);

        return userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException(
                                "Utilisateur non trouvé avec le nom d'utilisateur ou l'email: " + usernameOrEmail)));
    }
}
