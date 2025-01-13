package com.emergency.service;

import com.emergency.model.Camion;
import com.emergency.model.Caserne;
import com.emergency.model.FeuDetecte;
import com.emergency.model.Flotte;
import com.emergency.model.Pompier;
import com.emergency.model.enums.StatutFlotte;
import com.emergency.model.enums.StatutRessource;
import com.emergency.repository.FlotteRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlotteService {

    private final FlotteRepository flotteRepository;
    private final CaserneService caserneService;
    private final FeuDetecteService feuDetecteService;
    private final ApplicationEventPublisher eventPublisher;
    private final CamionService camionService;
    private final PompierService pompierService;

    private final EntityManager entityManager;

    @Transactional
    public Flotte creerFlotte(Caserne caserne, int nombrePompiersNecessaires, FeuDetecte feu) {
        List<Pompier> pompiersDisponibles = caserne.getPompiers().stream()
                .filter(pompier -> pompier.getStatut() == StatutRessource.DISPONIBLE)
                .limit(nombrePompiersNecessaires)
                .collect(Collectors.toList());

        List<Camion> camionsDisponibles = caserne.getCamions().stream()
                .filter(camion -> camion.getStatut() == StatutRessource.DISPONIBLE)
                .collect(Collectors.toList());

        if(pompiersDisponibles.isEmpty() || camionsDisponibles.isEmpty()) {
            throw new RuntimeException("Pas de ressources disponibles pour créer la flotte !.");
        }

        List<Camion> camionsUtilises = assignerPompiersAuxCamions(camionsDisponibles, pompiersDisponibles);

        camionsUtilises = new ArrayList<>(camionsUtilises); // S'assurer que la liste est mutable
        camionsDisponibles = new ArrayList<>(camionsDisponibles); // S'assurer que la liste est mutable
        pompiersDisponibles = new ArrayList<>(pompiersDisponibles); // S'assurer que la liste est mutable

        camionsUtilises.forEach(camion -> {
            camion.setStatut(StatutRessource.EN_INTERVENTION);
            camion.getPompiersTransportes().forEach(pompier -> pompier.setStatut(StatutRessource.EN_INTERVENTION));
        });

        Flotte flotte = Flotte.builder()
                .caserne(caserne)
                .pompiers(pompiersDisponibles)
                .camions(camionsUtilises)
                .feu(feu)
                .statutFlotte(StatutFlotte.EN_DEPLACEMENT)
                .dateDepart(LocalDateTime.now())
                .dateArrivee(null)
                .build();

        Flotte finalFlotte = flotte;
        camionsDisponibles.forEach(camion -> {
            camion.getFlottes().add(finalFlotte);
            camionService.updateCamion(camion);
        });
        Flotte flotteAdded = flotteRepository.save(flotte);
        entityManager.flush(); // Force la synchronisation avec la base
        return flotteAdded;
    }

    @Transactional
    public List<Camion> assignerPompiersAuxCamions(List<Camion> camions, List<Pompier> pompiers) {
        List<Camion> camionsUtilises = new ArrayList<>();
        Iterator<Pompier> pompierIterator = pompiers.iterator();

        for (Camion camion : camions) {
            List<Pompier> pompiersPourCamion = new ArrayList<>();
            for (int i = 0; i < camion.getCapacite() && pompierIterator.hasNext(); i++) {
                pompiersPourCamion.add(pompierIterator.next());
            }
            camion.setPompiersTransportes(pompiersPourCamion);
            camionsUtilises.add(camion);

            if (!pompierIterator.hasNext()) break;
        }

        return camionsUtilises;
    }

    public Flotte getFlotteById(Long flotteId) {
        return flotteRepository.findById(flotteId)
                .orElseThrow(() -> new RuntimeException("Flotte non trouvée"));
    }

    @Transactional
    public void teleporterFlotteSurFeu(Flotte flotte) throws InterruptedException {
        flotte.setStatutFlotte(StatutFlotte.EN_DEPLACEMENT);
        System.out.println("Déplacement de la flotte sur le feu : " + flotte);
        Thread.sleep(10000); // Simuler le déplacement
        flotte.setCamions(flotte.getCamions().stream()
                .peek(camion -> {
                    camion.setCoordonneeX(flotte.getFeu().getCoordonneeX());
                    camion.setCoordonneeY(flotte.getFeu().getCoordonneeY());
                })
                .collect(Collectors.toList()));
        flotte.setDateArrivee(LocalDateTime.now());
        flotte.setStatutFlotte(StatutFlotte.EN_INTERVENTION);
    }

    @Transactional
    public Flotte addFlotte(Flotte flotte) {
        Flotte flotteAdded = flotteRepository.save(flotte);
        entityManager.flush(); // Force la synchronisation avec la base
        return flotteAdded;
    }

    @Transactional
    public void updateFlotte(Flotte flotte) {
        try {
            System.out.println("Mise à jour de la flotte : " + flotte);
            entityManager.flush(); // Force la synchronisation avec la base
            flotteRepository.save(flotte);
            System.out.println("Flotte mise à jour avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour de la flotte : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Flotte getFlotteByFeu(FeuDetecte feu) {
        return flotteRepository.findByFeu(feu);
    }

    @Transactional
    public void teleporterFlotteSurCaserne(Flotte flotte) throws InterruptedException {
        flotte.setStatutFlotte(StatutFlotte.RETOUR_CASERNE);
        System.out.println("Déplacement de la flotte qui rentre à  la caserne : " + flotte);
        Thread.sleep(10000); // Simuler le déplacement
        flotte.setCamions(flotte.getCamions().stream()
                .peek(camion -> {
                    camion.setCoordonneeX(flotte.getCaserne().getCoordonneeX());
                    camion.setCoordonneeY(flotte.getCaserne().getCoordonneeY());
                    camion.setStatut(StatutRessource.DISPONIBLE);
                })
                .collect(Collectors.toList()));
        flotte.setPompiers(flotte.getPompiers().stream()
                .peek(pompier -> {
                    pompier.setStatut(StatutRessource.DISPONIBLE);
                })
                .collect(Collectors.toList()));
        flotteRepository.delete(flotte);
    }
}


