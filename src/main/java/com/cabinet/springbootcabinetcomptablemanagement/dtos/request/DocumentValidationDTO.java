package com.cabinet.springbootcabinetcomptablemanagement.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DocumentValidationDTO {

    @NotNull(message = "ID du document est obligatoire")
    private Long documentId;

    @NotBlank(message = "Action est obligatoire")
    @Pattern(regexp = "VALIDE|REJETE", message = "Action doit être VALIDE ou REJETE")
    private String action;

    // Required if action = REJETE
    @NotBlank(message = "Motif de rejet est obligatoire", groups = RejectionValidation.class)
    private String commentaire;

    public interface RejectionValidation {}
}
