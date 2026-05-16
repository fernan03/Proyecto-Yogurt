package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Ingredient", description = "Ingrediente utilizado en una receta de yogurt")
public class IngredientDto {

    @Schema(description = "Nombre del ingrediente", example = "Leche entera")
    private String name;

    @Schema(description = "Cantidad del ingrediente", example = "1.0")
    private Double quantity;

    @Schema(description = "Unidad de medida", example = "litros")
    private String unit;

    @Schema(description = "Notas adicionales sobre el ingrediente", example = "Preferiblemente fresca")
    private String notes;

    @Schema(description = "Indica si el ingrediente es opcional", example = "false")
    private Boolean options;
}