package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.validation.FieldsComparison;

import java.math.BigDecimal;

@Entity
@Table(name = "camping_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldsComparison(
        first = "stockAvailable",
        second = "stockTotal",
        message = "{msg.validation.comparison.invalid}"
)
public class CampingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{msg.validation.required}")
    @Size(max = 50, message = "{msg.validation.size}")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @NotNull(message = "{msg.validation.required}")
    @PositiveOrZero(message = "{msg.validation.positive}")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull(message = "{msg.validation.required}")
    @PositiveOrZero(message = "{msg.validation.positive}")
    @Column(name = "stock_total", nullable = false)
    private Integer stockTotal;

    @NotNull(message = "{msg.validation.required}")
    @PositiveOrZero(message = "{msg.validation.positive}")
    @Column(name = "stock_available", nullable = false)
    private Integer stockAvailable;
}