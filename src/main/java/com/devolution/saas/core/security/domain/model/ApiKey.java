package com.devolution.saas.core.security.domain.model;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant une clé API dans le système.
 * Une clé API permet l'authentification des systèmes externes et des intégrations.
 */
@Entity
@Table(name = "api_keys")
@Getter
@Setter
public class ApiKey extends TenantAwareEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Nom de la clé API.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Préfixe de la clé API (partie visible).
     */
    @Column(name = "prefix", nullable = false, length = 8)
    private String prefix;

    /**
     * Hash de la clé API.
     */
    @Column(name = "key_hash", nullable = false)
    private String keyHash;

    /**
     * Statut de la clé API (ACTIVE, INACTIVE, REVOKED).
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApiKeyStatus status = ApiKeyStatus.ACTIVE;

    /**
     * Date d'expiration de la clé API.
     */
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    /**
     * Date de dernière utilisation de la clé API.
     */
    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    /**
     * Permissions associées à la clé API au format JSON.
     */
    @Column(name = "permissions", columnDefinition = "jsonb")
    private String permissions;

    /**
     * Adresses IP autorisées à utiliser cette clé API.
     */
    @ElementCollection
    @CollectionTable(name = "api_key_allowed_ips", joinColumns = @JoinColumn(name = "api_key_id"))
    @Column(name = "ip_address")
    private Set<String> allowedIps = new HashSet<>();

    /**
     * Limite de taux de requêtes par minute.
     */
    @Column(name = "rate_limit")
    private Integer rateLimit;

    /**
     * Description de la clé API.
     */
    @Column(name = "description")
    private String description;

    /**
     * Vérifie si la clé API est active.
     *
     * @return true si la clé API est active, false sinon
     */
    public boolean isActive() {
        return status == ApiKeyStatus.ACTIVE &&
                (expiresAt == null || expiresAt.isAfter(LocalDateTime.now()));
    }

    /**
     * Met à jour la date de dernière utilisation.
     */
    public void updateLastUsed() {
        this.lastUsedAt = LocalDateTime.now();
    }

    /**
     * Révoque la clé API.
     */
    public void revoke() {
        this.status = ApiKeyStatus.REVOKED;
    }

    /**
     * Désactive la clé API.
     */
    public void deactivate() {
        this.status = ApiKeyStatus.INACTIVE;
    }

    /**
     * Active la clé API.
     */
    public void activate() {
        this.status = ApiKeyStatus.ACTIVE;
    }

    /**
     * Vérifie si l'adresse IP est autorisée.
     *
     * @param ipAddress Adresse IP à vérifier
     * @return true si l'adresse IP est autorisée ou si aucune restriction n'est définie, false sinon
     */
    public boolean isIpAllowed(String ipAddress) {
        return allowedIps.isEmpty() || allowedIps.contains(ipAddress);
    }
}
