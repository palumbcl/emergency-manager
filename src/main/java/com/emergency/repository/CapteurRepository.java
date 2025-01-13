package com.emergency.repository;

import com.emergency.model.Capteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapteurRepository extends JpaRepository<Capteur, Long> {
}
