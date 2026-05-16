package com.example.demo.dto;

import java.util.List;

import com.example.demo.model.Recipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Recipe", description = "DTO para la creación y actualización de recetas de yogurt")
public class RecipeDto {

    @Schema(description = "Nombre de la receta", example = "Yogurt natural clásico")
    private String name;

    @Schema(description = "Descripción de la receta", example = "Receta tradicional de yogurt casero")
    private String description;

    @Schema(description = "Volumen de leche en litros", example = "1.0")
    private Double defaulMilkVolume;

    @Schema(description = "Cantidad de cultivo iniciador (starter) en gramos", example = "50.0")
    private Double defaulStarterAmount;

    @Schema(description = "Temperatura de calentamiento en °C", example = "85.0")
    private Double heatingTemperature;

    @Schema(description = "Duración del calentamiento en minutos", example = "30")
    private Integer heatingDuration;

    @Schema(description = "Temperatura de inoculación en °C", example = "43.0")
    private Double innoculationTemperature;

    @Schema(description = "Temperatura de incubación en °C", example = "42.0")
    private Double incubationTemperature;

    @Schema(description = "Tiempo mínimo de incubación en minutos", example = "240")
    private Integer minIncubationTime;

    @Schema(description = "Tiempo máximo de incubación en minutos", example = "480")
    private Integer maxIncubationTime;

    @Schema(description = "Tiempo de refrigeración en minutos", example = "180")
    private Integer refrigerationTime;

    @Schema(description = "Nivel de dificultad de la receta", example = "BEGINNER")
    private Recipe.DifficultyLevel difficulty;

    @Schema(description = "Consejos adicionales", example = "Mantener temperatura constante durante la incubación")
    private String tips;

    @Schema(description = "Lista de ingredientes de la receta")
    private List<IngredientDto> ingredients;
}