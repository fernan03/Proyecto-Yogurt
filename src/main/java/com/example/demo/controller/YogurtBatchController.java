package com.example.demo.controller;

import java.util.List;

import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.TemperatureRecordDto;
import com.example.demo.dto.batchDto;
import com.example.demo.model.YogurtBatch;
import com.example.demo.service.YogurtMakingService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/batches")
@RequiredArgsConstructor
@Tag(name = "Yogurt Batch", description = "Gestion del proceso de produccion de yogurt")
public class YogurtBatchController {

    private final YogurtMakingService yogurtMakingService;

    @PostMapping
    @Operation(summary = "Crear nuevo lote", description = "Inicia un nuevo proceso de produccion de yogurt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Lote creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    public ResponseEntity<YogurtBatch> startNewBatch(@RequestBody batchDto.StarBatchRequest request) {
        YogurtBatch batch = yogurtMakingService.startNewBatch(
            request.getRecipeId(), 
            request.getCustomMilkVolume(), 
            request.getCustomStarterAmonunt()
        );
        return new ResponseEntity<>(batch, HttpStatus.CREATED);
    }

    @PostMapping("/{batchId}/heating")
    @Operation(summary = "Iniciar calentamiento", description = "Inicia la fase de calentamiento del lote")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Calentamiento iniciado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    })
    public ResponseEntity<YogurtBatch> startHeating( 
        @Parameter(description = "ID del lote", example = "1")
        @PathVariable Long batchId){
        YogurtBatch batch = yogurtMakingService.startHeating(batchId);
        return ResponseEntity.ok(batch);
    }

    @PostMapping("/{batchId}/inoculating")
    @Operation(summary = "Iniciar inoculación", description = "Agrega el cultivo al lote")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inoculacion iniciada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    })
    public ResponseEntity<YogurtBatch> startInoculating(@PathVariable Long batchId){
        YogurtBatch batch = yogurtMakingService.startInoculating(batchId);
        return ResponseEntity.ok(batch);
    }

    @PostMapping("/{batchId}/incubation")
    @Operation(summary = "Iniciar incubación", description = "Comienza la fase de incubacion del yougrt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Incubacion iniciada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    })
    public ResponseEntity<YogurtBatch> starIncubation(@PathVariable Long batchId){
        YogurtBatch batch = yogurtMakingService.startIncubation(batchId);
        return ResponseEntity.ok(batch);
    }

    @PostMapping("/{batchId}/refrigeration")
    @Operation(summary = "Iniciar refrigeracion", description = "Enfria el lote para finalizar el proceso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Refrigeracion iniciada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    })
    public ResponseEntity<YogurtBatch> startRefrigeration(@PathVariable Long batchId){
        YogurtBatch batch = yogurtMakingService.startRefrigeration(batchId);
        return ResponseEntity.ok(batch);
    }

    @PostMapping("/{batchId}/complete")
    @Operation(summary = "Completar lote", description = "Marca el lote como finalizado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lote completado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    })
    public ResponseEntity<YogurtBatch> completeBatch(@PathVariable Long batchId){
        YogurtBatch batch = yogurtMakingService.completeBatch(batchId);
        return ResponseEntity.ok(batch);
    }

    @PostMapping("/{batchid}/fail")
    @Operation(summary = "Marcar lote como fallido", description = "Marca el lote como fallido con una razon")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lote marcado como fallido"),
        @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    })
    public ResponseEntity<YogurtBatch> markAsFailed(@PathVariable Long batchId, @RequestBody batchDto.FailRequest request){
        YogurtBatch batch = yogurtMakingService.markAsFailed(batchId, request.getReason());
        return ResponseEntity.ok(batch);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los lotes", description = "Obtiene una lista de todos los lotes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de lotes obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron lotes")
    })
    public ResponseEntity<List<YogurtBatch>> getAllBatches(
        @RequestParam(required = false) YogurtBatch.BatchStatus status){
        if(status != null){
            return ResponseEntity.ok(yogurtMakingService.getBatchesByStatus(status));
        }
        return ResponseEntity.ok(yogurtMakingService.getAllBatches());
    }

    @GetMapping("/{batchId}")
    @Operation(summary = "Obtener lote por ID", description = "Obtiene un lote por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lote obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    })
    public ResponseEntity<YogurtBatch> getBatch(@PathVariable Long batchId){
        YogurtBatch batch = yogurtMakingService.getBatch(batchId);
        return ResponseEntity.ok(batch);
    }

    @PostMapping("/{bathId}/temperature")
    @Operation(summary = "Registrar temperatura", description = "Registra una medicion de temperatura en el lote")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Temperatura registrada"),
        @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    })
    
    public ResponseEntity<Void> recordTemEntity(
        @PathVariable Long batchId,
        @RequestBody TemperatureRecordDto request) {
            yogurtMakingService.recordTemperature(batchId, request.getTemperature(), request.getType());
            return ResponseEntity.ok().build();
        }
}

