package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.validation.FieldMatch;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.validation.IsAdult;

import java.time.LocalDate;

@Data
@FieldMatch(
        first = "password",
        second = "confirmPassword"
)
public class UserRegistrationDTO {

    @NotBlank(message = "{msg.validation.required}")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "{msg.validation.email}")
    @Size(max = 100, message = "{msg.validation.size}")
    private String email;

    // ^                 : Inicio de la cadena
    // (?=.*[0-9])       : Al menos un dígito
    // (?=.*[a-z])       : Al menos una minúscula
    // (?=.*[A-Z])       : Al menos una mayúscula
    // (?=.*[@#$%^&+=!]) : Al menos un carácter especial
    // .{8,}             : Al menos 8 caracteres en total
    @NotBlank(message = "{msg.validation.required}")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
            message = "{msg.validation.password.complexity}"
    )
    private String password;

    @NotBlank(message = "{msg.validation.required}")
    private String confirmPassword;

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
