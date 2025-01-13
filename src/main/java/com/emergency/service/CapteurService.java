package com.emergency.service;

import com.emergency.model.Capteur;
import com.emergency.repository.CapteurRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CapteurService {

    private final CapteurRepository capteurRepository;

    private final EntityManager entityManager;

    public List<Capteur> getAllCapteurs() {
        return capteurRepository.findAll();
    }

    @Transactional
    public Capteur addCapteur(Capteur capteur) {
        return capteurRepository.save(capteur);
    }

    @Transactional
    public void addListCapteurs(List<Capteur> capteurs) {
        for (Capteur capteur : capteurs) {
            if (capteur.getId() != null && capteurRepository.existsById(capteur.getId())) {
                capteurRepository.save(capteur); // Mise à jour si existant
            } else {
                capteurRepository.save(capteur); // Insertion si nouveau
            }
        }
    }

    public boolean isCapteurExist(Long id) {
        return capteurRepository.existsById(id);
    }

    public Capteur getCapteurById(Long id) {
        return capteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Capteur non trouvé"));
    }

    @Transactional
    public void updateCapteur(Capteur existingCapteur) {
        capteurRepository.save(existingCapteur);
    }

    @Transactional
    public List<Capteur> getCapteursVoisins(Capteur capteur, int portee) {
        // Facteurs de conversion pour la latitude et la longitude
        double latitudeMoyenne = Math.toRadians(capteur.getCoordonneeY()); // Convertir en radians
        double kX = Math.cos(latitudeMoyenne) * 111_000; // Conversion longitude -> mètres
        double kY = 111_000; // Conversion latitude -> mètres

        return capteurRepository.findAll().stream()
                .filter(c -> !c.equals(capteur)) // Exclure le capteur de référence
                .filter(c -> {
                    double deltaX = (c.getCoordonneeX() - capteur.getCoordonneeX()) * kX; // Distance en mètres
                    double deltaY = (c.getCoordonneeY() - capteur.getCoordonneeY()) * kY; // Distance en mètres
                    double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY); // Distance euclidienne
                    return distance <= portee; // Vérifier si dans la portée
                })
                .toList();
    }

    @Transactional
    public double calculerCoordonneeMoyenneX(Capteur capteurLePlusProche, List<Capteur> capteursVoisins) {
        return capteursVoisins.stream()
                .mapToDouble(Capteur::getCoordonneeX)
                .average()
                .orElse(capteurLePlusProche.getCoordonneeX());
    }

    @Transactional
    public double calculerCoordonneeMoyenneY(Capteur capteurLePlusProche, List<Capteur> capteursVoisins) {
        return capteursVoisins.stream()
                .mapToDouble(Capteur::getCoordonneeY)
                .average()
                .orElse(capteurLePlusProche.getCoordonneeY());
    }

    @Transactional
    public void saveOrUpdateCapteur(Capteur capteur) {
        if (capteur.getId() != null && capteurRepository.existsById(capteur.getId())) {
            Capteur existingCapteur = capteurRepository.findById(capteur.getId()).orElseThrow();
            existingCapteur.setTemperature(capteur.getTemperature());
            existingCapteur.setCoordonneeX(capteur.getCoordonneeX());
            existingCapteur.setCoordonneeY(capteur.getCoordonneeY());
            existingCapteur.setEtat(capteur.getEtat());
            capteurRepository.save(existingCapteur);
        } else {
            entityManager.detach(capteur);
            capteurRepository.save(capteur);
        }
    }
}
