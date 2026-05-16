package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.TemperatureLog;
import com.example.demo.model.YogurtBatch;
import com.example.demo.repository.TemperatureLogRepository;
import com.example.demo.repository.YogurtBatchRepository;
import com.example.demo.service.TemperatureControlService;
import com.example.demo.dto.MonitoringDto;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
@Tag(name = "Monitoring", description = "Monitoreo y métricas del proceso de yogurt")
public class MonitoringController {

    private final YogurtBatchRepository batchRepository;
    private final TemperatureLogRepository temperatureLogRepository;
    private final TemperatureControlService temperatureControlService;

    @GetMapping("/batches/active")
    @Operation(summary = "Obtener lotes activos", description = "Lista todos los lotes en proceso (no finalizados)")
    @ApiResponse(responseCode = "200", description = "Lista de lotes activos obtenida")
    public ResponseEntity<List<YogurtBatch>> getActiveBatches(){
        List<YogurtBatch> activeBatches = batchRepository.findByStatus(YogurtBatch.BatchStatus.INCUBATING);
        activeBatches.addAll(batchRepository.findByStatus(YogurtBatch.BatchStatus.HEATING));
        activeBatches.addAll(batchRepository.findByStatus(YogurtBatch.BatchStatus.COOLING));
        activeBatches.addAll(batchRepository.findByStatus(YogurtBatch.BatchStatus.REFRIGERATING));
        return ResponseEntity.ok(activeBatches);
    }

    @GetMapping("/batches/{batchId}/temperature")
    @Operation(summary = "Resumen de temperatura", description = "Obtiene estadísticas de temperatura de un lote")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resumen obtenido"),
        @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    })
    public ResponseEntity<MonitoringDto.TemperatureSummary> getBatchTemperatureSummary(
        @Parameter(description = "ID del lote", example = "1")
        @PathVariable Long batchId){

        Double currentTemp = temperatureControlService.getCurrentTemperature(batchId);
        Double maxTemp = temperatureLogRepository.getMaxTemperatureByBatch(batchId);
        Double minTemp = temperatureLogRepository.getMinTemperatureByBatch(batchId);
        Double avgTemp = temperatureLogRepository.getAverageTemperatureByBatchAndType(
            batchId, TemperatureLog.LogType.INCUBATION);

        MonitoringDto.TemperatureSummary summary = MonitoringDto.TemperatureSummary.builder()
            .currentTemperature(currentTemp)
            .maximunTemperature(maxTemp)
            .minmumTemperature(minTemp)
            .averageTemperature(avgTemp)
            .build();

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/batches/{batchId}/temperature-logs")
    @Operation(
        summary = "Historial de temperaturas",
        description = "Obtiene registros de temperatura de un lote, opcionalmente filtrados por rango de fechas"
    )
    @ApiResponse(responseCode = "200", description = "Lista de registros obtenida")
    public ResponseEntity<List<TemperatureLog>> getTemperatureLogs(
        @Parameter(description = "ID del lote", example = "1")
        @PathVariable Long batchId,

        @Parameter(description = "Fecha inicio (ISO 8601)", example = "2026-04-27T10:00:00")
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,

        @Parameter(description = "Fecha fin (ISO 8601)", example = "2026-04-27T12:00:00")
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){

        if(start != null && end != null){
            return ResponseEntity.ok(
                temperatureLogRepository.findByBatchAndTimeRange(batchId, start, end)
            );
        }

        YogurtBatch batch = batchRepository.findById(batchId).orElseThrow();
        return ResponseEntity.ok(temperatureLogRepository.findByBatch(batch));
    }

    @GetMapping("/dashboard")
    @Operation(
        summary = "Dashboard general",
        description = "Obtiene métricas generales del sistema (conteos, activos, completados hoy)"
    )
    @ApiResponse(responseCode = "200", description = "Dashboard obtenido")
    public ResponseEntity<MonitoringDto.Dashboard> getDashboard(){

        long preparingCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.PREPARING);
        long heatingCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.HEATING);
        long coolingCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.COOLING);
        long incubatingCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.INCUBATING);
        long refrigeratingCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.REFRIGERATING);
        long completedCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.COMPLETED);
        long failedCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.FAILED);

        Map<String, Long> batchCounts = new HashMap<>();
        batchCounts.put("PREPARING", preparingCount);
        batchCounts.put("HEATING", heatingCount);
        batchCounts.put("COOLING", coolingCount);
        batchCounts.put("INCUBATING", incubatingCount);
        batchCounts.put("REFRIGERATING", refrigeratingCount);
        batchCounts.put("COMPLETED", completedCount);
        batchCounts.put("FAILED", failedCount);

        MonitoringDto.Dashboard dashboard = MonitoringDto.Dashboard.builder()
            .batchCounts(batchCounts)
            .activeBatchCount(preparingCount + heatingCount + coolingCount + incubatingCount + refrigeratingCount)
            .completedToday(batchRepository.findByStatusAndDateRange(
                YogurtBatch.BatchStatus.COMPLETED,
                LocalDateTime.now().withHour(0).withMinute(0),
                LocalDateTime.now()).size())
            .build();

        return ResponseEntity.ok(dashboard);
    }
}