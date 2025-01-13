package com.emergency.repository;

import com.emergency.model.FeuDetecte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeuDetecteRepository extends JpaRepository<FeuDetecte, Long> {
    Optional<FeuDetecte> findByCoordonneeXAndCoordonneeY(double coordonneeX, double coordonneeY);
}
