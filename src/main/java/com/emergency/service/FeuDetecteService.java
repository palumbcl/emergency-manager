package com.emergency.service;

import com.emergency.model.FeuDetecte;
import com.emergency.model.enums.StatutFeu;
import com.emergency.repository.FeuDetecteRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeuDetecteService {
    private final FeuDetecteRepository feuDetecteRepository;
    private final EntityManager entityManager;

    public FeuDetecte addFeuDetecte(FeuDetecte feuDetecte) {
        FeuDetecte feu = feuDetecteRepository.save(feuDetecte);
        entityManager.flush(); // Force la synchronisation avec la base
        return feu;
    }

    public FeuDetecte getFeuById(Long feuId) {
        return feuDetecteRepository.findById(feuId)
                .orElseThrow(() -> new RuntimeException("Feu non trouvé"));
    }

    public void eteindreFeu(FeuDetecte feu) throws InterruptedException {
        System.out.println("Extinction du feu : " + feu);
        Thread.sleep(10000);
        feu.setStatutFeu(StatutFeu.ETEINT);
        feu.setDateExtinction(LocalDateTime.now());
        feuDetecteRepository.save(feu);
    }

    public void updateFeuDetecte(FeuDetecte feu) {
        FeuDetecte feuUpdated = feuDetecteRepository.save(feu);
        entityManager.flush(); // Force la synchronisation avec la base
    }

    public List<FeuDetecte> getAllFeuDetectes() {
        return feuDetecteRepository.findAll();
    }

    public Optional<FeuDetecte> findFeuDetecteByCoordonneeXAndCoordonneeY(double coordonneeX, double coordonneeY) {
        return feuDetecteRepository.findByCoordonneeXAndCoordonneeY(coordonneeX, coordonneeY);
    }
}
