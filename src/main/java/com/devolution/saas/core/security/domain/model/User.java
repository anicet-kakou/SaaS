package com.devolution.saas.core.security.domain.model;

import com.devolution.saas.common.domain.model.TenantAwareEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entité représentant un utilisateur dans le système.
 * Implémente UserDetails pour l'intégration avec Spring Security.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends TenantAwareEntity implements UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * Nom d'utilisateur unique.
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * Adresse email unique.
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Hash du mot de passe.
     */
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    /**
     * Prénom de l'utilisateur.
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * Nom de famille de l'utilisateur.
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Numéro de téléphone de l'utilisateur.
     */
    @Column(name = "phone")
    private String phone;

    /**
     * Statut de l'utilisateur (ACTIVE, INACTIVE, SUSPENDED, etc.).
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    /**
     * Indique si le compte est verrouillé.
     */
    @Column(name = "locked", nullable = false)
    private boolean locked = false;

    /**
     * Nombre de tentatives de connexion échouées.
     */
    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts = 0;

    /**
     * Date et heure de la dernière connexion.
     */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    /**
     * URL de la photo de profil.
     */
    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    /**
     * Indique si le compte n'a pas expiré.
     */
    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired = true;

    /**
     * Indique si le compte n'est pas verrouillé.
     */
    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked = true;

    /**
     * Indique si les identifiants n'ont pas expiré.
     */
    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired = true;

    /**
     * Indique si le compte est activé.
     */
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    /**
     * Date du dernier changement de mot de passe.
     */
    @Column(name = "last_password_change_date")
    private LocalDateTime lastPasswordChangeDate;

    /**
     * Rôles de l'utilisateur.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * Organisations auxquelles l'utilisateur appartient.
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Set<UserOrganization> organizations = new HashSet<>();

    /**
     * Ajoute un rôle à l'utilisateur.
     *
     * @param role Rôle à ajouter
     */
    public void addRole(Role role) {
        roles.add(role);
    }

    /**
     * Supprime un rôle de l'utilisateur.
     *
     * @param role Rôle à supprimer
     */
    public void removeRole(Role role) {
        roles.remove(role);
    }

    /**
     * Ajoute une organisation à l'utilisateur.
     *
     * @param organization Organisation à ajouter
     */
    public void addOrganization(UserOrganization organization) {
        organizations.add(organization);
    }

    /**
     * Supprime une organisation de l'utilisateur.
     *
     * @param organization Organisation à supprimer
     */
    public void removeOrganization(UserOrganization organization) {
        organizations.remove(organization);
    }

    /**
     * Incrémente le compteur de tentatives de connexion échouées.
     */
    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 5) {
            this.locked = true;
            this.accountNonLocked = false;
        }
    }

    /**
     * Réinitialise le compteur de tentatives de connexion échouées.
     */
    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
        this.locked = false;
        this.accountNonLocked = true;
    }

    /**
     * Enregistre une connexion réussie.
     */
    public void recordSuccessfulLogin() {
        this.lastLoginAt = LocalDateTime.now();
        resetFailedLoginAttempts();
    }

    /**
     * Verrouille le compte utilisateur.
     */
    public void lock() {
        this.locked = true;
        this.accountNonLocked = false;
    }

    /**
     * Déverrouille le compte utilisateur.
     */
    public void unlock() {
        this.locked = false;
        this.accountNonLocked = true;
        resetFailedLoginAttempts();
    }

    /**
     * Désactive le compte utilisateur.
     */
    public void disable() {
        this.enabled = false;
        super.deactivate();
    }

    /**
     * Active le compte utilisateur.
     */
    public void enable() {
        this.enabled = true;
        super.activate();
    }

    /**
     * Retourne le nom complet de l'utilisateur.
     *
     * @return Nom complet
     */
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        } else {
            return username;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Ajoute les rôles comme autorités
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

            // Ajoute les permissions des rôles comme autorités
            authorities.addAll(role.getPermissions().stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                    .collect(Collectors.toSet()));
        }

        return authorities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return passwordHash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
