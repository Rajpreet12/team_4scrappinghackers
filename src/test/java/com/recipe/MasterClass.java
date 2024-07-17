package com.recipe;

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

import com.hooks.Hooks;
import com.recipe.database.DatabaseOperations;
import com.recipe.vos.FilterVo;
import com.recipe.vos.RecipeVo;

public class MasterClass {

	public static List<Integer> alreadySaved;
	public static FilterVo filterVo;

	public static final String URL="https://www.tarladalal.com/RecipeAtoZ.aspx";

	@Test (dataProvider = "data-provider")
	public void myLFVTest (String currentSearchTerm) 
	{
		WebDriver driver=null;

		try
		{

			FirefoxOptions firefoxOptions=new FirefoxOptions();
			if(!Hooks.configreader.getBrowserMode().isEmpty())
				firefoxOptions.addArguments(Hooks.configreader.getBrowserMode());
			driver= new FirefoxDriver(firefoxOptions);

			System.out.println("Launching browser--> searchTerm --> "+currentSearchTerm);

			driver.navigate().to(URL+"?beginswith="+currentSearchTerm+"&pageindex=1");

			List<WebElement> allLinks =driver.findElements(By.xpath("//div[contains(.,'Goto Page:')]//a[@class='respglink']"));

			int lastPageIndex=0;
			try {
				lastPageIndex = Integer.parseInt(allLinks.get(allLinks.size()-1).getText());
			} catch (Exception e) {
				System.out.println("lastPageIndex is available");
			}

			System.out.println("searchTerm : "+currentSearchTerm+ ", lastPageIndex ---> "+lastPageIndex);

			String recipeId;

			for(int pageNumber=1;pageNumber<=lastPageIndex;pageNumber++)
			{
				try {
					driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(2));


					driver.navigate().to(URL+"?beginswith="+currentSearchTerm+"&pageindex="+pageNumber);

					List<WebElement> allRecipeCards =driver.findElements(By.xpath("//div[@class='rcc_recipecard']"));

					System.out.println(" ========== Currently Searching -> "+currentSearchTerm +" Page : "+(pageNumber)+ "Cards Count : "+allRecipeCards.size() +"  ========== ");

					for (int cardsCounter=0;cardsCounter<allRecipeCards.size();cardsCounter++) 
					{
						allRecipeCards = driver.findElements(By.xpath("//div[@class='rcc_recipecard']"));

						WebElement singleCard = allRecipeCards.get(cardsCounter);

						WebElement recipeNameElement=singleCard.findElement(By.xpath("//div[@id='"+singleCard.getAttribute("id")+"']//span[@class='rcc_recipename']"));

						recipeId=singleCard.getAttribute("id").replaceAll("rcp", "").trim();

						if(alreadySaved.contains(Integer.parseInt(recipeId)))
						{
							System.out.println("Recipe "+recipeId+" Already in db..");
							continue;
						}
						else
						{
							DatabaseOperations.insertCheckedRecipeId(Integer.parseInt(recipeId),filterVo.getFilterName());
						}

						//open card, open recipe
						recipeNameElement.click();
						
						System.out.println("\n_________________________Checking Recipe# "+recipeId+"_______________________________");

						RecipeVo sinleRecipeOutput = Library.getRecipeDetails(driver);

						sinleRecipeOutput.setRecipe_ID(recipeId);

						//Step 1 - check if recipe is NOT having eliminated ingredients
						if(! Library.isIngPresent(sinleRecipeOutput.getPlainIngredientsList(), filterVo.getLstEliminate()))
						{
							//Step 2 - check if recipe IS having 'add' ingredients
							if(Library.isIngPresent(sinleRecipeOutput.getPlainIngredientsList(), filterVo.getLstAdd()))
							{
								//Step 3 - check if recipe is NOT having avoiding terms
								if(! Library.isNeedToAvoidThisRecipe(sinleRecipeOutput.getTags(), filterVo.getRecipeToAvoid()))
									
									
								{
									/*if (filterVo.getFilterName().equalsIgnoreCase("Lfv") || (filterVo.getFilterName()
									.equalsIgnoreCase("LCHFE"))
									&& (Library.isIngPresent(sinleRecipeOutput.getPlainIngredientsList(),
											filterVo.getketorecipeToAvoid())
											|| (Library.isIngPresent(
													sinleRecipeOutput.getPlainIngredientsList(),
													filterVo.getfoodprocessing())))) {*/
									if(filterVo.getFilterName()=="LFV" || (filterVo.getFilterName()=="LCHFE"
											&& Library.isIngPresent(sinleRecipeOutput.getTags(), filterVo.getfoodprocessing())))
									
								
									System.out.println("Got required recipe/inserting in db !");
									//Step 4 - insert required recipe in Elimination table
									DatabaseOperations.insertRecipe(sinleRecipeOutput,filterVo.getFilterName()+"_Elimination");

									//Step 5 -  get list(set) of matching allergic ingredients from recipe ingredients
									Set<String> matchingAllergicIngredients=getMatchingAllergiIngredients(sinleRecipeOutput.getPlainIngredientsList());

									//Step 6 - 'for each' allergic ingredient found, insert into respective allergy table
									if(!matchingAllergicIngredients.isEmpty())
									{
										System.out.println("Got matching allergic ingredients.."+matchingAllergicIngredients);
										for (String singleAllergicIngredient : matchingAllergicIngredients) {

											System.out.println("Inserting into "+filterVo.getFilterName()+"_Allergy_"+singleAllergicIngredient.replaceAll(" ", "_"));
											DatabaseOperations.insertRecipe(sinleRecipeOutput,filterVo.getFilterName()+"_Allergy_"+singleAllergicIngredient.replaceAll(" ", "_"));
										}
									}
									else
									{
										System.out.println("this recipe is not having any allergic ingredients !");
									}
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

							if(!filterVo.getTo_Add_If_notFullyVgean().isEmpty())
							{
								// Step 7
								//prepare new elimination criteria ~ regular elimination minus "to add"
								List<String> toAddEliminationNew= new ArrayList<String>();
								toAddEliminationNew.addAll(filterVo.getLstEliminate());  //e.g. butter can be there
								toAddEliminationNew.removeAll(filterVo.getTo_Add_If_notFullyVgean()); // removed e.g. butter

								// recipe is NOT having new elimination criteria
								if(! Library.isIngPresent(sinleRecipeOutput.getPlainIngredientsList(), toAddEliminationNew))
								{
									// recipe is having "to_add" ingredients
									if( Library.isIngPresent(sinleRecipeOutput.getPlainIngredientsList(), filterVo.getTo_Add_If_notFullyVgean()) &&
											! Library.isNeedToAvoidThisRecipe(sinleRecipeOutput.getTags(), filterVo.getRecipeToAvoid())	)
									{
										// Step 8 - if to_add ingredients present, insert into *_to_add table
										DatabaseOperations.insertRecipe(sinleRecipeOutput,filterVo.getFilterName()+"_to_add");
									}

								}
							}
						}

						driver.navigate().to(URL+"?beginswith="+currentSearchTerm+"&pageindex="+pageNumber);
					}

				} catch (Exception e) {

					e.printStackTrace();

					System.out.println(" ========== Error, skipping that recipe Currently Searching -> "+currentSearchTerm +" Page : "+(pageNumber)+" ========== ");
				}
			}
		}
		finally
		{
			if(driver!=null)
				driver.quit();
		}

	}


	private  Set<String> getMatchingAllergiIngredients(List<String> plainIngredientsList) {

		Set<String> allergicIngr=new HashSet<String>();
		for (String plainIngr : plainIngredientsList) {

			for (String algIng :Hooks. allergies) {


				if(algIng.trim().equalsIgnoreCase(plainIngr.trim())||
						(plainIngr.trim()).equalsIgnoreCase(algIng.trim()+"s")||(plainIngr.trim()).equalsIgnoreCase(algIng.trim()+"es")||
						Arrays.asList(plainIngr.trim().split(" ")).contains(algIng.toLowerCase().trim())||
						Arrays.asList(plainIngr.trim().split(" ")).contains((algIng+"s").toLowerCase().trim())||
						Arrays.asList(plainIngr.trim().split(" ")).contains((algIng+"es").toLowerCase().trim()))
				{
					allergicIngr.add(algIng);
				}
			}
		}

		return allergicIngr;

	}



	@DataProvider (name = "data-provider", parallel = true)
	public Object[][] dpMethod(){

		String search[][]= {{"A"},{"B"},{"C"},{"D"},{"E"},{"F"},{"G"},{"H"},{"I"},{"J"},{"K"},{"L"},{"M"},{"N"},{"O"},{"P"},{"Q"},{"R"},{"S"},{"T"},{"U"},{"V"},{"W"},{"X"},{"Y"},{"Z"},{"Misc"}};

		return search;
	}



}
