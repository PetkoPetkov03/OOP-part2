package com.sparks.of.fabrication.oop2.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Date;

@Entity
@Table(name = "employee_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "details")
    private String details;

}
