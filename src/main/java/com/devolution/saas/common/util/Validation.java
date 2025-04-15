package com.devolution.saas.common.util;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Utilitaire pour la validation des données.
 */
public final class Validation {

    // Pattern pour valider les types de couverture
    public static final Pattern COVERAGE_TYPE_PATTERN = Pattern.compile("^(THIRD_PARTY|COMPREHENSIVE)$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[0-9]{10,15}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");

    private Validation() {
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

    /**
     * Valide qu'un UUID n'est pas null.
     *
     * @param id        L'UUID à valider
     * @param paramName Le nom du paramètre pour le message d'erreur
     * @throws IllegalArgumentException si l'UUID est null
     */
    public static void validateNotNull(UUID id, String paramName) {
        if (id == null) {
            throw new IllegalArgumentException("L'" + paramName + " ne peut pas être null");
        }
    }

    /**
     * Valide qu'une chaîne n'est pas null ou vide.
     *
     * @param value     La chaîne à valider
     * @param paramName Le nom du paramètre pour le message d'erreur
     * @throws IllegalArgumentException si la chaîne est null ou vide
     */
    public static void validateNotEmpty(String value, String paramName) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Le " + paramName + " ne peut pas être null ou vide");
        }
    }

    /**
     * Valide qu'un nombre est positif ou zéro.
     *
     * @param value     Le nombre à valider
     * @param paramName Le nom du paramètre pour le message d'erreur
     * @throws IllegalArgumentException si le nombre est négatif
     */
    public static void validateNotNegative(int value, String paramName) {
        if (value < 0) {
            throw new IllegalArgumentException("Le " + paramName + " ne peut pas être négatif");
        }
    }

    /**
     * Valide qu'un BigDecimal n'est pas null.
     *
     * @param value     Le BigDecimal à valider
     * @param paramName Le nom du paramètre pour le message d'erreur
     * @throws IllegalArgumentException si le BigDecimal est null
     */
    public static void validateNotNull(BigDecimal value, String paramName) {
        if (value == null) {
            throw new IllegalArgumentException("Le " + paramName + " ne peut pas être null");
        }
    }

    /**
     * Valide qu'un BigDecimal est dans une plage donnée.
     *
     * @param value     Le BigDecimal à valider
     * @param min       La valeur minimale (inclusive)
     * @param max       La valeur maximale (inclusive)
     * @param paramName Le nom du paramètre pour le message d'erreur
     * @throws IllegalArgumentException si le BigDecimal est hors de la plage
     */
    public static void validateRange(BigDecimal value, BigDecimal min, BigDecimal max, String paramName) {
        validateNotNull(value, paramName);
        if (value.compareTo(min) < 0 || value.compareTo(max) > 0) {
            throw new IllegalArgumentException("Le " + paramName + " doit être compris entre " + min + " et " + max);
        }
    }

    /**
     * Valide qu'un BigDecimal est positif ou zéro.
     *
     * @param value     Le BigDecimal à valider
     * @param paramName Le nom du paramètre pour le message d'erreur
     * @throws IllegalArgumentException si le BigDecimal est négatif
     */
    public static void validateNotNegative(BigDecimal value, String paramName) {
        validateNotNull(value, paramName);
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le " + paramName + " ne peut pas être négatif");
        }
    }

    /**
     * Valide qu'une chaîne correspond à un pattern donné.
     *
     * @param value       La chaîne à valider
     * @param pattern     Le pattern à utiliser
     * @param paramName   Le nom du paramètre pour le message d'erreur
     * @param validValues Description des valeurs valides pour le message d'erreur
     * @throws IllegalArgumentException si la chaîne ne correspond pas au pattern
     */
    public static void validatePattern(String value, Pattern pattern, String paramName, String validValues) {
        validateNotEmpty(value, paramName);
        if (!pattern.matcher(value).matches()) {
            throw new IllegalArgumentException(paramName + " invalide: " + value + ". " + validValues);
        }
    }
}
