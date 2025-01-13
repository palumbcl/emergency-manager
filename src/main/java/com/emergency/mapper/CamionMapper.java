package com.emergency.mapper;

import com.emergency.dto.CamionDTO;
import com.emergency.model.Camion;
import com.emergency.model.Caserne;
import com.emergency.model.Pompier;
import com.emergency.model.enums.StatutRessource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CamionMapper {

    public CamionDTO toDTO(Camion camion) {
        CamionDTO camionDTO = new CamionDTO();
        camionDTO.setId(camion.getId());
        camionDTO.setCapacite(camion.getCapacite());
        camionDTO.setVitesse(camion.getVitesse());
        camionDTO.setCoordonneeX(camion.getCoordonneeX());
        camionDTO.setCoordonneeY(camion.getCoordonneeY());
        camionDTO.setStatut(camion.getStatut().toString());
        camionDTO.setCaserneId(camion.getCaserne().getId());
        camionDTO.setPompiersTransportes(camion.getPompiersTransportes().stream().map(Pompier::getId).collect(Collectors.toList()));
        return camionDTO;
    }

    public Camion toEntity(CamionDTO camionDTO, Caserne caserne, List<Pompier> pompiersTransportes){
        Camion camion = new Camion();
        camion.setId(camionDTO.getId());
        camion.setCapacite(camionDTO.getCapacite());
        camion.setVitesse(camionDTO.getVitesse());
        camion.setCoordonneeX(camionDTO.getCoordonneeX());
        camion.setCoordonneeY(camionDTO.getCoordonneeY());
        camion.setStatut(StatutRessource.valueOf(camionDTO.getStatut()));
        camion.setCaserne(caserne);
        camion.setPompiersTransportes(pompiersTransportes);
        return camion;
    }
}
