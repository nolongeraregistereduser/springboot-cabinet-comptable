package com.cabinet.springbootcabinetcomptablemanagement.dtos.response;

import com.cabinet.springbootcabinetcomptablemanagement.models.Document;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DocumentResponseDTO {

    private Long id;
    private String numeroPiece;
    private String type;
    private String categorieComptable;
    private LocalDate datePiece;
    private BigDecimal montant;
    private String fournisseur;
    private String nomFichierOriginal;
    private String statut;
    private String commentaireComptable;
    private LocalDateTime dateValidation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Societe info
    private Long societeId;
    private String societeRaisonSociale;

    public DocumentResponseDTO(Document document) {
        this.id = document.getId();
        this.numeroPiece = document.getNumeroPiece();
        this.type = document.getType().name();
        this.categorieComptable = document.getCategorieComptable();
        this.datePiece = document.getDatePiece();
        this.montant = document.getMontant();
        this.fournisseur = document.getFournisseur();
        this.nomFichierOriginal = document.getNomFichierOriginal();
        this.statut = document.getStatut().name();
        this.commentaireComptable = document.getCommentaireComptable();
        this.dateValidation = document.getDateValidation();
        this.createdAt = document.getCreatedAt();
        this.updatedAt = document.getUpdatedAt();

        if (document.getSociete() != null) {
            this.societeId = document.getSociete().getId();
            this.societeRaisonSociale = document.getSociete().getRaisonSociale();
        }
    }
}

