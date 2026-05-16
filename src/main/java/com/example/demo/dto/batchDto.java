package com.example.demo.dto;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

public class batchDto {

    @Data
    @Schema(name = "StartBatchRequest", description = "Solicitud para iniciar un nuevo lote de yogurt")
    public static class StarBatchRequest {

        @Schema(description = "ID de la receta a utilizar", example = "1")
        private Long recipeId;

        @Schema(description = "Volumen personalizado de leche en litros (opcional)", example = "2.0")
        private Double customMilkVolume;

        @Schema(description = "Cantidad personalizada de cultivo iniciador en gramos (opcional)", example = "100.0")
        private Double customStarterAmonunt;
    }

    @Data
    @Schema(name = "FailRequest", description = "Motivo por el cual un lote fue marcado como fallido")
    public static class FailRequest {

        @Schema(description = "Razón del fallo del lote", example = "Temperatura fuera de rango")
        private String reason;
    }
}