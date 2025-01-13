package com.emergency.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FlotteDTO {
    private Long id;
    private Long caserneId;
    private Long feuId;
    private List<Long> camions; // Liste des IDs des camions
    private List<Long> pompiers; // Liste des IDs des pompiers
    private LocalDateTime dateDepart;
    private LocalDateTime dateArrivee;
    private String statutFlotte; // Statut sous forme de String
}
