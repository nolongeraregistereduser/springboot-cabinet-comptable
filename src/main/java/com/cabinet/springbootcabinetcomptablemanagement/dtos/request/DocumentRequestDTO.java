package com.cabinet.springbootcabinetcomptablemanagement.dtos.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DocumentRequestDTO {

    @NotBlank(message = "Numéro de pièce est obligatoire")
    @Size(max = 50)
    private String numeroPiece;

    @NotBlank(message = "Type de document est obligatoire")
    @Pattern(regexp = "FACTURE_ACHAT|FACTURE_VENTE|TICKET_CAISSE|RELEVE_BANCAIRE",
            message = "Type de document invalide")
    private String type;

    @NotBlank(message = "Catégorie comptable est obligatoire")
    private String categorieComptable;

    @NotNull(message = "Date de la pièce est obligatoire")
    @PastOrPresent(message = "La date ne peut pas être dans le futur")
    private LocalDate datePiece;

    @NotNull(message = "Montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
    private BigDecimal montant;

    @NotBlank(message = "Fournisseur est obligatoire")
    private String fournisseur;

    @NotNull(message = "Fichier est obligatoire")
    private MultipartFile fichier;


}
