package com.cabinet.springbootcabinetcomptablemanagement.repositories;

import com.cabinet.springbootcabinetcomptablemanagement.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document,Long> {

    Optional<Document> findByNumeroPiece(String numeroPiece);

    List<Document> findBySocieteIdAndStatus(Long societeId, Document.StatutDocument statutDocument);

    List<Document> findBySocieteId(Long societeId);

    List<Document> findByStatut(Document.StatutDocument statut);

    boolean existsByNumeroPiece(String numeroPiece);
}
