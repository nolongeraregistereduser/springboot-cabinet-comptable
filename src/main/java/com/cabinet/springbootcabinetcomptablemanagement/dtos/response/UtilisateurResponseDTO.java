package com.cabinet.springbootcabinetcomptablemanagement.dtos.response;

import com.cabinet.springbootcabinetcomptablemanagement.models.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UtilisateurResponseDTO {

    private Long id;
    private String email;
    private String nomComplet;
    private String role;
    private Boolean actif;
    private LocalDateTime createdAt;

    // Societe info (only for SOCIETE role)
    private Long societeId;
    private String societeRaisonSociale;

    public UtilisateurResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nomComplet = user.getNomComplet();
        this.role = user.getRole().name();
        this.actif = user.getActif();
        this.createdAt = user.getCreatedAt();

        if (user.getSociete() != null) {
            this.societeId = user.getSociete().getId();
            this.societeRaisonSociale = user.getSociete().getRaisonSociale();
        }
    }
}
