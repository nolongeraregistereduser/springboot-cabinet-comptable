package com.cabinet.springbootcabinetcomptablemanagement.services;

import com.cabinet.springbootcabinetcomptablemanagement.models.Societe;

import java.util.List;
import java.util.Optional;

public interface SocieteService {

    Societe createSociete(Societe societe);

    Optional<Societe> getSocieteById(Long id);

    Optional<Societe> getSocieteByIce(String ice);

    List<Societe> getAllSocietes();

    Societe updateSociete(Long id, Societe societe);

    void deleteSociete(Long id);


}
