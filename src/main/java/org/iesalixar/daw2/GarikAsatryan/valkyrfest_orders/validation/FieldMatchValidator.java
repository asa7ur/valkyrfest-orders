package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;
    private String message;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        // Leemos los nombres de los campos que configuramos en la anotación
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            // Usamos BeanWrapper para obtener los valores reales de los campos por su nombre
            final Object firstObj = new BeanWrapperImpl(value).getPropertyValue(firstFieldName);
            final Object secondObj = new BeanWrapperImpl(value).getPropertyValue(secondFieldName);

            // Comparamos si ambos son iguales (manejando posibles nulos)
            boolean isValid = firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);

            if (!isValid) {
                // Si no coinciden, desactivamos el error global y lo enviamos específicamente
                // al campo de "confirmación" para que el error aparezca justo debajo de ese input.
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(secondFieldName)
                        .addConstraintViolation();
            }

            return isValid;
        } catch (final Exception ignore) {
            // Si algo falla al acceder a los campos, devolvemos false por seguridad
            return false;
        }
    }
}