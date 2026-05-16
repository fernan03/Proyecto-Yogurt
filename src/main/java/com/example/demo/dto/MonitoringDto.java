package com.example.demo.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Data;


import io.swagger.v3.oas.annotations.media.Schema;

public class MonitoringDto {

    @Data
    @Builder
    @Schema(name = "TemperatureSummary", description = "Resumen de estadísticas de temperatura de un lote")
    public static class TemperatureSummary {

        @Schema(description = "Temperatura actual del lote en °C", example = "42.5")
        private Double currentTemperature;

        @Schema(description = "Temperatura máxima registrada en °C", example = "45.0")
        private Double maximunTemperature;

        @Schema(description = "Temperatura mínima registrada en °C", example = "38.5")
        private Double minmumTemperature;

        @Schema(description = "Temperatura promedio durante la incubación en °C", example = "42.0")
        private Double averageTemperature;
    }

    @Data
    @Builder
    @Schema(name = "Dashboard", description = "Resumen general del sistema de producción de yogurt")
    public static class Dashboard {

        @Schema(
            description = "Cantidad de lotes por estado",
            example = "{ \"PREPARING\": 2, \"HEATING\": 1, \"COOLING\": 0, \"INCUBATING\": 3, \"REFRIGERATING\": 1, \"COMPLETED\": 5, \"FAILED\": 1 }"
        )
        private Map<String, Long> batchCounts;

        @Schema(description = "Cantidad total de lotes activos", example = "7")
        private Long activeBatchCount;

        @Schema(description = "Cantidad de lotes completados hoy", example = "3")
        private Integer completedToday;
    }
}