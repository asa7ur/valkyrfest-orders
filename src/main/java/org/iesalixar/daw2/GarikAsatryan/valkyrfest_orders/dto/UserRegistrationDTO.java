package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.validation.IsAdult;

import java.time.LocalDate;

@Data
public class UserRegistrationDTO {

    @NotBlank(message = "{msg.validation.required}")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "{msg.validation.email}")
    @Size(max = 100, message = "{msg.validation.size}")
    private String email;

    @NotBlank(message = "{msg.validation.required}")
    private String password;

    @NotEmpty(message = "{msg.validation.required}")
    @Size(max = 100, message = "{msg.validation.size}")
    private String firstName;

    @NotEmpty(message = "{msg.validation.required}")
    @Size(max = 100, message = "{msg.validation.size}")
    private String lastName;

    @NotNull(message = "{msg.validation.required}")
    @IsAdult
    private LocalDate birthDate;

    @NotEmpty(message = "{msg.validation.required}")
    @Size(max = 30, message = "{msg.validation.size}")
    private String phone;
}
