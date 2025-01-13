package com.emergency;

import com.emergency.events.CapteursUpdatedEvent;
import com.emergency.events.FeuDetecteEvent;
import com.emergency.events.FlotteAttribueeEvent;
import com.emergency.model.Camion;
import com.emergency.model.Capteur;
import com.emergency.model.Caserne;
import com.emergency.model.FeuDetecte;
import com.emergency.model.Flotte;
import com.emergency.model.Pompier;
import com.emergency.model.enums.Etat;
import com.emergency.model.enums.StatutFeu;
import com.emergency.model.enums.StatutFlotte;
import com.emergency.model.enums.StatutRessource;
import com.emergency.service.CamionService;
import com.emergency.service.CapteurService;
import com.emergency.service.CaserneService;
import com.emergency.service.EmergencyManagerService;
import com.emergency.service.FeuDetecteService;
import com.emergency.service.FlotteService;
import com.emergency.service.PompierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmergencyIntegrationTest {

    @Mock
    private CapteurService capteurService;

    @Mock
    private CaserneService caserneService;

    @Mock
    private FeuDetecteService feuDetecteService;

    @Mock
    private FlotteService flotteService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private PompierService pompierService;

    @Mock
    private CamionService camionService;

    @InjectMocks
    private EmergencyManagerService emergencyManagerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldDetectFireAndPublishEvent() throws InterruptedException {
        // GIVEN
        List<Capteur> capteurs = List.of(
                new Capteur(1L, 4.81, 45.73, 120, 100, Etat.FONCTIONNEL),
                new Capteur(2L, 4.83, 45.77, 110, 100, Etat.FONCTIONNEL)
        );
        Caserne caserne = Caserne.builder()
                .id(1L)
                .nom("Caserne 1")
                .coordonneeX(4.82)
                .coordonneeY(45.74)
                .build();
        FeuDetecte feu = FeuDetecte.builder()
                .id(1L)
                .coordonneeX(4.81)
                .coordonneeY(45.73)
                .intensite(12)
                .statutFeu(StatutFeu.DETECTE)
                .build();
        FeuDetecte feu2 = FeuDetecte.builder()
                .id(2L)
                .coordonneeX(4.83)
                .coordonneeY(45.77)
                .intensite(11)
                .statutFeu(StatutFeu.DETECTE)
                .build();
        Camion camion =  Camion.builder()
                .id(1L)
                .capacite(3)
                .statut(StatutRessource.DISPONIBLE)
                .vitesse(50)
                .coordonneeX(4.82)
                .coordonneeY(45.74)
                .caserne(caserne)
                .build();
        Pompier pompier1 = Pompier.builder()
                .id(1L)
                .nom("Pompier 1")
                .prenom("Prenom 1")
                .statut(StatutRessource.DISPONIBLE)
                .caserne(caserne)
                .build();
        Pompier pompier2 = Pompier.builder()
                .id(2L)
                .nom("Pompier 2")
                .prenom("Prenom 2")
                .statut(StatutRessource.DISPONIBLE)
                .caserne(caserne)
                .build();
        Pompier pompier3 = Pompier.builder()
                .id(3L)
                .nom("Pompier 3")
                .prenom("Prenom 3")
                .statut(StatutRessource.DISPONIBLE)
                .caserne(caserne)
                .build();

        Flotte flotte = Flotte.builder()
                .id(1L)
                .caserne(caserne)
                .dateDepart(LocalDateTime.now())
                .feu(feu)
                .camions(List.of(camion))
                .pompiers(List.of(pompier1, pompier2, pompier3))
                .statutFlotte(StatutFlotte.EN_DEPLACEMENT)
                .build();

        when(feuDetecteService.getFeuById(1L)).thenReturn(feu).thenReturn(feu2);
        when(caserneService.getCaserneLaPlusProche(4.81, 45.73)).thenReturn(caserne);
        when(flotteService.creerFlotte(caserne, 3, feu)).thenReturn(flotte);

        //emergencyManagerService.getAllCapteursFromAPI();

        // WHEN
        emergencyManagerService.onCapteursUpdated(new CapteursUpdatedEvent(CapteursUpdatedEvent.class, capteurs));

        // THEN
        verify(feuDetecteService, times(2)).addFeuDetecte(any(FeuDetecte.class));
        verify(eventPublisher, times(2)).publishEvent(any(FeuDetecteEvent.class));


        // WHEN
        emergencyManagerService.onFeuDetecte(new FeuDetecteEvent(FeuDetecteEvent.class, feu.getId()));

        // THEN
        verify(feuDetecteService, times(1)).getFeuById(1L);
        verify(caserneService, times(1)).getCaserneLaPlusProche(4.81, 45.73);
        verify(flotteService, times(1)).creerFlotte(caserne,3, feu);
        verify(flotteService, times(1)).updateFlotte(flotte);
        verify(feuDetecteService, times(1)).updateFeuDetecte(feu);
        verify(eventPublisher, times(1)).publishEvent(any(FlotteAttribueeEvent.class));

        when(flotteService.getFlotteById(1L)).thenReturn(flotte);
        when(feuDetecteService.getFeuById(1L)).thenReturn(feu);

        // WHEN
        emergencyManagerService.onFlotteAttribuee(new FlotteAttribueeEvent(FlotteAttribueeEvent.class, flotte.getId()));

        // THEN
        verify(flotteService, times(1)).getFlotteById(1L);
        verify(feuDetecteService, times(2)).getFeuById(1L);
        verify(flotteService, times(1)).teleporterFlotteSurFeu(flotte);
        verify(feuDetecteService, times(1)).eteindreFeu(feu);
        verify(flotteService, times(1)).teleporterFlotteSurCaserne(flotte);
    }
}