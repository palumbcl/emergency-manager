package com.emergency.model;

import com.emergency.model.enums.StatutFlotte;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Flotte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_caserne", nullable = false)
    private Caserne caserne;

    @ManyToOne
    @JoinColumn(name = "id_feu", nullable = false)
    private FeuDetecte feu;

    @ManyToMany
    @JoinTable(name = "FLOTTE_CAMION",
            joinColumns = @JoinColumn(name = "flotte_id"),
            inverseJoinColumns = @JoinColumn(name = "camion_id")
    )
    private List<Camion> camions;

    @ManyToMany
    @JoinTable(
            name = "FLOTTE_POMPIER",
            joinColumns = @JoinColumn(name = "flotte_id"),
            inverseJoinColumns = @JoinColumn(name = "pompier_id")
    )
    private List<Pompier> pompiers;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_depart")
    private LocalDateTime dateDepart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_arrivee")
    private LocalDateTime dateArrivee;
    @Column(name = "statut_flotte")
    private StatutFlotte statutFlotte;
}
