package com.emergency.service;

import com.emergency.mapper.PompierMapper;
import com.emergency.model.Pompier;
import com.emergency.model.enums.StatutRessource;
import com.emergency.repository.PompierRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PompierService {
    private final PompierRepository repository;
    private final PompierMapper pompierMapper;
    private final CaserneService caserneService;
    private final EntityManager entityManager;

    public List<Pompier> getAllPompiers() {
        return repository.findAll();
    }

    public Pompier addPompier(Pompier pompier) {
        Pompier pompierAdded = repository.save(pompier);
        entityManager.flush(); // Force la synchronisation avec la base
        return pompierAdded;
    }

    public Pompier updatePompier(Pompier pompier) {
        Pompier pompierUpdated = repository.save(pompier);
        entityManager.flush(); // Force la synchronisation avec la base
        return pompierUpdated;
    }

    public List<Pompier> getPompiersDisponibles(){
        return repository.findAll()
                .stream()
                .filter(p -> p.getStatut() == StatutRessource.DISPONIBLE)
                .toList();
    }

    public List<Pompier> getPompiersByIds(List<Long> pompiersIds) {
        return repository.findAllById(pompiersIds);
    }
}
