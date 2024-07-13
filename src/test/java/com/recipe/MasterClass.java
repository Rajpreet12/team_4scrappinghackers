package com.recipe;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.recipe.database.DatabaseOperations;
import com.recipe.vos.FilterVo;
import com.recipe.vos.RecipeVo;

public class MasterClass {


	public static List<Integer> alreadySaved;
	public static FilterVo filterVo;

	public static final String URL="https://www.tarladalal.com/RecipeAtoZ.aspx";

	@Test (dataProvider = "data-provider")
	public void myLFVTest (String currentSearchTerm) {

		WebDriver driver = new FirefoxDriver();

		System.out.println("searchTerm : "+currentSearchTerm);

		driver.navigate().to(URL+"?beginswith="+currentSearchTerm+"&pageindex=1");

		List<WebElement> allLinks =driver.findElements(By.xpath("//div[contains(.,'Goto Page:')]//a[@class='respglink']"));

		int lastPageIndex = Integer.parseInt(allLinks.get(allLinks.size()-1).getText());

		System.out.println("lastPageIndex ---> "+lastPageIndex);

		String rcpId;
		
		for(int pageNumber=1;pageNumber<=lastPageIndex;pageNumber++)
		{
			try {
				driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(2));

				System.out.println(" ========== Currently Searching -> "+currentSearchTerm +" Page : "+(pageNumber)+" ========== ");

				driver.navigate().to(URL+"?beginswith="+currentSearchTerm+"&pageindex="+pageNumber);

				List<WebElement> allRecipeCards =driver.findElements(By.xpath("//div[@class='rcc_recipecard']"));

				System.out.println("all Recipe cards count : "+allRecipeCards.size());

				int totalRecipes=allRecipeCards.size();

				for (int cardsCounter=0;cardsCounter<totalRecipes;cardsCounter++) 
				{
					allRecipeCards =driver.findElements(By.xpath("//div[@class='rcc_recipecard']"));

					WebElement singleCard =allRecipeCards.get(cardsCounter);

					WebElement nameEle=singleCard.findElement(By.xpath("//div[@id='"+singleCard.getAttribute("id")+"']//span[@class='rcc_recipename']"));

					rcpId=singleCard.getAttribute("id").replaceAll("rcp", "").trim();

					if(alreadySaved.contains(Integer.parseInt(rcpId)))
					{
						System.out.println("Recipe "+rcpId+" Already in db..");
						continue;
					}
					else
					{
						DatabaseOperations.insertCheckedRecipeId(Integer.parseInt(rcpId));
					}

					nameEle.click();

					System.out.println("\n_________________________Checking Recipe# "+rcpId+"_______________________________");

					RecipeVo sinleRecipeOutput = Library.getRecipeDetails(driver);

					sinleRecipeOutput.setRecipe_ID(rcpId);

					if(!Library. isIngPresent(sinleRecipeOutput.getPlainIngredientsList(), filterVo.getLstEliminate()))
					{
						if(Library.isIngPresent(sinleRecipeOutput.getPlainIngredientsList(), filterVo.getLstAdd()))
						{
							if(!Library.isNeedToAvoidThisRecipe(sinleRecipeOutput.getTags(), filterVo.getRecipeToAvoid()))
							{
								System.out.println("Got required recipe/inserted in db !");

								DatabaseOperations.insertRecipe(sinleRecipeOutput,"LFV_Elimination");
							}
							else
							{
								System.out.println("[#3 ignore recipe] This recipe is having avoiding terms !");
							}
						}
						else
						{
							System.out.println("[#2 ignore recipe] This recipe is NOT having any required(add) igredients !");
						}
					}
					else
					{
						System.out.println("[#1 ignore recipe] This recipe is having eleminated ingredient !");
					}


					driver.navigate().to(URL+"?beginswith="+currentSearchTerm+"&pageindex="+pageNumber);
				}

			} catch (Exception e) {

				e.printStackTrace();

				System.out.println(" ========== Error, skipping that recipe Currently Searching -> "+currentSearchTerm +" Page : "+(pageNumber)+" ========== ");
			}
		}
	}
	
	

	@DataProvider (name = "data-provider", parallel = true)
	public Object[][] dpMethod(){

		String search[][]= {{"A"},{"B"},{"C"},{"D"},{"E"},{"F"},{"G"},{"H"},{"I"},{"J"},{"K"},{"L"},{"M"},{"N"},{"O"},{"P"},{"Q"},{"R"},{"S"},{"T"},{"U"},{"V"},{"W"},{"X"},{"Y"},{"Z"},{"Misc"}};

		return search;
	}
}
