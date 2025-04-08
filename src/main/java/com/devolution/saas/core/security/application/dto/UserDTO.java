package com.devolution.saas.core.security.application.dto;

import com.devolution.saas.core.security.domain.model.UserStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * DTO pour les utilisateurs.
 *
 * @param id                Identifiant unique de l'utilisateur
 * @param username          Nom d'utilisateur
 * @param email             Adresse email
 * @param firstName         Prénom
 * @param lastName          Nom de famille
 * @param phone             Numéro de téléphone
 * @param status            Statut de l'utilisateur
 * @param profilePictureUrl URL de la photo de profil
 * @param lastLoginAt       Date et heure de la dernière connexion
 * @param createdAt         Date et heure de création
 * @param updatedAt         Date et heure de dernière mise à jour
 * @param organizationId    ID de l'organisation principale
 * @param active            Indique si l'utilisateur est actif
 * @param locked            Indique si le compte est verrouillé
 * @param roles             Rôles de l'utilisateur
 * @param organizations     Organisations auxquelles l'utilisateur appartient
 */
public record UserDTO(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        String phone,
        UserStatus status,
        String profilePictureUrl,
        LocalDateTime lastLoginAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID organizationId,
        boolean active,
        boolean locked,
        Set<RoleDTO> roles,
        Set<UserOrganizationDTO> organizations
) {
    /**
     * Constructeur compact avec valeurs par défaut pour les collections.
     */
    public UserDTO {
        roles = roles != null ? roles : new HashSet<>();
        organizations = organizations != null ? organizations : new HashSet<>();
    }

    /**
     * Crée un nouveau builder pour UserDTO.
     *
     * @return Builder pour UserDTO
     */
    public static Builder builder() {
        return new Builder();
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
     * Builder pattern pour UserDTO.
     */
    public static class Builder {
        private UUID id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private String phone;
        private UserStatus status;
        private String profilePictureUrl;
        private LocalDateTime lastLoginAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private UUID organizationId;
        private boolean active;
        private boolean locked;
        private Set<RoleDTO> roles = new HashSet<>();
        private Set<UserOrganizationDTO> organizations = new HashSet<>();

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder status(UserStatus status) {
            this.status = status;
            return this;
        }

        public Builder profilePictureUrl(String profilePictureUrl) {
            this.profilePictureUrl = profilePictureUrl;
            return this;
        }

        public Builder lastLoginAt(LocalDateTime lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder locked(boolean locked) {
            this.locked = locked;
            return this;
        }

        public Builder roles(Set<RoleDTO> roles) {
            this.roles = roles;
            return this;
        }

        public Builder organizations(Set<UserOrganizationDTO> organizations) {
            this.organizations = organizations;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(id, username, email, firstName, lastName, phone, status,
                    profilePictureUrl, lastLoginAt, createdAt, updatedAt, organizationId,
                    active, locked, roles, organizations);
        }
    }
}
