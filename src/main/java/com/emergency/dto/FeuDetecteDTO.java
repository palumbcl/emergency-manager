package com.emergency.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FeuDetecteDTO {
    private Long id;
    private double coordonneeX;
    private double coordonneeY;
    private int intensite;
    private String statutFeu; // Statut sous forme de String
    private LocalDateTime dateApparition;
    private LocalDateTime dateExtinction;
}
