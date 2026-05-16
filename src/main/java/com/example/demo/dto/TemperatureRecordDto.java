package com.example.demo.dto;

import com.example.demo.model.TemperatureLog;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "TemperatureRecord", description = "Registro de temperatura para un lote de yogurt")
public class TemperatureRecordDto {

    @Schema(description = "Temperatura registrada en °C", example = "42.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double temperature;

    @Schema(
        description = "Tipo de registro de temperatura",
        example = "INCUBATION",
        allowableValues = {"HEATING", "COOLING", "INCUBATION", "REFRIGERATION", "MANUAL"}
    )
    private TemperatureLog.LogType type;
}