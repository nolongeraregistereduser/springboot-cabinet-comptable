package com.cabinet.springbootcabinetcomptablemanagement.controllers;

import com.cabinet.springbootcabinetcomptablemanagement.dtos.response.DocumentResponseDTO;
import com.cabinet.springbootcabinetcomptablemanagement.exceptions.ResourceNotFoundException;
import com.cabinet.springbootcabinetcomptablemanagement.exceptions.UnauthorizedException;
import com.cabinet.springbootcabinetcomptablemanagement.models.Document;
import com.cabinet.springbootcabinetcomptablemanagement.models.User;
import com.cabinet.springbootcabinetcomptablemanagement.repositories.UtilisateurRepository;
import com.cabinet.springbootcabinetcomptablemanagement.services.DocumentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Société Controller
 * Handles endpoints specific to SOCIETE users
 */
@RestController
@RequestMapping("/api/societe")
@RequiredArgsConstructor
@Slf4j
public class SocieteController {

    private final DocumentService documentService;
    private final UtilisateurRepository utilisateurRepository;


    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Utilisateur non authentifié");
        }

        String email = authentication.getName();
        User user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> ResourceNotFoundException.ofField("Utilisateur", "email", email));
        
        // Force load société to avoid LazyInitializationException
        if (user.getSociete() != null) {
            // Access the société to trigger lazy loading within transaction
            user.getSociete().getId();
            user.getSociete().getRaisonSociale();
        }
        
        return user;
    }


    @GetMapping("/documents")
    @PreAuthorize("hasRole('SOCIETE')")
    @Transactional
    public ResponseEntity<List<DocumentResponseDTO>> getMySocieteDocuments(
            @RequestParam(required = false) String exerciceComptable) {
        
        try {
            log.info("Récupération des documents de la société de l'utilisateur");

            User currentUser = getCurrentUser();

            if (currentUser.getSociete() == null) {
                throw new UnauthorizedException("L'utilisateur SOCIETE doit être associé à une société");
            }

            Long societeId = currentUser.getSociete().getId();
            log.debug("Société ID: {}", societeId);
            
            List<Document> documents;

            if (exerciceComptable != null && !exerciceComptable.isEmpty()) {
                documents = documentService.getDocumentBySocieteAndExercice(societeId, exerciceComptable);
            } else {
                documents = documentService.getDocumentBySociete(societeId);
            }

            List<DocumentResponseDTO> responseDTOs = documents.stream()
                    .map(DocumentResponseDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des documents", e);
            throw e;
        }
    }
}
