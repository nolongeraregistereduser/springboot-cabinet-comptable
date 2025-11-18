package com.cabinet.springbootcabinetcomptablemanagement.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Table(name="utilisateurs")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;  // bCrypt encoded inshalaah brebbi

    @Column(nullable = false)
    private String nomComplet;

    @Enumerated(EnumType.STRING)  // comptable wla societe
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "societe_id")
    private Societe societe;

    @Column(nullable = false)
    private Boolean actif = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


    public enum Role{SOCIETE,COMPTABLE}
}
