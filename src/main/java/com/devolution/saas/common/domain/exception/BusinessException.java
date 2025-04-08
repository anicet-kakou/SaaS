package com.devolution.saas.common.domain.exception;

import lombok.Getter;

/**
 * Exception de base pour les erreurs métier.
 * Utilisée pour signaler des erreurs liées aux règles métier.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final String code;
    private final String message;
    private final transient Object[] args;

    /**
     * Constructeur avec code d'erreur et message.
     *
     * @param code    Code d'erreur
     * @param message Message d'erreur
     */
    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.args = new Object[0];
    }

    /**
     * Constructeur avec code d'erreur, message et arguments pour la traduction.
     *
     * @param code    Code d'erreur
     * @param message Message d'erreur
     * @param args    Arguments pour la traduction du message
     */
    public BusinessException(String code, String message, Object... args) {
        super(message);
        this.code = code;
        this.message = message;
        this.args = args;
    }

    /**
     * Constructeur avec code d'erreur, message et cause.
     *
     * @param code    Code d'erreur
     * @param message Message d'erreur
     * @param cause   Cause de l'exception
     */
    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.args = new Object[0];
    }
}
