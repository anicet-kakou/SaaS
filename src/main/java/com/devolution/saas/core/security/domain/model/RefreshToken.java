package com.devolution.saas.core.security.domain.model;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité représentant un jeton de rafraîchissement.
 * Utilisé pour générer de nouveaux jetons d'accès JWT sans demander à l'utilisateur de se reconnecter.
 */
@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
public class RefreshToken extends TenantAwareEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Jeton de rafraîchissement.
     */
    @Column(name = "token", nullable = false, unique = true)
    private String token;

    /**
     * ID de l'utilisateur associé au jeton.
     */
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * Date d'expiration du jeton.
     */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Indique si le jeton est actif.
     */
    @Column(name = "active", nullable = false)
    private boolean active = true;

    /**
     * Constructeur par défaut.
     */
    public RefreshToken() {
    }

    /**
     * Constructeur avec tous les champs.
     *
     * @param token     Jeton de rafraîchissement
     * @param userId    ID de l'utilisateur
     * @param expiresAt Date d'expiration
     */
    public RefreshToken(String token, UUID userId, LocalDateTime expiresAt) {
        this.token = token;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    /**
     * Vérifie si le jeton est expiré.
     *
     * @return true si le jeton est expiré, false sinon
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    /**
     * Vérifie si le jeton est valide.
     *
     * @return true si le jeton est valide, false sinon
     */
    public boolean isValid() {
        return !isExpired() && active;
    }

    /**
     * Révoque le jeton.
     */
    public void revoke() {
        this.active = false;
    }
}
