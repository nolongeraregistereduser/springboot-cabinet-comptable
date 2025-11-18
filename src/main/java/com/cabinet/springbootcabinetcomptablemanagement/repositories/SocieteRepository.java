package com.cabinet.springbootcabinetcomptablemanagement.repositories;

import com.cabinet.springbootcabinetcomptablemanagement.models.Societe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocieteRepository extends JpaRepository<Societe, Long> {

    Optional<Societe> findByIce(String ice);

    boolean existsByIce(String ice);

}
