package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class IsAdultValidator implements ConstraintValidator<IsAdult, LocalDate> {
    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return true;
        }
        return Period.between(birthDate, LocalDate.now()).getYears() >= 18;
    }
}
