package com.LFV;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Hook.Hook;
import Database.DatabaseOperations;
import com.LFV.FilterVo;
import com.recipecategory.ReceipeCategory;
import com.recipecategory.RecipeDetails_Locator;

public class MasterClass {

    private static final Logger logger = LoggerFactory.getLogger(MasterClass.class);

    public static List<Integer> alreadySaved;
    public static FilterVo filterVo;

    public static final String URL = "https://www.tarladalal.com/RecipeAtoZ.aspx";

    @Test(dataProvider = "data-provider")
    public void myLFVTest(String currentSearchTerm) {
        WebDriver driver = null;

        try {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            if (!Hook.configreader1.getBrowserMode().isEmpty())
                firefoxOptions.addArguments(Hook.configreader1.getBrowserMode());
            driver = new FirefoxDriver(firefoxOptions);

            logger.info("Launching browser--> searchTerm --> {}", currentSearchTerm);

            driver.navigate().to(URL + "?beginswith=" + currentSearchTerm + "&pageindex=1");

            List<WebElement> allLinks = driver.findElements(By.xpath("//div[contains(.,'Goto Page:')]//a[@class='respglink']"));

            int lastPageIndex = 0;
            try {
                lastPageIndex = Integer.parseInt(allLinks.get(allLinks.size() - 1).getText());
            } catch (Exception e) {
                logger.info("lastPageIndex is available");
            }

            logger.info("searchTerm : {}, lastPageIndex ---> {}", currentSearchTerm, lastPageIndex);

            String recipeId;

            for (int pageNumber = 1; pageNumber <= lastPageIndex; pageNumber++) {
                try {
                    driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(2));

                    driver.navigate().to(URL + "?beginswith=" + currentSearchTerm + "&pageindex=" + pageNumber);

                    List<WebElement> allRecipeCards = driver.findElements(By.xpath("//div[@class='rcc_recipecard']"));

                    logger.info("========== Currently Searching -> {} Page : {} Cards Count : {}  ==========",
                            currentSearchTerm, pageNumber, allRecipeCards.size());

                    for (int cardsCounter = 0; cardsCounter < allRecipeCards.size(); cardsCounter++) {
                        allRecipeCards = driver.findElements(By.xpath("//div[@class='rcc_recipecard']"));

                        WebElement singleCard = allRecipeCards.get(cardsCounter);

                        WebElement recipeNameElement = singleCard.findElement(By.xpath("//div[@id='" + singleCard.getAttribute("id") + "']//span[@class='rcc_recipename']"));

                        recipeId = singleCard.getAttribute("id").replaceAll("rcp", "").trim();

                        if (alreadySaved.contains(Integer.parseInt(recipeId))) {
                            logger.info("Recipe {} Already in db..", recipeId);
                            continue;
                        } else {
                            DatabaseOperations.insertCheckedRecipeId(Integer.parseInt(recipeId), filterVo.getFilterName());
                        }

                        // Open card, open recipe
                        recipeNameElement.click();

                        logger.info("\n_________________________Checking Recipe# {}_______________________________", recipeId);

                        ReceipeCategory sinleRecipeOutput = Library.getRecipeDetails(driver);

                        sinleRecipeOutput.setRecipe_ID(recipeId);

                        // Step 1 - Check if recipe is NOT having eliminated ingredients
                        if (!Library.isIngPresent(sinleRecipeOutput.getPlainIngredientsList(), filterVo.getLstEliminate())) {
                            // Step 2 - Check if recipe IS having 'add' ingredients
                            if (Library.isIngPresent(sinleRecipeOutput.getPlainIngredientsList(), filterVo.getLstAdd())) {
                                // Step 3 - Check if recipe is NOT having avoiding terms
                                if (!Library.isNeedToAvoidThisRecipe(sinleRecipeOutput.getTags(), filterVo.getRecipeToAvoid())) {
                                    logger.info("Got required recipe/inserting in db !");
                                    // Step 4 - Insert required recipe in Elimination table
                                    DatabaseOperations.insertRecipe(sinleRecipeOutput, filterVo.getFilterName() + "_Elimination");

                                    // Step 5 - Get list(set) of matching allergic ingredients from recipe ingredients
                                    Set<String> matchingAllergicIngredients = getMatchingAllergiIngredients(sinleRecipeOutput.getPlainIngredientsList());

                                    // Step 6 - 'for each' allergic ingredient found, insert into respective allergy table
                                    if (!matchingAllergicIngredients.isEmpty()) {
                                        logger.info("Got matching allergic ingredients..{}", matchingAllergicIngredients);
                                        for (String singleAllergicIngredient : matchingAllergicIngredients) {
                                            logger.info("Inserting into {}_Allergy_{}", filterVo.getFilterName(), singleAllergicIngredient.replaceAll(" ", "_"));
                                            DatabaseOperations.insertRecipe(sinleRecipeOutput, filterVo.getFilterName() + "_Allergy_" + singleAllergicIngredient.replaceAll(" ", "_"));
                                        }
                                    } else {
                                        logger.info("This recipe is not having any allergic ingredients !");
                                    }
                                } else {
                                    logger.info("[#3 ignore recipe] This recipe is having avoiding terms !");
                                }
                            } else {
                                logger.info("[#2 ignore recipe] This recipe is NOT having any required(add) ingredients !");
                            }
                        } else {
                            if (!filterVo.getTo_Add_If_notFullyVgean().isEmpty()) {
                                // Step 7
                                // Prepare new elimination criteria ~ regular elimination minus "to add"
                                List<String> toAddEliminationNew = new ArrayList<>();
                                toAddEliminationNew.addAll(filterVo.getLstEliminate());
                                toAddEliminationNew.removeAll(filterVo.getTo_Add_If_notFullyVgean());

                                // Recipe is NOT having new elimination criteria
                                if (!Library.isIngPresent(sinleRecipeOutput.getPlainIngredientsList(), toAddEliminationNew)) {
                                    // Recipe is having "to_add" ingredients
                                    if (Library.isIngPresent(sinleRecipeOutput.getPlainIngredientsList(), filterVo.getTo_Add_If_notFullyVgean()) &&
                                            !Library.isNeedToAvoidThisRecipe(sinleRecipeOutput.getTags(), filterVo.getRecipeToAvoid())) {
                                        // Step 8 - If to_add ingredients present, insert into *_to_add table
                                        DatabaseOperations.insertRecipe(sinleRecipeOutput, filterVo.getFilterName() + "_to_add");
                                    }
                                }
                            }
                        }

                        driver.navigate().to(URL + "?beginswith=" + currentSearchTerm + "&pageindex=" + pageNumber);
                    }

                } catch (Exception e) {
                    logger.error("Error, skipping that recipe Currently Searching -> {} Page : {}", currentSearchTerm, pageNumber, e);
                }
            }
        } finally {
            if (driver != null)
                driver.quit();
        }
    }

    private Set<String> getMatchingAllergiIngredients(List<String> plainIngredientsList) {
        Set<String> allergicIngr = new HashSet<>();
        for (String plainIngr : plainIngredientsList) {
            for (String algIng : Hook.allergies) {
                if (algIng.trim().equalsIgnoreCase(plainIngr.trim()) ||
                        (plainIngr.trim()).equalsIgnoreCase(algIng.trim() + "s") || (plainIngr.trim()).equalsIgnoreCase(algIng.trim() + "es") ||
                        Arrays.asList(plainIngr.trim().split(" ")).contains(algIng.toLowerCase().trim()) ||
                        Arrays.asList(plainIngr.trim().split(" ")).contains((algIng + "s").toLowerCase().trim()) ||
                        Arrays.asList(plainIngr.trim().split(" ")).contains((algIng + "es").toLowerCase().trim())) {
                    allergicIngr.add(algIng);
                }
            }
        }
        return allergicIngr;
    }

    @DataProvider(name = "data-provider", parallel = true)
    public Object[][] dpMethod() {
        String search[][] = {{"A"}, {"B"}, {"C"}, {"D"}, {"E"}, {"F"}, {"G"}, {"H"}, {"I"}, {"J"}, {"K"}, {"L"}, {"M"}, {"N"}, {"O"}, {"P"}, {"Q"}, {"R"}, {"S"}, {"T"}, {"U"}, {"V"}, {"W"}, {"X"}, {"Y"}, {"Z"}, {"Misc"}};
        return search;
    }
}
