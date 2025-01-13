package com.emergency.model;

import com.emergency.model.enums.StatutRessource;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Camion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private int capacite;
    @Column(nullable = false)
    private int vitesse;
    @Column(nullable = false, name = "coordonnee_x")
    private double coordonneeX;
    @Column(nullable = false, name = "coordonnee_y")
    private double coordonneeY;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutRessource statut;
    @ManyToOne
    @JoinColumn(name = "caserne_id")
    private Caserne caserne;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "camion_pompier",
            joinColumns = @JoinColumn(name = "camion_id"),
            inverseJoinColumns = @JoinColumn(name = "pompier_id")
    )
    private List<Pompier> pompiersTransportes;
    @ManyToMany(mappedBy = "camions", fetch = FetchType.EAGER)
    private List<Flotte> flottes;
}
