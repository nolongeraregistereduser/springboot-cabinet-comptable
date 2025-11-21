package com.cabinet.springbootcabinetcomptablemanagement.services.Impl;

import com.cabinet.springbootcabinetcomptablemanagement.exceptions.DuplicateResourceException;
import com.cabinet.springbootcabinetcomptablemanagement.exceptions.ResourceNotFoundException;
import com.cabinet.springbootcabinetcomptablemanagement.models.Document;
import com.cabinet.springbootcabinetcomptablemanagement.models.Societe;
import com.cabinet.springbootcabinetcomptablemanagement.repositories.DocumentRepository;
import com.cabinet.springbootcabinetcomptablemanagement.repositories.SocieteRepository;
import com.cabinet.springbootcabinetcomptablemanagement.services.DocumentService;
import com.cabinet.springbootcabinetcomptablemanagement.services.FileStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;
    private final SocieteRepository societeRepository;

    @Override
    public Document createDocument(String numeroPiece, Document.TypeDocument type, String categorieComptable,
                                   LocalDate datePiece, BigDecimal montant, String fournisseur,
                                   MultipartFile file, Long societeId, String exerciceComptable) {
        log.info("Création d'un nouveau document: numéro={}, société={}", numeroPiece, societeId);

        // Vérifier que le numéro de pièce n'existe pas déjà
        if (documentRepository.existsByNumeroPiece(numeroPiece)) {
            throw new DuplicateResourceException("Un document avec le numéro de pièce '" + numeroPiece + "' existe déjà");
        }

        // Vérifier que la société existe
        Societe societe = societeRepository.findById(societeId)
                .orElseThrow(() -> ResourceNotFoundException.of("Société", societeId));

        // Valider le fichier
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Le fichier est requis");
        }

        // Validation de la taille du fichier (10MB max)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("La taille du fichier ne doit pas dépasser 10MB");
        }

        // Validation du type de fichier
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("application/pdf") ||
                        contentType.startsWith("image/"))) {
            throw new IllegalArgumentException("Seuls les fichiers PDF, JPG et PNG sont acceptés");
        }

        try {
            // Stocker le fichier
            String storedFileName = fileStorageService.storeFile(file);

            // Créer le document
            Document document = new Document();
            document.setNumeroPiece(numeroPiece);
            document.setType(type);
            document.setCategorieComptable(categorieComptable);
            document.setDatePiece(datePiece);
            document.setMontant(montant);
            document.setFournisseur(fournisseur);
            document.setCheminFichier(storedFileName);
            document.setNomFichierOriginal(file.getOriginalFilename());
            document.setStatut(Document.StatutDocument.EN_ATTENTE);
            document.setSociete(societe);
            document.setExerciceComptable(exerciceComptable);
            document.setCreatedAt(LocalDateTime.now());

            Document savedDocument = documentRepository.save(document);
            log.info("Document créé avec succès: ID={}, numéro={}", savedDocument.getId(), numeroPiece);

            return savedDocument;

        } catch (Exception e) {
            log.error("Erreur lors de la création du document", e);
            throw new RuntimeException("Erreur lors de la création du document: " + e.getMessage(), e);
        }
    }

    @Override
    public Document updateDocument(Document document, MultipartFile file) {
        log.info("Mise à jour du document ID: {}", document.getId());

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Le fichier est requis");
        }

        // Validation de la taille du fichier (10MB max)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("La taille du fichier ne doit pas dépasser 10MB");
        }

        // Validation du type de fichier
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("application/pdf") ||
                        contentType.startsWith("image/"))) {
            throw new IllegalArgumentException("Seuls les fichiers PDF, JPG et PNG sont acceptés");
        }

        try {
            String fileName = fileStorageService.storeFile(file);
            document.setCheminFichier(fileName);
            document.setStatut(Document.StatutDocument.EN_ATTENTE);
            document.setDatePiece(LocalDate.now());
            document.setDateModification(LocalDateTime.now());

            return documentRepository.save(document);

        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour du document", e);
            throw new RuntimeException("Erreur lors de la sauvegarde du document: " + e.getMessage(), e);
        }

    }

    @Override
    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public List<Document> getDocumentBySociete(Long societeId) {
        return documentRepository.findBySocieteId(societeId);
    }

    @Override
    public List<Document> getDocumentByExerciceComptable(String exerciseComputable) {
        log.info("Récupération des documents pour l'exercice comptable: {}", exerciseComputable);
        return documentRepository.findByExerciceComptable(exerciseComputable);
    }

    @Override
    public List<Document> getDocumentBySocieteAndExercice(Long societeId, String exerciseComputable) {
        log.info("Récupération des documents pour société {} et exercice {}", societeId, exerciseComputable);
        return documentRepository.findBySocieteId(societeId).stream()
                .filter(doc -> doc.getExerciceComptable() != null && doc.getExerciceComptable().equals(exerciseComputable))
                .toList();
    }

    @Override
    public Document validateDocument(Long id, String commentaire) {
        log.info("Validation du document ID: {}", id);

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document non trouvé avec l'ID: " + id));

        if (document.getStatut() != Document.StatutDocument.EN_ATTENTE) {
            throw new IllegalStateException("Seuls les documents en attente peuvent être validés");
        }

        document.setStatut(Document.StatutDocument.VALIDE);
        document.setCommentaire(commentaire);
        document.setDateValidation(LocalDateTime.now());
        document.setDateModification(LocalDateTime.now());

        Document validatedDocument = documentRepository.save(document);
        log.info("Document validé avec succès: {}", id);

        return validatedDocument;
    }

    @Override
    public Document rejectDocument(Long id, String motif) {

        if (motif == null || motif.trim().isEmpty()) {
            throw new IllegalArgumentException("Le motif de rejet est obligatoire");
        }

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document non trouvé avec l'ID: " + id));

        if (document.getStatut() != Document.StatutDocument.EN_ATTENTE) {
            throw new IllegalStateException("Seuls les documents en attente peuvent être rejetés");
        }

        document.setStatut(Document.StatutDocument.REJETE);
        document.setCommentaire(motif);
        document.setDateValidation(LocalDateTime.now());
        document.setDateModification(LocalDateTime.now());

        return documentRepository.save(document);


    }

    @Override
    public void deleteDocument(Long id) {
        log.info("Suppression du document ID: {}", id);

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document non trouvé avec l'ID: " + id));

        // Supprimer le fichier physique si présent
        if (document.getCheminFichier() != null && !document.getCheminFichier().isEmpty()) {
            try {
                fileStorageService.deleteFile(document.getCheminFichier());
                log.info("Fichier supprimé: {}", document.getCheminFichier());
            } catch (Exception e) {
                log.warn("Impossible de supprimer le fichier: {}", document.getCheminFichier(), e);
            }
        }

        documentRepository.delete(document);
        log.info("Document supprimé avec succès: {}", id);
    }

    @Override
    public byte[] downloadDocument(Long id) {
        log.info("Téléchargement du document ID: {}", id);

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document non trouvé avec l'ID: " + id));

        if (document.getCheminFichier() == null || document.getCheminFichier().isEmpty()) {
            throw new IllegalStateException("Aucun fichier associé à ce document");
        }

        try {
            return fileStorageService.loadFile(document.getCheminFichier());
        } catch (Exception e) {
            log.error("Erreur lors du téléchargement du document", e);
            throw new RuntimeException("Erreur lors du téléchargement du fichier: " + e.getMessage(), e);
        }
    }
}
