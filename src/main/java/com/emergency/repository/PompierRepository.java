package com.emergency.repository;

import com.emergency.model.Pompier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PompierRepository extends JpaRepository<Pompier, Long> {

}
