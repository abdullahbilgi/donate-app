package com.project.donate.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTime;

    private String status;

    private String function;

    private String service;

    @Column(columnDefinition = "TEXT")
    private String message;
}
