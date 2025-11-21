package com.cabinet.springbootcabinetcomptablemanagement.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "documents")
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @Column(nullable = false, unique = true)
        private String numeroPiece;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private TypeDocument type;

        @Column(nullable = false)
        private String categorieComptable;

        @Column(nullable = false)
        private LocalDate datePiece;

        @Column(nullable = false, precision = 15, scale = 2)
        private BigDecimal montant;

        @Column(nullable = false)
        private String fournisseur;

        @Column(nullable = false)
        private String cheminFichier; // File path

        @Column(nullable = false)
        private String nomFichierOriginal;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private StatutDocument statut = StatutDocument.EN_ATTENTE;

        private LocalDateTime dateValidation;

        @Column(length = 1000)
        private String commentaireComptable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "societe_id", nullable = false)
    private Societe societe;

    @Column(nullable = false)
    private String exerciceComptable; // Format: "2024" or "2024-2025"

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

        private LocalDateTime updatedAt;

        @PreUpdate
        protected void onUpdate() {
            updatedAt = LocalDateTime.now();
        }

    public void setCommentaire(String commentaire) {
        this.commentaireComptable = commentaire;
    }

    public void setDateModification(LocalDateTime now) {
        this.updatedAt = now;
    }

    public enum TypeDocument {
        FACTURE_ACHAT,
        FACTURE_VENTE,
        TICKET_CAISSE,
        RELEVE_BANCAIRE
    }

    public enum StatutDocument {
        EN_ATTENTE,
        VALIDE,
        REJETE
    }
}

