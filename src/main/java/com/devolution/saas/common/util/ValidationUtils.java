package com.devolution.saas.common.util;

import java.util.regex.Pattern;

/**
 * Utilitaire pour la validation des données.
 */
public final class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[0-9]{10,15}$");

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");

    private ValidationUtils() {
        // Constructeur privé pour empêcher l'instanciation
    }

    /**
     * Valide une adresse email.
     *
     * @param email Adresse email à valider
     * @return true si l'adresse email est valide, false sinon
     */
    public static boolean isValidEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Valide un numéro de téléphone.
     *
     * @param phone Numéro de téléphone à valider
     * @return true si le numéro de téléphone est valide, false sinon
     */
    public static boolean isValidPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Valide un mot de passe.
     * Le mot de passe doit contenir au moins 8 caractères, une lettre majuscule,
     * une lettre minuscule, un chiffre et un caractère spécial.
     *
     * @param password Mot de passe à valider
     * @return true si le mot de passe est valide, false sinon
     */
    public static boolean isValidPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Vérifie si une valeur est dans une plage.
     *
     * @param value Valeur à vérifier
     * @param min   Valeur minimale (incluse)
     * @param max   Valeur maximale (incluse)
     * @return true si la valeur est dans la plage, false sinon
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Vérifie si une valeur est dans une plage.
     *
     * @param value Valeur à vérifier
     * @param min   Valeur minimale (incluse)
     * @param max   Valeur maximale (incluse)
     * @return true si la valeur est dans la plage, false sinon
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    /**
     * Vérifie si une chaîne a une longueur dans une plage.
     *
     * @param str       Chaîne à vérifier
     * @param minLength Longueur minimale (incluse)
     * @param maxLength Longueur maximale (incluse)
     * @return true si la longueur de la chaîne est dans la plage, false sinon
     */
    public static boolean hasLengthInRange(String str, int minLength, int maxLength) {
        if (str == null) {
            return minLength == 0;
        }
        int length = str.length();
        return length >= minLength && length <= maxLength;
    }
}
