package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Ingredient;
import com.example.demo.model.Recipe;
import com.example.demo.repository.RecipeRepository;
import com.example.demo.dto.RecipeDto;

import com.example.demo.exception.BusinessException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Transactional
    public Recipe createRecipe(RecipeDto recipeDto) {
        if (recipeRepository.findByName(recipeDto.getName()).isPresent()){
            throw new BusinessException("Recipe with name '" + recipeDto.getName() + "' already exists");
        }

        Recipe recipe = Recipe.builder()
            .name(recipeDto.getName())
            .description(recipeDto.getDescription())
            .defaultMilkVolume(recipeDto.getDefaulMilkVolume())
            .defaultStarterAmount(recipeDto.getDefaulStarterAmount())
            .heatingTemperature(recipeDto.getHeatingTemperature())
            .heatingDuration(recipeDto.getHeatingDuration())
            .inoculationTemperature(recipeDto.getInnoculationTemperature())
            .incubationTemperature(recipeDto.getIncubationTemperature())
            .minIncubationTime(recipeDto.getMinIncubationTime())
            .maxIncubationTime(recipeDto.getMaxIncubationTime())
            .refrigerationTime(recipeDto.getRefrigerationTime())
            .difficulty(recipeDto.getDifficulty())
            .tips(recipeDto.getTips())
            .active(true)
            .build();

        if(recipeDto.getIngredients() != null){
            recipeDto.getIngredients().forEach(ingredientDto -> {
                Ingredient ingredient = Ingredient.builder()
                    .name(ingredientDto.getName())
                    .quantity(ingredientDto.getQuantity())
                    .unit(ingredientDto.getUnit())
                    .notes(ingredientDto.getNotes())
                    .optional(ingredientDto.getOptions())
                    .recipe(recipe)
                    .build();

                recipe.getIngredients().add(ingredient);
            });
        }

        Recipe savRecipe = recipeRepository.save(recipe);
        log.info("Recipe created: {}", savRecipe.getName());

        return savRecipe;

    }

    @Transactional
    public Recipe updateRecipe(Long id, RecipeDto recipeDto) {
        Recipe recipe = getRecipe(id);

        recipe.setName(recipeDto.getName());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setDefaultMilkVolume(recipeDto.getDefaulMilkVolume());
        recipe.setDefaultStarterAmount(recipeDto.getDefaulStarterAmount());
        recipe.setHeatingTemperature(recipeDto.getHeatingTemperature());
        recipe.setHeatingDuration(recipeDto.getHeatingDuration());
        recipe.setInoculationTemperature(recipeDto.getInnoculationTemperature());
        recipe.setIncubationTemperature(recipeDto.getIncubationTemperature());
        recipe.setMinIncubationTime(recipeDto.getMinIncubationTime());
        recipe.setMaxIncubationTime(recipeDto.getMaxIncubationTime());
        recipe.setRefrigerationTime(recipeDto.getRefrigerationTime());
        recipe.setDifficulty(recipeDto.getDifficulty());
        recipe.setTips(recipeDto.getTips());

        recipe.getIngredients().clear();
        if (recipeDto.getIngredients() != null) {
            recipeDto.getIngredients().forEach(ingredientDto -> {
                Ingredient ingredient = Ingredient.builder()
                    .name(ingredientDto.getName())
                    .quantity(ingredientDto.getQuantity())
                    .unit(ingredientDto.getUnit())
                    .notes(ingredientDto.getNotes())
                    .optional(ingredientDto.getOptions())
                    .recipe(recipe)
                    .build();

                recipe.getIngredients().add(ingredient);
            });

        }

        Recipe updaRecipe = recipeRepository.save(recipe);
        log.info("Recipe updated: {}", updaRecipe.getName());

        return updaRecipe;
    }

    public Recipe getRecipe(Long id) {
        return recipeRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Recipe not found by this id: " + id));
    }

    public List<Recipe> getAllActiveRecipes() {
        return recipeRepository.findByActive(true);
    }

    public List<Recipe> searchRecipes(String Keyword){
        return recipeRepository.searchByKeyword(Keyword);
    }

    @Transactional
    public void deactivateRecipe(Long id){
        Recipe recipe = getRecipe(id);
        recipe.setActive(false);
        recipeRepository.save(recipe);
        log.info("Recipe deactivated: {}", recipe.getName());
    }

    @Transactional
    public void activateRecipe(Long id){
        Recipe recipe = getRecipe(id);
        recipe.setActive(true);
        recipeRepository.save(recipe);
        log.info("Recipe activated: {}", recipe.getName());
    }
}
