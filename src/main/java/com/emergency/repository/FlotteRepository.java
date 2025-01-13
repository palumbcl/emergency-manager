package com.emergency.repository;

import com.emergency.model.FeuDetecte;
import com.emergency.model.Flotte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlotteRepository extends JpaRepository<Flotte, Long> {
    Flotte findByFeu(FeuDetecte feu);
}
