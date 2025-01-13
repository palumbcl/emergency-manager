package com.emergency.service;

import com.emergency.mapper.CaserneMapper;
import com.emergency.model.Camion;
import com.emergency.model.Caserne;
import com.emergency.model.Pompier;
import com.emergency.model.enums.StatutRessource;
import com.emergency.repository.CaserneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CaserneService {
    private final CaserneRepository caserneRepository;
    private final CaserneMapper caserneMapper;

    public List<Caserne> getAllCasernes() {
        return caserneRepository.findAll();
    }

    public List<Object> getRessourcesDisponibles(Long caserneId) {
        Caserne caserne = caserneRepository.findById(caserneId)
                .orElseThrow(() -> new RuntimeException("Caserne non trouvée"));

        List<Pompier> pompiersDisponibles = caserne.getPompiers().stream()
                .filter(pompier -> pompier.getStatut() == StatutRessource.DISPONIBLE)
                .toList();

        List<Camion> camionsDisponibles = caserne.getCamions().stream()
                .filter(camion -> camion.getStatut() == StatutRessource.DISPONIBLE)
                .toList();

        return List.of(pompiersDisponibles, camionsDisponibles);
    }

    public Caserne getCaserneLaPlusProche(double feuX, double feuY) {
        return caserneRepository.findAll().stream()
                .min(Comparator.comparingDouble(
                        caserne -> calculerDistance(caserne.getCoordonneeX(), caserne.getCoordonneeY(), feuX, feuY)))
                .orElseThrow(() -> new RuntimeException("Aucune caserne disponible"));
    }

    public boolean hasRessourcesNecessaires(Caserne caserne, int nombrePompiersNecessaires, int nombreCamionsNecessaires) {
        long pompiersDisponibles = caserne.getPompiers().stream()
                .filter(pompier -> pompier.getStatut() == StatutRessource.DISPONIBLE)
                .count();

        long camionsDisponibles = caserne.getCamions().stream()
                .filter(camion -> camion.getStatut() == StatutRessource.DISPONIBLE)
                .count();

        return pompiersDisponibles >= nombrePompiersNecessaires && camionsDisponibles >= nombreCamionsNecessaires;
    }


    private double calculerDistance(double x1, double y1, double x2, double y2) {
        double dx = (x2 - x1) * 111_000; // Conversion degrés -> mètres
        double dy = (y2 - y1) * 111_000;
        return Math.sqrt(dx * dx + dy * dy);
    }

}
