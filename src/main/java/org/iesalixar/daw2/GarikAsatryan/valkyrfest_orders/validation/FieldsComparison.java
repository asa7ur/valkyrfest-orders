package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldsComparisonValidator.class)
@Repeatable(FieldsComparison.List.class)
public @interface FieldsComparison {
    String message() default "El primer campo debe ser menor que el segundo";

    String first();  // Nombre del campo 1

    String second(); // Nombre del campo 2

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // Para permitir varias anotaciones en una misma clase
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FieldsComparison[] value();
    }
}
