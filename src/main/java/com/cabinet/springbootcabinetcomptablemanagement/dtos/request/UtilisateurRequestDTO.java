package com.cabinet.springbootcabinetcomptablemanagement.dtos.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UtilisateurRequestDTO {

    @NotBlank(message = "Email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    @NotBlank(message = "Nom complet est obligatoire")
    private String nomComplet;

    @NotBlank(message = "Rôle est obligatoire")
    @Pattern(regexp = "SOCIETE|COMPTABLE", message = "Rôle doit être SOCIETE ou COMPTABLE")
    private String role;

    // Required only if role = SOCIETE
    private Long societeId;
}
