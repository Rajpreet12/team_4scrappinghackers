package com.recipe;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.hooks.Hooks;
import com.recipe.vos.RecipeDetailsLocatorsVo;
import com.recipe.vos.RecipeVo;

public class Library {
	/*----------------------------- Single Recipe --------------------------------------------*/
	public static RecipeVo getRecipeDetails(WebDriver driver) {

		RecipeVo sinleRecipeOutput=new RecipeVo();

		sinleRecipeOutput.setRecipe_Name(driver.findElement(By.xpath(RecipeDetailsLocatorsVo.Receipe_Name)).getText());

		List<WebElement> allIngr= driver.findElements(By.xpath(RecipeDetailsLocatorsVo.Ingredients_loc));

		sinleRecipeOutput.setIngredients(allIngr.stream().map(webElement -> webElement.getText()).collect(Collectors.toList()));

		List<WebElement> allIngrPln= driver.findElements(By.xpath(RecipeDetailsLocatorsVo.Ingredients_Plain_loc));

		sinleRecipeOutput.setPlainIngredientsList(allIngrPln.stream().map(webElement -> webElement.getText()).collect(Collectors.toList()));

		sinleRecipeOutput.setPreparation_Time(driver.findElement(By.xpath(RecipeDetailsLocatorsVo.Preparation_Time_loc)).getText());
		sinleRecipeOutput.setCooking_Time(driver.findElement(By.xpath(RecipeDetailsLocatorsVo.Cooking_Time_loc)).getText());
		sinleRecipeOutput.setRecipe_Description(driver.findElement(By.xpath(RecipeDetailsLocatorsVo.Recipe_Description_loc)).getText());
		sinleRecipeOutput.setRecipe_URL(driver.getCurrentUrl());

		List<WebElement> alltags= driver.findElements(By.xpath(RecipeDetailsLocatorsVo.Tags_loc));

		sinleRecipeOutput.setTags(alltags.stream().map(webElement -> webElement.getText()).collect(Collectors.toList()));

		List<WebElement> allBCs=driver.findElements(By.xpath("//div[@id='show_breadcrumb']/div//span"));

		sinleRecipeOutput.setBreadcrumbs(allBCs.stream().map(webElement -> webElement.getText()).collect(Collectors.toList()));

		System.out.println("Cuisine Category ------>  "+sinleRecipeOutput.getCuisine_category());

		List<WebElement> allSteps= driver.findElements(By.xpath(RecipeDetailsLocatorsVo.Preparation_method_loc));
		sinleRecipeOutput.setPreparation_method(allSteps.stream().map(webElement -> webElement.getText()).collect(Collectors.toList()));

		sinleRecipeOutput.setNo_of_servings(driver.findElement(By.xpath(RecipeDetailsLocatorsVo.No_of_servings_loc)).getText());

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

		List<WebElement>  tableOfNutRows = driver.findElements(By.xpath(RecipeDetailsLocatorsVo.Nutrient_values_loc));
		sinleRecipeOutput.setNutrient_values(tableOfNutRows.stream().map(webElement -> webElement.getText()).collect(Collectors.toList()));

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		return sinleRecipeOutput;
	}

	public static boolean isNeedToAvoidThisRecipe(List<String> allTags,List<String> lFV_Avoid) {


		for (String singleTag : allTags) {

			for (String avoidItem : lFV_Avoid) {

				if(singleTag.toLowerCase().contains(avoidItem.toLowerCase()))
				{
					System.out.println("need to avoid this receipe.... got avoiding term "+avoidItem);
					return true;
				}
			}
		}

		return false;

	}

	public static boolean isIngPresent(List<String> inputRecIngr, List<String> criteriaList,String forLog) {

		for (String singleIng : inputRecIngr) {

			singleIng=singleIng.toLowerCase();
			singleIng= singleIng.replaceAll("[^a-zA-Z0-9 ]", " ").replaceAll(" +", " ");  

			for (String eleIng : criteriaList) {


				if(singleIng.trim().equalsIgnoreCase(eleIng.trim())||
						Arrays.asList(singleIng.trim().split(" ")).contains(eleIng.toLowerCase().trim())||
						Arrays.asList(singleIng.trim().split(" ")).contains((eleIng+"s").toLowerCase().trim())||
						Arrays.asList(singleIng.trim().split(" ")).contains((eleIng+"es").toLowerCase().trim()))
				{

					String toIgnore= Hooks.ExceptionIngredientMapping.get(eleIng);
					if(toIgnore!=null)
					{
						if(!singleIng.trim().toLowerCase().contains(toIgnore))
						{
							System.out.println("[match] found  "+eleIng+ "... in "+singleIng  +" for "+forLog);

							return true;
						}
						
					}
					else
					{
						System.out.println("[match] found  "+eleIng+ "... in "+singleIng  +" for "+forLog);
						return true;
					}
				}

				//olive oil - cri
				//extra virgin olive oil   = ing
				if(eleIng.contains(" ") && singleIng.contains(eleIng.toLowerCase()) )
				{
					System.out.println("[match] found  "+eleIng+ "... in "+singleIng  +" for "+forLog);


					String toIgnore= Hooks.ExceptionIngredientMapping.get(eleIng);
					if(toIgnore!=null)
					{
						if(!singleIng.trim().toLowerCase().contains(toIgnore))
						{
							System.out.println("[match] found  "+eleIng+ "... in "+singleIng  +" for "+forLog);
							return true;
						}
					}
					else
					{
						System.out.println("[match] found  "+eleIng+ "... in "+singleIng  +" for "+forLog);
						return true;
					}
				}
			}
		}

		return false;
	}

}
