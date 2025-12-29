package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsAdultValidator.class)
public @interface IsAdult {
    String message() default "{msg.notAdult}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
