package com.cabinet.springbootcabinetcomptablemanagement.controllers;

import com.cabinet.springbootcabinetcomptablemanagement.dtos.request.DocumentRequestDTO;
import com.cabinet.springbootcabinetcomptablemanagement.dtos.response.DocumentResponseDTO;
import com.cabinet.springbootcabinetcomptablemanagement.exceptions.ResourceNotFoundException;
import com.cabinet.springbootcabinetcomptablemanagement.exceptions.UnauthorizedException;
import com.cabinet.springbootcabinetcomptablemanagement.models.Document;
import com.cabinet.springbootcabinetcomptablemanagement.models.User;
import com.cabinet.springbootcabinetcomptablemanagement.repositories.UtilisateurRepository;
import com.cabinet.springbootcabinetcomptablemanagement.services.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Document Controller
 * Handles document upload, listing, download, and deletion
 * Role-based access: SOCIETE users can only access their own documents, COMPTABLE can access all
 */
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

    private final DocumentService documentService;
    private final UtilisateurRepository utilisateurRepository;

    /**
     * Get current authenticated user from SecurityContext
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Utilisateur non authentifié");
        }

        String email = authentication.getName();
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> ResourceNotFoundException.ofField("Utilisateur", "email", email));
    }

    /**
     * Upload a new document
     * Only SOCIETE users can upload documents for their société
     * 
     * POST /api/documents
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('SOCIETE')")
    public ResponseEntity<DocumentResponseDTO> uploadDocument(
            @Valid @ModelAttribute DocumentRequestDTO requestDTO) {
        
        log.info("Upload de document: numéro={}, type={}", requestDTO.getNumeroPiece(), requestDTO.getType());

        User currentUser = getCurrentUser();

        // Vérifier que l'utilisateur SOCIETE ne peut uploader que pour sa propre société
        if (currentUser.getSociete() == null) {
            throw new UnauthorizedException("L'utilisateur SOCIETE doit être associé à une société");
        }

        // Vérifier que la société dans la requête correspond à la société de l'utilisateur
        if (!currentUser.getSociete().getId().equals(requestDTO.getSocieteId())) {
            throw new UnauthorizedException("Vous ne pouvez uploader des documents que pour votre propre société");
        }

        // Convertir le type de document depuis String vers enum
        Document.TypeDocument typeDocument;
        try {
            typeDocument = Document.TypeDocument.valueOf(requestDTO.getType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type de document invalide: " + requestDTO.getType());
        }

        // Créer le document
        Document document = documentService.createDocument(
                requestDTO.getNumeroPiece(),
                typeDocument,
                requestDTO.getCategorieComptable(),
                requestDTO.getDatePiece(),
                requestDTO.getMontant(),
                requestDTO.getFournisseur(),
                requestDTO.getFichier(),
                requestDTO.getSocieteId(),
                requestDTO.getExerciceComptable().toString()
        );

        DocumentResponseDTO responseDTO = new DocumentResponseDTO(document);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Get all documents
     * SOCIETE users see only their société's documents
     * COMPTABLE users see all documents
     * 
     * GET /api/documents
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('SOCIETE', 'COMPTABLE')")
    public ResponseEntity<List<DocumentResponseDTO>> getAllDocuments(
            @RequestParam(required = false) String exerciceComptable) {
        
        log.info("Récupération de tous les documents");

        User currentUser = getCurrentUser();
        List<Document> documents;

        if (currentUser.getRole() == User.Role.COMPTABLE) {
            // Comptable voit tous les documents
            if (exerciceComptable != null && !exerciceComptable.isEmpty()) {
                documents = documentService.getDocumentByExerciceComptable(exerciceComptable);
            } else {
                documents = documentService.getAllDocuments();
            }
        } else {
            // Société voit seulement ses propres documents
            if (currentUser.getSociete() == null) {
                throw new UnauthorizedException("L'utilisateur SOCIETE doit être associé à une société");
            }

            Long societeId = currentUser.getSociete().getId();
            if (exerciceComptable != null && !exerciceComptable.isEmpty()) {
                documents = documentService.getDocumentBySocieteAndExercice(societeId, exerciceComptable);
            } else {
                documents = documentService.getDocumentBySociete(societeId);
            }
        }

        List<DocumentResponseDTO> responseDTOs = documents.stream()
                .map(DocumentResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Get documents by exercice comptable
     * 
     * GET /api/documents/exercice/{exercice}
     */
    @GetMapping("/exercice/{exercice}")
    @PreAuthorize("hasAnyRole('SOCIETE', 'COMPTABLE')")
    public ResponseEntity<List<DocumentResponseDTO>> getDocumentsByExercice(
            @PathVariable String exercice) {
        
        log.info("Récupération des documents pour l'exercice: {}", exercice);

        User currentUser = getCurrentUser();
        List<Document> documents;

        if (currentUser.getRole() == User.Role.COMPTABLE) {
            documents = documentService.getDocumentByExerciceComptable(exercice);
        } else {
            if (currentUser.getSociete() == null) {
                throw new UnauthorizedException("L'utilisateur SOCIETE doit être associé à une société");
            }
            documents = documentService.getDocumentBySocieteAndExercice(
                    currentUser.getSociete().getId(), exercice);
        }

        List<DocumentResponseDTO> responseDTOs = documents.stream()
                .map(DocumentResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Get documents by société ID
     * Only COMPTABLE can access this endpoint
     * 
     * GET /api/documents/societe/{societeId}
     */
    @GetMapping("/societe/{societeId}")
    @PreAuthorize("hasRole('COMPTABLE')")
    public ResponseEntity<List<DocumentResponseDTO>> getDocumentsBySociete(
            @PathVariable Long societeId) {
        
        log.info("Récupération des documents pour la société: {}", societeId);

        List<Document> documents = documentService.getDocumentBySociete(societeId);
        List<DocumentResponseDTO> responseDTOs = documents.stream()
                .map(DocumentResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Get a single document by ID
     * SOCIETE users can only access their own société's documents
     * COMPTABLE users can access any document
     * 
     * GET /api/documents/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SOCIETE', 'COMPTABLE')")
    public ResponseEntity<DocumentResponseDTO> getDocumentById(@PathVariable Long id) {
        
        log.info("Récupération du document ID: {}", id);

        Document document = documentService.getDocumentById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Document", id));

        User currentUser = getCurrentUser();

        // Vérifier l'accès: SOCIETE ne peut voir que ses propres documents
        if (currentUser.getRole() == User.Role.SOCIETE) {
            if (currentUser.getSociete() == null) {
                throw new UnauthorizedException("L'utilisateur SOCIETE doit être associé à une société");
            }
            if (!document.getSociete().getId().equals(currentUser.getSociete().getId())) {
                throw new UnauthorizedException("Vous n'avez pas accès à ce document");
            }
        }

        DocumentResponseDTO responseDTO = new DocumentResponseDTO(document);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Download document file
     * SOCIETE users can only download their own société's documents
     * COMPTABLE users can download any document
     * 
     * GET /api/documents/{id}/download
     */
    @GetMapping("/{id}/download")
    @PreAuthorize("hasAnyRole('SOCIETE', 'COMPTABLE')")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        
        log.info("Téléchargement du document ID: {}", id);

        Document document = documentService.getDocumentById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Document", id));

        User currentUser = getCurrentUser();

        // Vérifier l'accès: SOCIETE ne peut télécharger que ses propres documents
        if (currentUser.getRole() == User.Role.SOCIETE) {
            if (currentUser.getSociete() == null) {
                throw new UnauthorizedException("L'utilisateur SOCIETE doit être associé à une société");
            }
            if (!document.getSociete().getId().equals(currentUser.getSociete().getId())) {
                throw new UnauthorizedException("Vous n'avez pas accès à ce document");
            }
        }

        byte[] fileContent = documentService.downloadDocument(id);

        // Déterminer le type MIME basé sur l'extension du fichier
        String contentType = determineContentType(document.getNomFichierOriginal());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment", document.getNomFichierOriginal());

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }

    /**
     * Delete a document
     * SOCIETE users can only delete their own société's documents
     * COMPTABLE users can delete any document
     * 
     * DELETE /api/documents/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SOCIETE', 'COMPTABLE')")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        
        log.info("Suppression du document ID: {}", id);

        Document document = documentService.getDocumentById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Document", id));

        User currentUser = getCurrentUser();

        // Vérifier l'accès: SOCIETE ne peut supprimer que ses propres documents
        if (currentUser.getRole() == User.Role.SOCIETE) {
            if (currentUser.getSociete() == null) {
                throw new UnauthorizedException("L'utilisateur SOCIETE doit être associé à une société");
            }
            if (!document.getSociete().getId().equals(currentUser.getSociete().getId())) {
                throw new UnauthorizedException("Vous n'avez pas accès à ce document");
            }
        }

        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Helper method to determine content type from filename
     */
    private String determineContentType(String filename) {
        if (filename == null) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        String lowerFilename = filename.toLowerCase();
        if (lowerFilename.endsWith(".pdf")) {
            return MediaType.APPLICATION_PDF_VALUE;
        } else if (lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG_VALUE;
        } else if (lowerFilename.endsWith(".png")) {
            return MediaType.IMAGE_PNG_VALUE;
        }

        return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }
}
