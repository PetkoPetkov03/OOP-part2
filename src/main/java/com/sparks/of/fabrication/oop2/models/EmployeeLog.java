package com.sparks.of.fabrication.oop2.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Date;

/**
 * Represents Log entity for the employee .
 */
@Entity
@Table(name = "employee_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLog {

    /**
     * The unique identifier for the log entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    /**
     * The employee associated with this log entry.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /**
     * The timestamp when the log entry was created.
     */
    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    /**
     * The main message of the log entry.
     */
    @Column(name = "message", nullable = false)
    private String message;

    /**
     * Additional details related to the log entry.
     */
    @Column(name = "details")
    private String details;
}
