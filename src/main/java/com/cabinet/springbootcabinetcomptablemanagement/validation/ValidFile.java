package com.cabinet.springbootcabinetcomptablemanagement.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
@Documented
public @interface ValidFile {
    String message() default "Fichier invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String[] allowedTypes() default {"application/pdf", "image/jpeg", "image/png"};
    long maxSize() default 10485760L; // 10MB in bytes
}

