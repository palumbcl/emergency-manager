package com.emergency.dto;

import lombok.Data;
import java.util.List;

@Data
public class CaserneDTO {
    private Long id;
    private String nom;
    private double coordonneeX;
    private double coordonneeY;
    private List<String> pompiers; // Liste des noms des pompiers
    private List<Long> camions;    // Liste des IDs des camions
}
