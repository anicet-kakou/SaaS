package com.devolution.saas.core.security.application.usecase;

import com.devolution.saas.core.security.application.dto.UserDTO;
import com.devolution.saas.core.security.application.mapper.UserMapper;
import com.devolution.saas.core.security.domain.model.User;
import com.devolution.saas.core.security.domain.model.UserStatus;
import com.devolution.saas.core.security.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Cas d'utilisation pour la liste des utilisateurs par statut.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ListUsersByStatus {

    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    /**
     * Exécute le cas d'utilisation.
     *
     * @param status Statut des utilisateurs
     * @return Liste des DTOs d'utilisateurs
     */
    @Transactional(readOnly = true)
    public List<UserDTO> execute(UserStatus status) {
        log.debug("Listage des utilisateurs par statut: {}", status);

        // Création de la spécification de base
        Specification<User> spec = Specification.where(null);

        // Ajout du critère de statut
        spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status));

        // Exécution de la requête avec la spécification
        return jpaUserRepository.findAll(spec).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}
