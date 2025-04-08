package com.devolution.saas.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Utilitaire pour la manipulation des dates.
 */
public final class DateUtils {

    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    private static final String DEFAULT_DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    private DateUtils() {
        // Constructeur privé pour empêcher l'instanciation
    }

    /**
     * Convertit une date java.util.Date en LocalDate.
     *
     * @param date Date à convertir
     * @return LocalDate correspondant
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(DEFAULT_ZONE_ID).toLocalDate();
    }

    /**
     * Convertit une date java.util.Date en LocalDateTime.
     *
     * @param date Date à convertir
     * @return LocalDateTime correspondant
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(DEFAULT_ZONE_ID).toLocalDateTime();
    }

    /**
     * Convertit un LocalDate en java.util.Date.
     *
     * @param localDate LocalDate à convertir
     * @return Date correspondante
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(DEFAULT_ZONE_ID).toInstant());
    }

    /**
     * Convertit un LocalDateTime en java.util.Date.
     *
     * @param localDateTime LocalDateTime à convertir
     * @return Date correspondante
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(DEFAULT_ZONE_ID).toInstant());
    }

    /**
     * Formate une date LocalDate en chaîne de caractères.
     *
     * @param date Date à formater
     * @return Chaîne formatée
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }

    /**
     * Formate une date LocalDateTime en chaîne de caractères.
     *
     * @param dateTime Date à formater
     * @return Chaîne formatée
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT));
    }

    /**
     * Calcule la différence en jours entre deux dates.
     *
     * @param startDate Date de début
     * @param endDate   Date de fin
     * @return Nombre de jours entre les deux dates
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Les dates ne peuvent pas être nulles");
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * Vérifie si une date est dans le futur.
     *
     * @param date Date à vérifier
     * @return true si la date est dans le futur, false sinon
     */
    public static boolean isFutureDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(LocalDate.now());
    }

    /**
     * Vérifie si une date est dans le passé.
     *
     * @param date Date à vérifier
     * @return true si la date est dans le passé, false sinon
     */
    public static boolean isPastDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isBefore(LocalDate.now());
    }
}
