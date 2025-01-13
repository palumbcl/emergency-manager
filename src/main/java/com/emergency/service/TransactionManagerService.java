package com.emergency.service;

import com.emergency.events.FeuDetecteEvent;
import com.emergency.events.FlotteAttribueeEvent;
import com.emergency.model.Caserne;
import com.emergency.model.FeuDetecte;
import com.emergency.model.Flotte;
import com.emergency.model.enums.StatutFeu;
import com.emergency.model.enums.StatutFlotte;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionManagerService {

    private final FlotteService flotteService;
    private final FeuDetecteService feuDetecteService;
    private final CaserneService caserneService;

    @Transactional
    public void handleFeuDetecte(FeuDetecteEvent event, ApplicationEventPublisher eventPublisher) {
        FeuDetecte feu = feuDetecteService.getFeuById(event.getFeuId());
        System.out.println("Attribution de ressources pour le feu détecté : " + feu);

        // Trouver la caserne la plus proche
        Caserne caserne = caserneService.getCaserneLaPlusProche(feu.getCoordonneeX(), feu.getCoordonneeY());

        // Calculer le nombre de pompiers nécessaires
        int nombrePompiersNecessaires = Math.max(3, (int) Math.ceil(feu.getIntensite() / 10.0));

        // Créer une flotte pour le feu
        Flotte flotte = flotteService.creerFlotte(caserne, nombrePompiersNecessaires, feu);

        if (flotte != null) {
            flotte.setStatutFlotte(StatutFlotte.EN_DEPLACEMENT);
            feu.setStatutFeu(StatutFeu.TRAITEMENT_EN_COURS);
            flotteService.updateFlotte(flotte);
            feuDetecteService.updateFeuDetecte(feu);

            System.out.println("Flotte attribuée : " + flotte);
            // Publier un événement pour notifier la création de la flotte
            eventPublisher.publishEvent(new FlotteAttribueeEvent(this, flotte.getId()));
        } else {
            System.out.println("Aucune ressource disponible pour ce feu.");
        }
    }
}
