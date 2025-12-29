package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.DocumentType;

import java.time.LocalDate;

@Data
public class CampingOrderDTO {
    @NotBlank(message = "{msg.validation.required}")
    private String firstName;

    @NotBlank(message = "{msg.validation.required}")
    private String lastName;

    @NotNull(message = "{msg.validation.required}")
    private DocumentType documentType;

    @NotBlank(message = "{msg.validation.required}")
    private String documentNumber;

    @NotNull(message = "{msg.validation.required}")
    private LocalDate birthDate;

    @NotNull(message = "{msg.validation.required}")
    private Long campingTypeId; // El ID del tipo de camping
}