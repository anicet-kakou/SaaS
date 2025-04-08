package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.core.security.application.dto.UserDTO;
import com.devolution.saas.core.security.application.mapper.UserMapper;
import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Cas d'utilisation pour la liste des utilisateurs par organisation.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ListUsersByOrganization {

    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des DTOs d'utilisateurs
     */
    @Transactional(readOnly = true)
    public List<UserDTO> execute(UUID organizationId) {
        log.debug("Listage des utilisateurs par organisation: {}", organizationId);

        // Création de la spécification de base
        Specification<User> spec = (root, criteriaQuery, criteriaBuilder) -> {
            // Jointure avec la table user_organizations
            return criteriaBuilder.equal(
                    root.join("organizations").get("organizationId"),
                    organizationId
            );
        };

        // Exécution de la requête avec la spécification
        return jpaUserRepository.findAll(spec).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}
