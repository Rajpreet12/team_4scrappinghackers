package test.hackathon;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Library {
	/*----------------------------- Single Recipe --------------------------------------------*/
	public static RecipeVo getRecipeDetails(WebDriver driver) {

		RecipeVo sinleRecipeOutput=new RecipeVo();

		sinleRecipeOutput.setRecipe_Name(driver.findElement(By.xpath(RecipeDetailsLocators.Receipe_Name)).getText());

		List<WebElement> allIngr= driver.findElements(By.xpath(RecipeDetailsLocators.Ingredients_loc));

		sinleRecipeOutput.setIngredients(allIngr.stream().map(webElement -> webElement.getText()).collect(Collectors.toList()));
		
		List<WebElement> allIngrPln= driver.findElements(By.xpath(RecipeDetailsLocators.Ingredients_Plain_loc));

		sinleRecipeOutput.setPlainIngredientsList(allIngrPln.stream().map(webElement -> webElement.getText()).collect(Collectors.toList()));

		sinleRecipeOutput.setPreparation_Time(driver.findElement(By.xpath(RecipeDetailsLocators.Preparation_Time_loc)).getText());
		sinleRecipeOutput.setCooking_Time(driver.findElement(By.xpath(RecipeDetailsLocators.Cooking_Time_loc)).getText());
		sinleRecipeOutput.setRecipe_Description(driver.findElement(By.xpath(RecipeDetailsLocators.Recipe_Description_loc)).getText());
		sinleRecipeOutput.setRecipe_URL(driver.getCurrentUrl());

		List<WebElement> allBCs=driver.findElements(By.xpath("//div[@id='show_breadcrumb']/div//span"));

		sinleRecipeOutput.setBreadcrumbs(allBCs.stream().map(webElement -> webElement.getText()).collect(Collectors.toList()));

		List<WebElement> alltags= driver.findElements(By.xpath(RecipeDetailsLocators.Tags_loc));

		sinleRecipeOutput.setTags(alltags.stream().map(webElement -> webElement.getText()).collect(Collectors.toList()));

		List<WebElement> allSteps= driver.findElements(By.xpath(RecipeDetailsLocators.Preparation_method_loc));
		sinleRecipeOutput.setPreparation_method(allSteps.stream().map(webElement -> webElement.getText()).collect(Collectors.toList()));

		sinleRecipeOutput.setNo_of_servings(driver.findElement(By.xpath(RecipeDetailsLocators.No_of_servings_loc)).getText());

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

		List<WebElement>  tableOfNutRows = driver.findElements(By.xpath(RecipeDetailsLocators.Nutrient_values_loc));
		sinleRecipeOutput.setNutrient_values(tableOfNutRows.stream().map(webElement -> webElement.getText()).collect(Collectors.toList()));

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		return sinleRecipeOutput;
	}

}
