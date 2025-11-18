package com.cabinet.springbootcabinetcomptablemanagement.dtos.response;

import com.cabinet.springbootcabinetcomptablemanagement.models.Societe;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SocieteResponseDTO {

    private Long id;
    private String raisonSociale;
    private String ice;
    private String adresse;
    private String telephone;
    private String emailContact;
    private Boolean actif;
    private LocalDateTime createdAt;

    public SocieteResponseDTO(Societe societe) {
        this.id = societe.getId();
        this.raisonSociale = societe.getRaisonSociale();
        this.ice = societe.getIce();
        this.adresse = societe.getAdresse();
        this.telephone = societe.getTelephone();
        this.emailContact = societe.getEmailContact();
        this.actif = societe.getActif();
        this.createdAt = societe.getCreatedAt();
    }
}
