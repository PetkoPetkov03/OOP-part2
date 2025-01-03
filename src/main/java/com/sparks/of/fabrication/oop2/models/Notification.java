package com.sparks.of.fabrication.oop2.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Date;

/**
 * Represents a notification entity.
 */
@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    /**
     * The unique identifier for the notification.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification", nullable = false)
    private Long idNotification;

    /**
     * The employee that is using the program.
     */
    @ManyToOne
    @JoinColumn(name = "id_employee", nullable = false)
    private Employee employee;

    /**
     * The content of the notification message.
     */
    @Column(name = "message", length = 255)
    private String message;

    /**
     * The status of the notification.
     */
    @Column(name = "status", length = 20)
    private String status;

    /**
     * The date when the notification was created.
     */
    @Column(name = "date_sent")
    private Date dateSent;
}

