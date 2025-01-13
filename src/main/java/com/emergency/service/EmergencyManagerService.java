package com.emergency.service;

import com.emergency.events.CapteursUpdatedEvent;
import com.emergency.events.FeuDetecteEvent;
import com.emergency.events.FlotteAttribueeEvent;
import com.emergency.model.Capteur;
import com.emergency.model.FeuDetecte;
import com.emergency.model.Flotte;
import com.emergency.model.enums.StatutFeu;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmergencyManagerService {

    private final FlotteService flotteService;
    private final FeuDetecteService feuDetecteService;
    private final CaserneService caserneService;
    private final TransactionManagerService transactionManagerService;

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CapteurService capteurService;

    @Value("${API_URL_IOT}")
    private String urlIot;

    @Value("${API_URL_SIMULATION}")
    private String urlSimulation;

    @Scheduled(fixedRate = 3000)
    public void getAllCapteursFromAPI() {
        System.out.println("Récupération des capteurs depuis la chaîne IoT...");

        try {
            Capteur[] capteursArray = restTemplate.getForObject(urlIot, Capteur[].class);

            if (capteursArray != null) {
                List<Capteur> capteurs = Arrays.asList(capteursArray);

                capteurs.forEach(capteur -> {
                    try {
                        // Validation des coordonnées
                        if (capteur.getCoordonneeX() == 0 || capteur.getCoordonneeY() == 0) {
                            System.err.println("Coordonnées invalides pour le capteur : " + capteur);
                            return;
                        }
                        capteurService.saveOrUpdateCapteur(capteur);
                    } catch (Exception e) {
                        System.err.println("Erreur lors du traitement du capteur : " + capteur + " - " + e.getMessage());
                    }
                });

                System.out.println("Capteurs récupérés et sauvegardés avec succès.");
                eventPublisher.publishEvent(new CapteursUpdatedEvent(this, capteurs));
            } else {
                System.out.println("Aucun capteur reçu depuis l'API.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des capteurs : " + e.getMessage());
        }
    }


    @EventListener
    public void onCapteursUpdated(CapteursUpdatedEvent event) {
        System.out.println("Détection des feux...");
        event.getCapteurs().stream()
                .filter(capteur -> capteur.getTemperature() >= 100)
                .forEach(capteur -> {
                    List<Capteur> capteursVoisins = capteurService.getCapteursVoisins(capteur, 500);
                    double feuDetecteCoordonneeX = capteurService.calculerCoordonneeMoyenneX(capteur, capteursVoisins);
                    double feuDetecteCoordonneeY = capteurService.calculerCoordonneeMoyenneY(capteur, capteursVoisins);
                    FeuDetecte feu = FeuDetecte.builder()
                            .coordonneeX(feuDetecteCoordonneeX)
                            .coordonneeY(feuDetecteCoordonneeY)
                            .intensite((int) Math.round(capteur.getTemperature() / 10))
                            .statutFeu(StatutFeu.DETECTE)
                            .build();

                    var feuExistant = feuDetecteService.findFeuDetecteByCoordonneeXAndCoordonneeY(feu.getCoordonneeX(), feu.getCoordonneeY());
                    if (feuExistant.isEmpty()) {
                        feuDetecteService.addFeuDetecte(feu);
                        System.out.println("Feu détecté : " + feu);
                        eventPublisher.publishEvent(new FeuDetecteEvent(this, feu.getId()));
                    }
                });
    }

    @EventListener
    public void onFeuDetecte(FeuDetecteEvent event) {
        try {
            System.out.println("Gestion du feu détecté avec délégation transactionnelle.");
            transactionManagerService.handleFeuDetecte(event, eventPublisher);
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement du feu détecté : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @EventListener
    public void onFlotteAttribuee(FlotteAttribueeEvent event) throws InterruptedException {
        Flotte flotte = flotteService.getFlotteById(event.getFlotteId());
        FeuDetecte feu = feuDetecteService.getFeuById(flotte.getFeu().getId());

        flotteService.teleporterFlotteSurFeu(flotte);
        System.out.println("Ressources déplacées vers le feu : " + feu);
        // Éteindre le feu
        feuDetecteService.eteindreFeu(feu);
        System.out.println("Feu éteint : " + feu);
        // Ramener les ressources à la caserne
        flotteService.teleporterFlotteSurCaserne(flotte);
        //Appel API pour éteindre le feu dans la simulation
        // Envoyer la requête POST
        try {
            restTemplate.postForObject(urlSimulation, feu.getId(), Void.class);
            System.out.println("Requête API envoyée avec succès.");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de la requête API : " + e.getMessage());
        }
    }

}
