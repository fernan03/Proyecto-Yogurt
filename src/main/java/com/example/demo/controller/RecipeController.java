package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Recipe;
import com.example.demo.service.RecipeService;
import com.example.demo.dto.RecipeDto;

import lombok.RequiredArgsConstructor;


import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@Tag(name = "Recipes", description = "Gestión de recetas de yogurt")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    @Operation(summary = "Crear receta", description = "Crea una nueva receta de yogurt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Receta creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Recipe> createRecipe(@RequestBody RecipeDto recipeDto){
        Recipe recipe = recipeService.createRecipe(recipeDto);
        return new ResponseEntity<>(recipe, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar receta", description = "Actualiza una receta existente por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receta actualizada"),
        @ApiResponse(responseCode = "404", description = "Receta no encontrada")
    })
    public ResponseEntity<Recipe> updateRecipe(
        @Parameter(description = "ID de la receta", example = "1")
        @PathVariable Long id, 
        @RequestBody RecipeDto recipeDto){
        Recipe recipe = recipeService.updateRecipe(id, recipeDto);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener receta", description = "Obtiene una receta por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receta encontrada"),
        @ApiResponse(responseCode = "404", description = "Receta no encontrada")
    })
    public ResponseEntity<Recipe> getRecipe(
        @Parameter(description = "ID de la receta", example = "1")
        @PathVariable Long id){
        Recipe recipe = recipeService.getRecipe(id);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping
    @Operation(summary = "Listar recetas activas", description = "Obtiene todas las recetas activas")
    @ApiResponse(responseCode = "200", description = "Lista obtenida")
    public ResponseEntity<List<Recipe>> getAllRecipes(){
        return ResponseEntity.ok(recipeService.getAllActiveRecipes());
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar recetas", description = "Busca recetas por palabra clave")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultados encontrados"),
        @ApiResponse(responseCode = "400", description = "Parámetro inválido")
    })
    public ResponseEntity<List<Recipe>> searchRecipe(
        @Parameter(description = "Palabra clave de búsqueda", example = "natural")
        @RequestParam String keyword){
        return ResponseEntity.ok(recipeService.searchRecipes(keyword));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Desactivar receta", description = "Desactiva una receta (soft delete)")
    @ApiResponse(responseCode = "200", description = "Receta desactivada")
    public ResponseEntity<Void> deactivateRecipe(
        @Parameter(description = "ID de la receta", example = "1")
        @PathVariable Long id){
        recipeService.deactivateRecipe(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activar receta", description = "Activa una receta previamente desactivada")
    @ApiResponse(responseCode = "200", description = "Receta activada")
    public ResponseEntity<Void> activateRecipe(
        @Parameter(description = "ID de la receta", example = "1")
        @PathVariable Long id){
        recipeService.activateRecipe(id);
        return ResponseEntity.ok().build();
    }
}