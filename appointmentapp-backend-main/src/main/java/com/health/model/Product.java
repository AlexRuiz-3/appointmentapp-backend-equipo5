package com.health.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idProduct;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    private String presentation;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private Double unitPrice;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @Future(message = "La fecha de expiración debe ser futura")
    private LocalDate expired;

    @ManyToOne
    @JoinColumn(name = "id_category", nullable = false,
            foreignKey = @ForeignKey(name = "FK_PRODUCT_CATEGORY"))
    @NotNull(message = "La categoría es obligatoria")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "id_family", nullable = false,
            foreignKey = @ForeignKey(name = "FK_PRODUCT_FAMILY"))
    @NotNull(message = "La familia es obligatoria")
    private Family family;

    @ManyToOne
    @JoinColumn(name = "id_laboratory", nullable = false,
            foreignKey = @ForeignKey(name = "FK_PRODUCT_LABORATORY"))
    @NotNull(message = "El laboratorio es obligatoria")
    private Laboratory laboratory;
}
