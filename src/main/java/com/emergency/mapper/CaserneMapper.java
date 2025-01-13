package com.emergency.mapper;

import com.emergency.dto.CaserneDTO;
import com.emergency.model.Caserne;
import com.emergency.model.Camion;
import com.emergency.model.Pompier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CaserneMapper {

    public CaserneDTO toDTO(Caserne caserne) {
        CaserneDTO caserneDTO = new CaserneDTO();
        caserneDTO.setId(caserne.getId());
        caserneDTO.setNom(caserne.getNom());
        caserneDTO.setCoordonneeX(caserne.getCoordonneeX());
        caserneDTO.setCoordonneeY(caserne.getCoordonneeY());
        caserneDTO.setPompiers(caserne.getPompiers().stream()
                .map(pompier -> pompier.getNom() + " " + pompier.getPrenom())
                .collect(Collectors.toList()));
        caserneDTO.setCamions(caserne.getCamions().stream()
                .map(Camion::getId)
                .collect(Collectors.toList()));
        return caserneDTO;
    }

    public Caserne toEntity(CaserneDTO caserneDTO, List<Pompier> pompiers, List<Camion> camions){
        Caserne caserne = new Caserne();
        caserne.setId(caserneDTO.getId());
        caserne.setNom(caserneDTO.getNom());
        caserne.setCoordonneeX(caserneDTO.getCoordonneeX());
        caserne.setCoordonneeY(caserneDTO.getCoordonneeY());
        caserne.setPompiers(pompiers);
        caserne.setCamions(camions);
        return caserne;
    }
}
