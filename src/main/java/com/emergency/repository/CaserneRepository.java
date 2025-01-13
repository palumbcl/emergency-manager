package com.emergency.repository;

import com.emergency.model.Caserne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaserneRepository extends JpaRepository<Caserne, Long> {

}
