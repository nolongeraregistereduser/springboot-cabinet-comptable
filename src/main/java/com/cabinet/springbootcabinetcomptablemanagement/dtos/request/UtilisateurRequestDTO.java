package com.cabinet.springbootcabinetcomptablemanagement.dtos.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UtilisateurRequestDTO {

    @NotBlank(message = "Email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial")
    private String password;

    @NotBlank(message = "Nom complet est obligatoire")
    private String nomComplet;

    @NotBlank(message = "Rôle est obligatoire")
    @Pattern(regexp = "SOCIETE|COMPTABLE", message = "Rôle doit être SOCIETE ou COMPTABLE")
    private String role;

    // Required only if role = SOCIETE
    private Long societeId;
}
