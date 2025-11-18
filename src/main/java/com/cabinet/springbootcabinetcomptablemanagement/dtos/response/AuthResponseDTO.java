package com.cabinet.springbootcabinetcomptablemanagement.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {

    private String token;
    private String type = "Bearer";
    private Long userId;
    private String email;
    private String nomComplet;
    private String role; // Changed from User.Role to String
    private Long societeId;
    private String societeRaisonSociale; // null if COMPTABLE

    // Constructor for users without societe (COMPTABLE)
    public AuthResponseDTO(String token, Long userId, String email, String nomComplet, String role) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.nomComplet = nomComplet;
        this.role = role;
    }
}
