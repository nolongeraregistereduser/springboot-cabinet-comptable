package com.cabinet.springbootcabinetcomptablemanagement.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SocieteRequestDTO {

    @NotBlank(message = "Raison sociale est obligatoire")
    private String raisonSociale;

    @NotBlank(message = "ICE est obligatoire")
    @Pattern(regexp = "^[0-9]{15}$", message = "ICE doit contenir exactement 15 chiffres")
    private String ice;

    @NotBlank(message = "Adresse est obligatoire")
    private String adresse;

    @NotBlank(message = "Téléphone est obligatoire")
    @Pattern(regexp = "^(\\+212|0)[5-7][0-9]{8}$",
            message = "Format de téléphone marocain invalide")
    private String telephone;

    @NotBlank(message = "Email de contact est obligatoire")
    @Email(message = "Format d'email invalide")
    private String emailContact;
}
