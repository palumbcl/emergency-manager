package com.emergency.mapper;

import com.emergency.dto.FeuDetecteDTO;
import com.emergency.model.FeuDetecte;
import com.emergency.model.enums.StatutFeu;
import org.springframework.stereotype.Component;

@Component
public class FeuDetecteMapper {
    public FeuDetecteDTO toDTO(FeuDetecte feuDetecte) {
        FeuDetecteDTO feuDetecteDTO = new FeuDetecteDTO();
        feuDetecteDTO.setId(feuDetecte.getId());
        feuDetecteDTO.setCoordonneeX(feuDetecte.getCoordonneeX());
        feuDetecteDTO.setCoordonneeY(feuDetecte.getCoordonneeY());
        feuDetecteDTO.setIntensite(feuDetecte.getIntensite());
        feuDetecteDTO.setStatutFeu(feuDetecte.getStatutFeu().toString());
        feuDetecteDTO.setDateApparition(feuDetecte.getDateApparition());
        feuDetecteDTO.setDateExtinction(feuDetecte.getDateExtinction());
        return feuDetecteDTO;
    }

    public FeuDetecte toEntity(FeuDetecteDTO feuDetecteDTO){
        FeuDetecte feuDetecte = new FeuDetecte();
        // Ne pas assigner l'ID s'il est null
        if (feuDetecteDTO.getId() != null) {
            feuDetecte.setId(feuDetecteDTO.getId());
        }
        feuDetecte.setCoordonneeX(feuDetecteDTO.getCoordonneeX());
        feuDetecte.setCoordonneeY(feuDetecteDTO.getCoordonneeY());
        feuDetecte.setIntensite(feuDetecteDTO.getIntensite());
        feuDetecte.setStatutFeu(StatutFeu.valueOf(feuDetecteDTO.getStatutFeu()));
        feuDetecte.setDateApparition(feuDetecteDTO.getDateApparition());
        feuDetecte.setDateExtinction(feuDetecteDTO.getDateExtinction());
        return feuDetecte;
    }
}
