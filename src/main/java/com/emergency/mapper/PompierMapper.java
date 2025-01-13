package com.emergency.mapper;

import com.emergency.dto.PompierDTO;
import com.emergency.model.Caserne;
import com.emergency.model.Pompier;
import com.emergency.model.enums.StatutRessource;
import org.springframework.stereotype.Component;

@Component
public class PompierMapper {
    public PompierDTO toDTO(Pompier pompier) {
        PompierDTO pompierDTO = new PompierDTO();
        pompierDTO.setId(pompier.getId());
        pompierDTO.setNom(pompier.getNom());
        pompierDTO.setPrenom(pompier.getPrenom());
        pompierDTO.setStatut(pompier.getStatut().toString());
        pompierDTO.setCaserneId(pompier.getCaserne().getId());
        return pompierDTO;
    }

    public Pompier toEntity(PompierDTO pompierDTO, Caserne caserne){
        Pompier pompier = new Pompier();
        pompier.setId(pompierDTO.getId());
        pompier.setNom(pompierDTO.getNom());
        pompier.setPrenom(pompierDTO.getPrenom());
        pompier.setStatut(StatutRessource.valueOf(pompierDTO.getStatut()));
        pompier.setCaserne(caserne);
        return pompier;
    }
}
