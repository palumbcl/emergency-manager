package com.emergency.service;

import com.emergency.model.Camion;
import com.emergency.repository.CamionRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CamionService {
    private final CamionRepository camionRepository;
    private final EntityManager entityManager;

    public List<Camion> getAllCamions() {
        return camionRepository.findAll();
    }

    public List<Camion> getCamionsByIds(List<Long> camionsIds) {
        return camionRepository.findAllById(camionsIds);
    }

    public Camion updateCamion(Camion camion) {
        Camion camionUpdated = camionRepository.save(camion);
        entityManager.flush(); // Force la synchronisation avec la base
        return camionUpdated;
    }
}
