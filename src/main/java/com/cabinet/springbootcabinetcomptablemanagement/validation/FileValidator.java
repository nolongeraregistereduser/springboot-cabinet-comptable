package com.cabinet.springbootcabinetcomptablemanagement.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private String[] allowedTypes;
    private long maxSize;

    @Override
    public void initialize(ValidFile constraintAnnotation) {
        this.allowedTypes = constraintAnnotation.allowedTypes();
        this.maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Le fichier ne peut pas être vide")
                    .addConstraintViolation();
            return false;
        }

        // Check file size
        if (file.getSize() > maxSize) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("La taille du fichier ne doit pas dépasser %d MB", maxSize / 1048576))
                    .addConstraintViolation();
            return false;
        }

        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !Arrays.asList(allowedTypes).contains(contentType)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Type de fichier non autorisé. Types acceptés: PDF, JPG, PNG")
                    .addConstraintViolation();
            return false;
        }

        // Check file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !hasValidExtension(originalFilename)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Extension de fichier non autorisée. Extensions acceptées: .pdf, .jpg, .jpeg, .png")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    private boolean hasValidExtension(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        return extension.equals("pdf") || extension.equals("jpg") ||
               extension.equals("jpeg") || extension.equals("png");
    }
}

