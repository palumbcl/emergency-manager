package com.emergency.mapper;

import com.emergency.dto.FlotteDTO;
import com.emergency.model.Caserne;
import com.emergency.model.FeuDetecte;
import com.emergency.model.Flotte;
import com.emergency.model.Camion;
import com.emergency.model.Pompier;
import com.emergency.model.enums.StatutFlotte;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class FlotteMapper {
    public FlotteDTO toDTO(Flotte flotte) {
        FlotteDTO flotteDTO = new FlotteDTO();
        flotteDTO.setId(flotte.getId());
        flotteDTO.setCaserneId(flotte.getCaserne().getId());
        flotteDTO.setFeuId(flotte.getFeu().getId());
        flotteDTO.setCamions(flotte.getCamions().stream().map(Camion::getId).collect(Collectors.toList()));
        flotteDTO.setPompiers(flotte.getPompiers().stream().map(Pompier::getId).collect(Collectors.toList()));
        flotteDTO.setDateDepart(flotte.getDateDepart());
        flotteDTO.setDateArrivee(flotte.getDateArrivee());
        flotteDTO.setStatutFlotte(flotte.getStatutFlotte().toString());
        return flotteDTO;
    }

    public Flotte toEntity(FlotteDTO flotteDTO, Caserne caserne, FeuDetecte feu, List<Camion> camions, List<Pompier> pompiers){
        Flotte flotte = new Flotte();
        flotte.setId(flotteDTO.getId());
        flotte.setCaserne(caserne);
        flotte.setFeu(feu);
        flotte.setCamions(camions);
        flotte.setPompiers(pompiers);
        flotte.setDateDepart(flotteDTO.getDateDepart());
        flotte.setDateArrivee(flotteDTO.getDateArrivee());
        flotte.setStatutFlotte(StatutFlotte.valueOf(flotteDTO.getStatutFlotte()));
        return flotte;
    }
}
