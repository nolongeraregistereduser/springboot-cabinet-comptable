package com.cabinet.springbootcabinetcomptablemanagement.services.Impl;

import com.cabinet.springbootcabinetcomptablemanagement.models.Societe;
import com.cabinet.springbootcabinetcomptablemanagement.repositories.SocieteRepository;
import com.cabinet.springbootcabinetcomptablemanagement.services.SocieteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional

public class SocieteServiceImpl implements SocieteService {

    private final SocieteRepository societeRepository;

    @Override
    public Societe createSociete(Societe societe) {
        return societeRepository.save(societe);
    }

    @Override
    public Optional<Societe> getSocieteById(Long id) {
        return societeRepository.findById(id);
    }

    @Override
    public Optional<Societe> getSocieteByIce(String ice) {
        return societeRepository.findByIce(ice);
    }

    @Override
    public List<Societe> getAllSocietes() {
        return societeRepository.findAll();
    }

    @Override
    public Societe updateSociete(Long id, Societe societe) {
        return null;

    }

    @Override
    public void deleteSociete(Long id) {
        societeRepository.deleteById(id);
    }
}
