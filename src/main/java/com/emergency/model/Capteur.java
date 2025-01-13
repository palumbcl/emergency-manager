package com.emergency.model;

import com.emergency.model.enums.Etat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Capteur {
    @Id
    private Long id;
    @Column(nullable = false, name = "coordonnee_x")
    private double coordonneeX;
    @Column(nullable = false, name = "coordonnee_y")
    private double coordonneeY;
    @Column(nullable = false)
    private double temperature;
    @Column(nullable = false)
    private int portee = 500;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Etat etat;
}
