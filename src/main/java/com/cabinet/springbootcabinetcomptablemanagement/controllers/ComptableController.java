package com.cabinet.springbootcabinetcomptablemanagement.controllers;

import com.cabinet.springbootcabinetcomptablemanagement.dtos.request.DocumentValidationDTO;
import com.cabinet.springbootcabinetcomptablemanagement.dtos.response.DocumentResponseDTO;
import com.cabinet.springbootcabinetcomptablemanagement.exceptions.ResourceNotFoundException;
import com.cabinet.springbootcabinetcomptablemanagement.models.Document;
import com.cabinet.springbootcabinetcomptablemanagement.services.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Comptable Controller
 * Handles endpoints specific to COMPTABLE (Accountant) users
 * Only COMPTABLE role can access these endpoints
 */
@RestController
@RequestMapping("/api/comptable")
@RequiredArgsConstructor
@Slf4j
public class ComptableController {

    private final DocumentService documentService;

    /**
     * Get all pending documents (EN_ATTENTE status)
     * Only COMPTABLE users can access this endpoint
     * 
     * GET /api/comptable/documents/en-attente
     */
    @GetMapping("/documents/en-attente")
    @PreAuthorize("hasAuthority('ROLE_COMPTABLE')")
    public ResponseEntity<List<DocumentResponseDTO>> getPendingDocuments() {
        
        log.info("Récupération de tous les documents en attente");

        List<Document> pendingDocuments = documentService.getDocumentByStatut(Document.StatutDocument.EN_ATTENTE);
        
        List<DocumentResponseDTO> responseDTOs = pendingDocuments.stream()
                .map(DocumentResponseDTO::new)
                .collect(Collectors.toList());

        log.info("Nombre de documents en attente: {}", responseDTOs.size());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Validate a document
     * Only COMPTABLE users can validate documents
     * 
     * POST /api/comptable/documents/{id}/valider
     */
    @PostMapping("/documents/{id}/valider")
    @PreAuthorize("hasAuthority('ROLE_COMPTABLE')")
    public ResponseEntity<DocumentResponseDTO> validateDocument(
            @PathVariable Long id,
            @RequestBody(required = false) DocumentValidationDTO validationDTO) {
        
        log.info("Validation du document ID: {}", id);

        // Commentaire is optional for validation
        String commentaire = (validationDTO != null && validationDTO.getCommentaire() != null) 
                ? validationDTO.getCommentaire() 
                : null;

        Document validatedDocument = documentService.validateDocument(id, commentaire);
        DocumentResponseDTO responseDTO = new DocumentResponseDTO(validatedDocument);

        log.info("Document validé avec succès: ID={}, Statut={}", id, validatedDocument.getStatut());
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Reject a document
     * Only COMPTABLE users can reject documents
     * Commentaire (motif) is mandatory for rejection
     * 
     * POST /api/comptable/documents/{id}/rejeter
     */
    @PostMapping("/documents/{id}/rejeter")
    @PreAuthorize("hasAuthority('ROLE_COMPTABLE')")
    public ResponseEntity<DocumentResponseDTO> rejectDocument(
            @PathVariable Long id,
            @Valid @RequestBody DocumentValidationDTO validationDTO) {
        
        log.info("Rejet du document ID: {}", id);

        // Validate that commentaire is provided for rejection
        if (validationDTO.getCommentaire() == null || validationDTO.getCommentaire().trim().isEmpty()) {
            throw new IllegalArgumentException("Le motif de rejet est obligatoire");
        }

        Document rejectedDocument = documentService.rejectDocument(id, validationDTO.getCommentaire());
        DocumentResponseDTO responseDTO = new DocumentResponseDTO(rejectedDocument);

        log.info("Document rejeté avec succès: ID={}, Motif={}", id, validationDTO.getCommentaire());
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Get documents by société ID
     * Only COMPTABLE users can access this endpoint
     * 
     * GET /api/comptable/documents/societe/{societeId}
     */
    @GetMapping("/documents/societe/{societeId}")
    @PreAuthorize("hasAuthority('ROLE_COMPTABLE')")
    public ResponseEntity<List<DocumentResponseDTO>> getDocumentsBySociete(
            @PathVariable Long societeId) {
        
        log.info("Récupération des documents pour la société: {}", societeId);

        List<Document> documents = documentService.getDocumentBySociete(societeId);
        List<DocumentResponseDTO> responseDTOs = documents.stream()
                .map(DocumentResponseDTO::new)
                .collect(Collectors.toList());

        log.info("Nombre de documents trouvés pour la société {}: {}", societeId, responseDTOs.size());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Get documents by exercice comptable
     * Only COMPTABLE users can access this endpoint
     * 
     * GET /api/comptable/documents/exercice/{exercice}
     */
    @GetMapping("/documents/exercice/{exercice}")
    @PreAuthorize("hasAuthority('ROLE_COMPTABLE')")
    public ResponseEntity<List<DocumentResponseDTO>> getDocumentsByExercice(
            @PathVariable String exercice) {
        
        log.info("Récupération des documents pour l'exercice comptable: {}", exercice);

        List<Document> documents = documentService.getDocumentByExerciceComptable(exercice);
        List<DocumentResponseDTO> responseDTOs = documents.stream()
                .map(DocumentResponseDTO::new)
                .collect(Collectors.toList());

        log.info("Nombre de documents trouvés pour l'exercice {}: {}", exercice, responseDTOs.size());
        return ResponseEntity.ok(responseDTOs);
    }
}
