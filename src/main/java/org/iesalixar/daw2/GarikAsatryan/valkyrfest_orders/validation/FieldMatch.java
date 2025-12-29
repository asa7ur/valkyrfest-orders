package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación para validar que dos campos de una clase coinciden.
 */
@Target({ElementType.TYPE}) // Se aplica a la clase, no a un campo individual
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class) // Aquí vinculamos la lógica
public @interface FieldMatch {

    String message() default "{msg.validation.password.match}"; // Mensaje por defecto

    String first();  // Nombre del primer campo (password)

    String second(); // Nombre del segundo campo (confirmPassword)

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // Permite usar la anotación varias veces en la misma clase
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FieldMatch[] value();
    }
}