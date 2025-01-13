package com.emergency.dto;

import lombok.Data;

@Data
public class PompierDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String statut; // Statut sous forme de String
    private Long caserneId; // ID de la caserne d'origine
}
