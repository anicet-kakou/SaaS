package com.devolution.saas.core.security.domain.repository;

import com.devolution.saas.core.security.domain.model.RefreshToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de repository pour les opérations sur les jetons de rafraîchissement.
 */
public interface RefreshTokenRepository {

    /**
     * Enregistre un jeton de rafraîchissement.
     *
     * @param refreshToken Jeton de rafraîchissement à enregistrer
     * @return Jeton de rafraîchissement enregistré
     */
    RefreshToken save(RefreshToken refreshToken);

    /**
     * Trouve un jeton de rafraîchissement par son ID.
     *
     * @param id ID du jeton de rafraîchissement
     * @return Jeton de rafraîchissement trouvé ou Optional vide
     */
    Optional<RefreshToken> findById(UUID id);

    /**
     * Trouve un jeton de rafraîchissement par sa valeur.
     *
     * @param token Valeur du jeton de rafraîchissement
     * @return Jeton de rafraîchissement trouvé ou Optional vide
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Trouve tous les jetons de rafraîchissement d'un utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @return Liste des jetons de rafraîchissement
     */
    List<RefreshToken> findAllByUserId(UUID userId);

    /**
     * Trouve tous les jetons de rafraîchissement expirés.
     *
     * @param expiryDate Date d'expiration
     * @return Liste des jetons de rafraîchissement expirés
     */
    List<RefreshToken> findAllByExpiresAtBefore(LocalDateTime expiryDate);

    /**
     * Supprime un jeton de rafraîchissement.
     *
     * @param refreshToken Jeton de rafraîchissement à supprimer
     */
    void delete(RefreshToken refreshToken);

    /**
     * Supprime tous les jetons de rafraîchissement d'un utilisateur.
     *
     * @param userId ID de l'utilisateur
     */
    void deleteAllByUserId(UUID userId);

    /**
     * Supprime tous les jetons de rafraîchissement expirés.
     *
     * @param expiryDate Date d'expiration
     */
    void deleteAllByExpiresAtBefore(LocalDateTime expiryDate);
}
