package com.cabinet.springbootcabinetcomptablemanagement.services;

import com.cabinet.springbootcabinetcomptablemanagement.models.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface DocumentService {

    Document updateDocument(Document document, MultipartFile file);

    Optional<Document> getDocumentById(Long id);

    List<Document> getAllDocuments();

    List<Document> getDocumentBySociete(Long societeId);

    List<Document> getDocumentByExerciceComptable(String exerciseComputable);

    List<Document> getDocumentBySocieteAndExercice(Long societeId, String exerciseComputable);

    Document validateDocument(Long id, String commentaireComptable);

    Document rejectDocument(Long id, String commentaireComptable);

    void deleteDocument(Long id);

    byte[] downloadDocument(Long id);




}
