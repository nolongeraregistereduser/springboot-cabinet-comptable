package com.cabinet.springbootcabinetcomptablemanagement.repositories;

import com.cabinet.springbootcabinetcomptablemanagement.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document,Long> {

    @Query("SELECT d FROM Document d LEFT JOIN FETCH d.societe WHERE d.numeroPiece = :numeroPiece")
    Optional<Document> findByNumeroPiece(@Param("numeroPiece") String numeroPiece);

    @Query("SELECT d FROM Document d LEFT JOIN FETCH d.societe WHERE d.societe.id = :societeId AND d.statut = :statut")
    List<Document> findBySocieteIdAndStatut(@Param("societeId") Long societeId, @Param("statut") Document.StatutDocument statut);

    @Query("SELECT d FROM Document d LEFT JOIN FETCH d.societe WHERE d.societe.id = :societeId")
    List<Document> findBySocieteId(@Param("societeId") Long societeId);

    @Query("SELECT d FROM Document d LEFT JOIN FETCH d.societe WHERE d.statut = :statut")
    List<Document> findByStatut(@Param("statut") Document.StatutDocument statut);

    boolean existsByNumeroPiece(String numeroPiece);

    @Query("SELECT d FROM Document d LEFT JOIN FETCH d.societe WHERE d.exerciceComptable = :exerciceComptable")
    List<Document> findByExerciceComptable(@Param("exerciceComptable") String exerciceComptable);

    @Query("SELECT d FROM Document d LEFT JOIN FETCH d.societe WHERE d.id = :id")
    Optional<Document> findByIdWithSociete(@Param("id") Long id);

    @Query("SELECT DISTINCT d FROM Document d LEFT JOIN FETCH d.societe")
    List<Document> findAllWithSociete();
}
