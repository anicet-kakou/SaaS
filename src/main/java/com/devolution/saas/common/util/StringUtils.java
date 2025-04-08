package com.devolution.saas.common.util;

import java.text.Normalizer;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Utilitaire pour la manipulation des chaînes de caractères.
 */
public final class StringUtils {

    private static final Pattern SPECIAL_CHARS = Pattern.compile("[^a-zA-Z0-9]");
    private static final Pattern DIACRITICAL_MARKS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    private StringUtils() {
        // Constructeur privé pour empêcher l'instanciation
    }

    /**
     * Vérifie si une chaîne est nulle ou vide.
     *
     * @param str Chaîne à vérifier
     * @return true si la chaîne est nulle ou vide, false sinon
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Vérifie si une chaîne n'est ni nulle ni vide.
     *
     * @param str Chaîne à vérifier
     * @return true si la chaîne n'est ni nulle ni vide, false sinon
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Génère un slug à partir d'une chaîne.
     * Un slug est une version de la chaîne sans accents, en minuscules,
     * avec les espaces remplacés par des tirets et sans caractères spéciaux.
     *
     * @param input Chaîne d'entrée
     * @return Slug généré
     */
    public static String slugify(String input) {
        if (isEmpty(input)) {
            return "";
        }

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String withoutDiacritics = DIACRITICAL_MARKS.matcher(normalized).replaceAll("");
        String lowercase = withoutDiacritics.toLowerCase();
        String withoutSpecialChars = SPECIAL_CHARS.matcher(lowercase).replaceAll("-");
        String withoutMultipleDashes = withoutSpecialChars.replaceAll("-{2,}", "-");
        return withoutMultipleDashes.replaceAll("^-|-$", "");
    }

    /**
     * Tronque une chaîne à la longueur spécifiée.
     *
     * @param str       Chaîne à tronquer
     * @param maxLength Longueur maximale
     * @return Chaîne tronquée
     */
    public static String truncate(String str, int maxLength) {
        if (isEmpty(str) || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }

    /**
     * Génère un code aléatoire de la longueur spécifiée.
     *
     * @param length Longueur du code
     * @return Code généré
     */
    public static String generateRandomCode(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("La longueur doit être positive");
        }

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.substring(0, Math.min(length, uuid.length()));
    }

    /**
     * Masque une partie d'une chaîne, utile pour les données sensibles.
     *
     * @param str      Chaîne à masquer
     * @param start    Index de début du masquage (inclus)
     * @param end      Index de fin du masquage (exclus)
     * @param maskChar Caractère de masquage
     * @return Chaîne masquée
     */
    public static String mask(String str, int start, int end, char maskChar) {
        if (isEmpty(str)) {
            return str;
        }

        if (start < 0) {
            start = 0;
        }

        if (end > str.length()) {
            end = str.length();
        }

        if (start >= end) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str);
        for (int i = start; i < end; i++) {
            sb.setCharAt(i, maskChar);
        }

        return sb.toString();
    }
}
