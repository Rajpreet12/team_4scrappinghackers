package com.LFV;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recipecategory.ReceipeCategory;
import com.recipecategory.RecipeDetails_Locator;

public class Library {
    private static final Logger logger = LoggerFactory.getLogger(Library.class);

    /*----------------------------- Single Recipe --------------------------------------------*/
    public static ReceipeCategory getRecipeDetails(WebDriver driver) {
        ReceipeCategory sinleRecipeOutput = new ReceipeCategory();
        sinleRecipeOutput.setRecipe_Name(driver.findElement(By.xpath(ReceipeCategory.getRecipe_Name())).getText());
        List<WebElement> allIngr = driver.findElements(By.xpath(RecipeDetails_Locator.Ingredients_loc));
        sinleRecipeOutput.setIngredients(allIngr.stream().map(WebElement::getText).collect(Collectors.toList()));
        List<WebElement> allIngrPln = driver.findElements(By.xpath(RecipeDetails_Locator.Ingredients_Plain_loc));
        sinleRecipeOutput.setPlainIngredientsList(allIngrPln.stream().map(WebElement::getText).collect(Collectors.toList()));
        sinleRecipeOutput.setPreparation_Time(driver.findElement(By.xpath(RecipeDetails_Locator.Preparation_Time_loc)).getText());
        sinleRecipeOutput.setCooking_Time(driver.findElement(By.xpath(RecipeDetails_Locator.Cooking_Time_loc)).getText());
        sinleRecipeOutput.setRecipe_Description(driver.findElement(By.xpath(RecipeDetails_Locator.Recipe_Description_loc)).getText());
        sinleRecipeOutput.setRecipe_URL(driver.getCurrentUrl());
        List<WebElement> alltags = driver.findElements(By.xpath(RecipeDetails_Locator.Tags_loc));
        sinleRecipeOutput.setTags(alltags.stream().map(WebElement::getText).collect(Collectors.toList()));
        List<WebElement> allBCs = driver.findElements(By.xpath("//div[@id='show_breadcrumb']/div//span"));
        sinleRecipeOutput.setBreadcrumbs(allBCs.stream().map(WebElement::getText).collect(Collectors.toList()));
        logger.debug("Cuisine Category ------>  {}", sinleRecipeOutput.getCuisine_category());
        List<WebElement> allSteps = driver.findElements(By.xpath(RecipeDetails_Locator.Preparation_method_loc));
        sinleRecipeOutput.setPreparation_method(allSteps.stream().map(WebElement::getText).collect(Collectors.toList()));
        sinleRecipeOutput.setNo_of_servings(driver.findElement(By.xpath(RecipeDetails_Locator.No_of_servings_loc)).getText());
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        List<WebElement> tableOfNutRows = driver.findElements(By.xpath(RecipeDetails_Locator.Nutrient_values_loc));
        sinleRecipeOutput.setNutrient_values(tableOfNutRows.stream().map(WebElement::getText).collect(Collectors.toList()));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        return sinleRecipeOutput;
    }

    public static boolean isNeedToAvoidThisRecipe(List<String> allTags, List<String> lFV_Avoid) {
        for (String singleTag : allTags) {
            for (String avoidItem : lFV_Avoid) {
                if (singleTag.toLowerCase().contains(avoidItem.toLowerCase())) {
                    logger.debug("Need to avoid this recipe.... got avoiding term {}", avoidItem);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isIngPresent(List<String> inputRecIngr, List<String> criteriaList) {
        for (String singleIng : inputRecIngr) {
            for (String eleIng : criteriaList) {
                if (singleIng.trim().equalsIgnoreCase(eleIng.trim()) ||
                    singleIng.trim().equalsIgnoreCase(eleIng.trim() + "s") ||
                    singleIng.trim().equalsIgnoreCase(eleIng.trim() + "es") ||
                    Arrays.asList(singleIng.trim().split(" ")).contains(eleIng.toLowerCase().trim()) ||
                    Arrays.asList(singleIng.trim().split(" ")).contains((eleIng + "s").toLowerCase().trim()) ||
                    Arrays.asList(singleIng.trim().split(" ")).contains((eleIng + "es").toLowerCase().trim())) {
                    logger.debug("[match] found  {} ... in {}", eleIng, singleIng);
                    return true;
                }
            }
        }
        return false;
    }
}
