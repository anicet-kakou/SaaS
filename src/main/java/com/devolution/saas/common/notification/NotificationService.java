package com.devolution.saas.common.notification;

import java.util.Map;

/**
 * Interface pour le service de notification.
 * Permet d'envoyer des notifications aux utilisateurs.
 */
public interface NotificationService {

    /**
     * Envoie une notification à un utilisateur.
     *
     * @param userId           ID de l'utilisateur
     * @param notificationCode Code de la notification
     * @return ID de la notification envoyée
     */
    String sendNotification(String userId, String notificationCode);

    /**
     * Envoie une notification à un utilisateur avec des paramètres.
     *
     * @param userId           ID de l'utilisateur
     * @param notificationCode Code de la notification
     * @param params           Paramètres pour la notification
     * @return ID de la notification envoyée
     */
    String sendNotification(String userId, String notificationCode, Object[] params);

    /**
     * Envoie une notification à un utilisateur avec des paramètres et des données supplémentaires.
     *
     * @param userId           ID de l'utilisateur
     * @param notificationCode Code de la notification
     * @param params           Paramètres pour la notification
     * @param data             Données supplémentaires pour la notification
     * @return ID de la notification envoyée
     */
    String sendNotification(String userId, String notificationCode, Object[] params, Map<String, Object> data);

    /**
     * Envoie une notification à plusieurs utilisateurs.
     *
     * @param userIds          IDs des utilisateurs
     * @param notificationCode Code de la notification
     * @return Nombre de notifications envoyées
     */
    int sendNotificationToUsers(String[] userIds, String notificationCode);

    /**
     * Envoie une notification à plusieurs utilisateurs avec des paramètres.
     *
     * @param userIds          IDs des utilisateurs
     * @param notificationCode Code de la notification
     * @param params           Paramètres pour la notification
     * @return Nombre de notifications envoyées
     */
    int sendNotificationToUsers(String[] userIds, String notificationCode, Object[] params);

    /**
     * Envoie une notification à plusieurs utilisateurs avec des paramètres et des données supplémentaires.
     *
     * @param userIds          IDs des utilisateurs
     * @param notificationCode Code de la notification
     * @param params           Paramètres pour la notification
     * @param data             Données supplémentaires pour la notification
     * @return Nombre de notifications envoyées
     */
    int sendNotificationToUsers(String[] userIds, String notificationCode, Object[] params, Map<String, Object> data);

    /**
     * Envoie une notification à tous les utilisateurs ayant un rôle spécifique.
     *
     * @param role             Rôle des utilisateurs
     * @param notificationCode Code de la notification
     * @return Nombre de notifications envoyées
     */
    int sendNotificationToRole(String role, String notificationCode);

    /**
     * Envoie une notification à tous les utilisateurs ayant un rôle spécifique avec des paramètres.
     *
     * @param role             Rôle des utilisateurs
     * @param notificationCode Code de la notification
     * @param params           Paramètres pour la notification
     * @return Nombre de notifications envoyées
     */
    int sendNotificationToRole(String role, String notificationCode, Object[] params);

    /**
     * Envoie une notification à tous les utilisateurs ayant un rôle spécifique avec des paramètres et des données supplémentaires.
     *
     * @param role             Rôle des utilisateurs
     * @param notificationCode Code de la notification
     * @param params           Paramètres pour la notification
     * @param data             Données supplémentaires pour la notification
     * @return Nombre de notifications envoyées
     */
    int sendNotificationToRole(String role, String notificationCode, Object[] params, Map<String, Object> data);

    /**
     * Envoie une notification à tous les utilisateurs.
     *
     * @param notificationCode Code de la notification
     * @return Nombre de notifications envoyées
     */
    int sendNotificationToAll(String notificationCode);

    /**
     * Envoie une notification à tous les utilisateurs avec des paramètres.
     *
     * @param notificationCode Code de la notification
     * @param params           Paramètres pour la notification
     * @return Nombre de notifications envoyées
     */
    int sendNotificationToAll(String notificationCode, Object[] params);

    /**
     * Envoie une notification à tous les utilisateurs avec des paramètres et des données supplémentaires.
     *
     * @param notificationCode Code de la notification
     * @param params           Paramètres pour la notification
     * @param data             Données supplémentaires pour la notification
     * @return Nombre de notifications envoyées
     */
    int sendNotificationToAll(String notificationCode, Object[] params, Map<String, Object> data);
}
