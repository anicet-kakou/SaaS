package com.devolution.saas.core.security.infrastructure.persistence;

import com.devolution.saas.core.security.domain.model.RefreshToken;
import com.devolution.saas.core.security.domain.repository.RefreshTokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation JPA du repository pour les opérations sur les jetons de rafraîchissement.
 */
@Repository
public interface JpaRefreshTokenRepository extends JpaRepository<RefreshToken, UUID>, RefreshTokenRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<RefreshToken> findByToken(String token);

    /**
     * {@inheritDoc}
     */
    @Override
    List<RefreshToken> findAllByUserId(UUID userId);

    /**
     * {@inheritDoc}
     */
    @Override
    List<RefreshToken> findAllByExpiresAtBefore(LocalDateTime expiryDate);

    /**
     * {@inheritDoc}
     */
    @Override
    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.userId = :userId")
    void deleteAllByUserId(@Param("userId") UUID userId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.expiresAt < :expiryDate")
    void deleteAllByExpiresAtBefore(@Param("expiryDate") LocalDateTime expiryDate);
}
