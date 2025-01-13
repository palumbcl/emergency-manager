package com.emergency.dto;

import lombok.Data;
import java.util.List;

@Data
public class CamionDTO {
    private Long id;
    private int capacite;
    private int vitesse;
    private double coordonneeX;
    private double coordonneeY;
    private String statut; // Statut sous forme de String
    private Long caserneId;
    private List<Long> pompiersTransportes; // Liste des IDs des pompiers transportés
}
