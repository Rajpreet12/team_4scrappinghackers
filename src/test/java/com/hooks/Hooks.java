package com.hooks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.testng.annotations.BeforeSuite;

import com.recipe.RecipeScrapper;
import com.recipe.database.DatabaseOperations;
import com.recipe.vos.FilterVo;
import com.utilities.ConfigReader;
import com.utilities.ExcelReader;

import team_4scrappinghackers.LoggerLoad;


public class Hooks {

	public static Map<String,String> ExceptionIngredientMapping= new HashMap<String,String>();

	public static ConfigReader configreader;

	Properties prop;

	//	WebDriver driver;

	public static List<String> allergies=Arrays.asList("Milk","Soy","Egg","Sesame","Peanuts","Walnut","Almond","Hazelnut","Pecan","Cashew","Pistachio","Shell fish","Seafood");


	@BeforeSuite(alwaysRun = true)
	public void beforeSuiteWork()
	{
		LoggerLoad.info("Inside beforeSuiteWork.....");

		configreader=new ConfigReader();

		try {
			prop=configreader.loadConfig();
		} catch (Throwable e) {

			e.printStackTrace();
		}

		//------------------------------------------------------------------------LFV filter----------------------------------------------------------------------

		String file="/Users/ashwini/Desktop/hackathon/IngredientsAndComorbidities-ScrapperHackathon_New.xlsx";
		String sheet="Final list for LFV Elimination ";
		String LCHFsheet="Final list for LCHFElimination ";


		Integer toAddCol=2;
		Integer avoidTermCol=3;

		FilterVo filterVo_LFV= ExcelReader.read(file, sheet,toAddCol,avoidTermCol);

		LoggerLoad.info("LFV Filter.....");
		System.out.println("LFV eliminate ingr -->"+filterVo_LFV.getLstEliminate());
		System.out.println("LFV add ingr -->" +filterVo_LFV.getLstAdd());
		System.out.println("LFV avoid receipe -->"+filterVo_LFV.getRecipeToAvoid());
		//System.out.println("LFV optional -->" +filterVo.getOptinalRecipes());

		filterVo_LFV.getLstAdd().add("tea");
		filterVo_LFV.getLstAdd().add("coffee");
		filterVo_LFV.getLstAdd().add("herbal drink");
		filterVo_LFV.getLstAdd().add("chai");

		filterVo_LFV.setFilterName("LFV");


		//--------------------------------------------------------------------------LCHFE filter---------------------------------------------------------------------

		toAddCol=null;
		avoidTermCol=2;

		FilterVo filterVo_LCHFE= ExcelReader.read(file,LCHFsheet,toAddCol,avoidTermCol);
		System.out.println("LCHFE Filter.....");
		System.out.println("LCHFE eliminate ingr -->"+filterVo_LCHFE.getLstEliminate());
		System.out.println("LCHFE add ingr -->" +filterVo_LCHFE.getLstAdd());
		System.out.println("LCHFE avoid receipe -->"+filterVo_LCHFE.getRecipeToAvoid());
		filterVo_LCHFE.setFilterName("LCHFE");


		//-------------------------------------------------------------decide which filter to apply LFV, or LCHFE or.... ------------------------------------------------------

		//to mention in lowercase
		ExceptionIngredientMapping.put("pea", "chick pea");
		ExceptionIngredientMapping.put("potato", "sweet potato");


		RecipeScrapper.filterVo=filterVo_LFV;

		//	RecipeScrapper.filterVo=filterVo_LCHFE;

		String[] foodProcesses = new String[] {"Raw", "Steamed", "Boiled", "Porched", "Sauted", "Airfryed", "Pan fried"};

		filterVo_LCHFE.setLstAddfoodprocess(Arrays.asList(foodProcesses));

		filterVo_LCHFE.getRecipeToAvoid().add("Processed");

		prepareDatabase(RecipeScrapper.filterVo.getFilterName());

		RecipeScrapper.alreadySaved=DatabaseOperations.getAlreadyCheckedRecipeIds(RecipeScrapper.filterVo.getFilterName());

		System.out.println("alreadySaved count "+RecipeScrapper.alreadySaved.size());

	}

	private void prepareDatabase(String filterName) {


		String query="CREATE TABLE  IF NOT EXISTS public.<TABLE_NAME>"
				+ "("
				+ "  "
				+ "    \"Recipe_ID\" integer NOT NULL,"
				+ "    \"Recipe_Name\" text NOT NULL,"
				+ "    \"Recipe_Category\" text,"
				+ "    \"Food_Category\" text,"
				+ "    \"Ingredients\" text,"
				+ "    \"Preparation_Time\" text,"
				+ "    \"Cooking_Time\" text,"
				+ "    \"Tag\" text,"
				+ "    \"No_of_servings\" text,"
				+ "    \"Cuisine_category\" text,"
				+ "    \"Recipe_Description\" text,"
				+ "    \"Preparation_method\" text,"
				+ "    \"Nutrient_values\" text,"
				+ "    \"Recipe_URL\" text,"
				+ "    PRIMARY KEY (\"Recipe_ID\")"
				+ ");";


		DatabaseOperations.createTable("CREATE TABLE IF NOT EXISTS public.\"AlreadyCheckedRecipes_"+filterName+"\""
				+ "("
				+ "    \"Recipe_Id\" integer NOT NULL,"
				+ "    CONSTRAINT \"AlreadyCheckedRecipes_"+filterName+"_pkey\" PRIMARY KEY (\"Recipe_Id\")"
				+ ")"
				+ "");

		DatabaseOperations.createTable(query.replace("<TABLE_NAME>",filterName+"_elimination"));

		if(filterName.equalsIgnoreCase("lfv"))
			DatabaseOperations.createTable(query.replace("<TABLE_NAME>",filterName+"_to_add"));


		for (String singleAllergyTerm : allergies) {

			DatabaseOperations.createTable(query.replace("<TABLE_NAME>", filterName+"_Allergy_"+singleAllergyTerm.replaceAll(" ", "_")));
		}
	}

	//	@AfterTest
	//	public   void quitBrowser()
	//	{
	//		System.out.println("Inside quitBrowser.....");
	//
	//		driver.quit();
	//	}


}