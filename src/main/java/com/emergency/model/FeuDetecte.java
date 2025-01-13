package com.emergency.model;

import com.emergency.model.enums.StatutFeu;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class FeuDetecte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "coordonnee_x")
    private double coordonneeX;
    @Column(nullable = false, name = "coordonnee_y")
    private double coordonneeY;
    @Column(nullable = false)
    private int intensite;
    @Column(nullable = false, name = "statut_feu")
    @Enumerated(EnumType.STRING)
    private StatutFeu statutFeu;
    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, updatable = false, name = "date_apparition")
    private LocalDateTime dateApparition;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_extinction")
    private LocalDateTime dateExtinction;
}