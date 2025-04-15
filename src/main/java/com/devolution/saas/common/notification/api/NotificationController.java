package com.devolution.saas.common.notification.api;

import com.devolution.saas.common.notification.NotificationService;
import com.devolution.saas.common.notification.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des notifications.
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    // Injecter ici les services nécessaires pour gérer les notifications
    // (ex: NotificationQueryService, etc.)

    /**
     * Récupère les notifications de l'utilisateur courant.
     *
     * @param pageable Pagination
     * @return Page de notifications
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<NotificationDTO>> getMyNotifications(Pageable pageable) {
        // TODO: Implémenter la récupération des notifications de l'utilisateur courant
        return ResponseEntity.ok(Page.empty());
    }

    /**
     * Récupère les notifications non lues de l'utilisateur courant.
     *
     * @param pageable Pagination
     * @return Page de notifications non lues
     */
    @GetMapping("/unread")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<NotificationDTO>> getMyUnreadNotifications(Pageable pageable) {
        // TODO: Implémenter la récupération des notifications non lues de l'utilisateur courant
        return ResponseEntity.ok(Page.empty());
    }

    /**
     * Récupère une notification par son ID.
     *
     * @param id ID de la notification
     * @return Notification
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationDTO> getNotification(@PathVariable String id) {
        // TODO: Implémenter la récupération d'une notification par son ID
        return ResponseEntity.notFound().build();
    }

    /**
     * Marque une notification comme lue.
     *
     * @param id ID de la notification
     * @return Notification mise à jour
     */
    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable String id) {
        // TODO: Implémenter le marquage d'une notification comme lue
        return ResponseEntity.notFound().build();
    }

    /**
     * Marque toutes les notifications de l'utilisateur courant comme lues.
     *
     * @return Nombre de notifications mises à jour
     */
    @PutMapping("/read-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Integer> markAllAsRead() {
        // TODO: Implémenter le marquage de toutes les notifications comme lues
        return ResponseEntity.ok(0);
    }

    /**
     * Supprime une notification.
     *
     * @param id ID de la notification
     * @return Réponse vide
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteNotification(@PathVariable String id) {
        // TODO: Implémenter la suppression d'une notification
        return ResponseEntity.noContent().build();
    }

    /**
     * Supprime toutes les notifications de l'utilisateur courant.
     *
     * @return Nombre de notifications supprimées
     */
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Integer> deleteAllNotifications() {
        // TODO: Implémenter la suppression de toutes les notifications
        return ResponseEntity.ok(0);
    }

    /**
     * Envoie une notification à un utilisateur (réservé aux administrateurs).
     *
     * @param userId           ID de l'utilisateur
     * @param notificationCode Code de la notification
     * @param params           Paramètres pour la notification
     * @param data             Données supplémentaires pour la notification
     * @return ID de la notification envoyée
     */
    @PostMapping("/send")
    @PreAuthorize("hasAuthority('NOTIFICATION_SEND')")
    public ResponseEntity<String> sendNotification(
            @RequestParam String userId,
            @RequestParam String notificationCode,
            @RequestParam(required = false) Object[] params,
            @RequestBody(required = false) Map<String, Object> data) {
        String notificationId = notificationService.sendNotification(userId, notificationCode, params, data);
        return ResponseEntity.ok(notificationId);
    }

    /**
     * Envoie une notification à plusieurs utilisateurs (réservé aux administrateurs).
     *
     * @param userIds          IDs des utilisateurs
     * @param notificationCode Code de la notification
     * @param params           Paramètres pour la notification
     * @param data             Données supplémentaires pour la notification
     * @return Nombre de notifications envoyées
     */
    @PostMapping("/send-to-users")
    @PreAuthorize("hasAuthority('NOTIFICATION_SEND')")
    public ResponseEntity<Integer> sendNotificationToUsers(
            @RequestParam String[] userIds,
            @RequestParam String notificationCode,
            @RequestParam(required = false) Object[] params,
            @RequestBody(required = false) Map<String, Object> data) {
        int count = notificationService.sendNotificationToUsers(userIds, notificationCode, params, data);
        return ResponseEntity.ok(count);
    }

    /**
     * Envoie une notification à tous les utilisateurs ayant un rôle spécifique (réservé aux administrateurs).
     *
     * @param role             Rôle des utilisateurs
     * @param notificationCode Code de la notification
     * @param params           Paramètres pour la notification
     * @param data             Données supplémentaires pour la notification
     * @return Nombre de notifications envoyées
     */
    @PostMapping("/send-to-role")
    @PreAuthorize("hasAuthority('NOTIFICATION_SEND')")
    public ResponseEntity<Integer> sendNotificationToRole(
            @RequestParam String role,
            @RequestParam String notificationCode,
            @RequestParam(required = false) Object[] params,
            @RequestBody(required = false) Map<String, Object> data) {
        int count = notificationService.sendNotificationToRole(role, notificationCode, params, data);
        return ResponseEntity.ok(count);
    }

    /**
     * Envoie une notification à tous les utilisateurs (réservé aux administrateurs).
     *
     * @param notificationCode Code de la notification
     * @param params           Paramètres pour la notification
     * @param data             Données supplémentaires pour la notification
     * @return Nombre de notifications envoyées
     */
    @PostMapping("/send-to-all")
    @PreAuthorize("hasAuthority('NOTIFICATION_SEND')")
    public ResponseEntity<Integer> sendNotificationToAll(
            @RequestParam String notificationCode,
            @RequestParam(required = false) Object[] params,
            @RequestBody(required = false) Map<String, Object> data) {
        int count = notificationService.sendNotificationToAll(notificationCode, params, data);
        return ResponseEntity.ok(count);
    }
}
