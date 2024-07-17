package com.recipecategory;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecipeDetails_Locator {

    private static final Logger logger = LoggerFactory.getLogger(RecipeDetails_Locator.class);

    public static String Recipe_Category_loc;
    public static String Food_Category_loc;
    public static String Cuisine_category_loc;
    public static String Ingredients_loc = "//div[@id='rcpinglist']//span[@itemprop='recipeIngredient']";
    public static String Ingredients_Plain_loc = "//div[@id='rcpinglist']//span[@itemprop='recipeIngredient']//a";
    public static String Preparation_Time_loc = "//time[@itemprop='prepTime']";
    public static String Cooking_Time_loc = "//time[@itemprop='cookTime']";
    public static String Tags_loc = "//div[@id='recipe_tags']//a";
    public static String No_of_servings_loc = "//span[contains(@id,'lblServes')]";
    public static String Recipe_Description_loc = "//p[@id='recipe_description']";
    public static String Preparation_method_loc = "//div[contains(@id,'pnlRcpMethod')]//li";
    public static String Nutrient_values_loc = "//table[@id='rcpnutrients']//tr";
    public static String Receipe_Name = "(//span[@class='rcc_recipename'])[1]";

    private String recipe_Name;
    private List<String> ingredients;
    private List<String> plainIngredientsList;
    private String preparation_Time;
    private String cooking_Time;
    private String recipe_Description;
    private String recipe_URL;
    private List<String> tags;
    private List<String> breadcrumbs;
    private List<String> preparation_Steps;
    private String no_of_servings;
    private List<String> nutrient_values;

    // Setters
    public void setRecipe_Name(String recipe_Name) {
        logger.info("Setting Recipe_Name: {}", recipe_Name);
        this.recipe_Name = recipe_Name;
    }

    public void setIngredients(List<String> ingredients) {
        logger.info("Setting Ingredients: {}", ingredients);
        this.ingredients = ingredients;
    }

    public void setPlainIngredientsList(List<String> plainIngredientsList) {
        logger.info("Setting PlainIngredientsList: {}", plainIngredientsList);
        this.plainIngredientsList = plainIngredientsList;
    }

    public void setPreparation_Time(String preparation_Time) {
        logger.info("Setting Preparation_Time: {}", preparation_Time);
        this.preparation_Time = preparation_Time;
    }

    public void setCooking_Time(String cooking_Time) {
        logger.info("Setting Cooking_Time: {}", cooking_Time);
        this.cooking_Time = cooking_Time;
    }

    public void setRecipe_Description(String recipe_Description) {
        logger.info("Setting Recipe_Description: {}", recipe_Description);
        this.recipe_Description = recipe_Description;
    }

    public void setRecipe_URL(String recipe_URL) {
        logger.info("Setting Recipe_URL: {}", recipe_URL);
        this.recipe_URL = recipe_URL;
    }

    public void setTags(List<String> tags) {
        logger.info("Setting Tags: {}", tags);
        this.tags = tags;
    }

    public void setBreadcrumbs(List<String> breadcrumbs) {
        logger.info("Setting Breadcrumbs: {}", breadcrumbs);
        this.breadcrumbs = breadcrumbs;
    }

    public void setPreparation_Steps(List<String> preparation_Steps) {
        logger.info("Setting Preparation_Steps: {}", preparation_Steps);
        this.preparation_Steps = preparation_Steps;
    }

    public void setNo_of_servings(String no_of_servings) {
        logger.info("Setting No_of_servings: {}", no_of_servings);
        this.no_of_servings = no_of_servings;
    }

    public void setNutrient_values(List<String> nutrient_values) {
        logger.info("Setting Nutrient_values: {}", nutrient_values);
        this.nutrient_values = nutrient_values;
    }

    // Getters
    public String getRecipe_Name() {
        return recipe_Name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getPlainIngredientsList() {
        return plainIngredientsList;
    }

    public String getPreparation_Time() {
        return preparation_Time;
    }

    public String getCooking_Time() {
        return cooking_Time;
    }

    public String getRecipe_Description() {
        return recipe_Description;
    }

    public String getRecipe_URL() {
        return recipe_URL;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getBreadcrumbs() {
        return breadcrumbs;
    }

    public List<String> getPreparation_Steps() {
        return preparation_Steps;
    }

    public String getNo_of_servings() {
        return no_of_servings;
    }

    public List<String> getNutrient_values() {
        return nutrient_values;
    }
}
