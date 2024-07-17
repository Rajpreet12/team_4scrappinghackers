package com.hooks;

import java.util.Arrays;import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;

import com.recipe.MasterClass;
import com.recipe.database.DatabaseOperations;
import com.recipe.vos.FilterVo;
import com.utilities.ConfigReader;
import com.utilities.ExcelReader;

public class Hooks {

	public static ConfigReader configreader;

	Properties prop;

	//	WebDriver driver;

	public static List<String> allergies=Arrays.asList("Milk","Soy","Egg","Sesame","Peanuts","Walnut","Almond","Hazelnut","Pecan","Cashew","Pistachio","Shell fish","Seafood");


	@BeforeSuite(alwaysRun = true)
	public void beforeSuiteWork()
	{
		System.out.println("Inside beforeSuiteWork.....");

		configreader=new ConfigReader();

		try {
			prop=configreader.loadConfig();
		} catch (Throwable e) {

			e.printStackTrace();
		}

		//------------------------------------------------------------------------LFV filter----------------------------------------------------------------------

		String file="C:\\Users\\rajes\\eclipse-workspace\\team_4scrappinghackers-Single\\team_4scrappinghackers-Single\\src\\test\\resources\\IngredientsAndComorbidities-ScrapperHackathon_New.xlsx";
		String sheet="Final list for LFV Elimination ";
		String LCHFsheet="Final list for LCHFElimination ";


		Integer toAddCol=2;
		Integer avoidTermCol=3;

		FilterVo filterVo_LFV= ExcelReader.read(file, sheet,toAddCol,avoidTermCol);

		System.out.println("LFV Filter.....");
		System.out.println("LFV eliminate ingr -->"+filterVo_LFV.getLstEliminate());
		System.out.println("LFV add ingr -->" +filterVo_LFV.getLstAdd());
		System.out.println("LFV avoid receipe -->"+filterVo_LFV.getRecipeToAvoid());
		//System.out.println("LFV optional -->" +filterVo.getOptinalRecipes());

		filterVo_LFV.getLstAdd().add("tea");
		filterVo_LFV.getLstAdd().add("coffee");
		filterVo_LFV.getLstAdd().add("herbal drink");
		filterVo_LFV.getLstAdd().add("chai");

		filterVo_LFV.setFilterName("LFV");
		System.out.println("LFV add ingr -->" +filterVo_LFV.getLstAdd());

		//--------------------------------------------------------------------------LCHFE filter---------------------------------------------------------------------

		toAddCol=null;
		avoidTermCol=2;

		FilterVo filterVo_LCHFE= ExcelReader.read(file,LCHFsheet,toAddCol,avoidTermCol);
		
		String[] foodProcesses = new String[] {"Raw", "Steamed", "Boiled", "Porched", "Sauted", "Airfryed", "Pan fried"};
		
		filterVo_LCHFE.setLstAddfoodprocess(Arrays.asList(foodProcesses));		
	
		/*
		 * filterVo_LCHFE.getLstAddfoodprocess().add("Raw");
		 * filterVo_LCHFE.getLstAddfoodprocess().add("Steamed");
		 * filterVo_LCHFE.getLstAddfoodprocess().add("Boiled");
		 * filterVo_LCHFE.getLstAddfoodprocess().add("Porched");
		 * filterVo_LCHFE.getLstAddfoodprocess().add("Sauted");
		 * filterVo_LCHFE.getLstAddfoodprocess().add("Airfryed");
		 * filterVo_LCHFE.getLstAddfoodprocess().add("Pan fried");
		 */
		
		filterVo_LCHFE.getRecipeToAvoid().add("Processed");
		System.out.println("LCHFE Filter.....");
		System.out.println("LCHFE eliminate ingr -->"+filterVo_LCHFE.getLstEliminate());
		System.out.println("LCHFE add ingr -->" +filterVo_LCHFE.getLstAdd());
		System.out.println("LCHFE avoid receipe -->"+filterVo_LCHFE.getRecipeToAvoid());
		System.out.println("LCHFE Addfoodprocess receipe -->"+filterVo_LCHFE.getLstAddfoodprocess());
		
		
		filterVo_LCHFE.setFilterName("LCHFE");

		System.out.println("LCHFE add ingr -->" +filterVo_LCHFE.getLstAdd());
		//-------------------------------------------------------------decide which filter to apply LFV, or LCHFE or.... ------------------------------------------------------


		//MasterClass.filterVo=filterVo_LFV;

		MasterClass.filterVo=filterVo_LCHFE;

		prepareDatabase(MasterClass.filterVo.getFilterName());

		MasterClass.alreadySaved=DatabaseOperations.getAlreadyCheckedRecipeIds(MasterClass.filterVo.getFilterName());

		System.out.println("alreadySaved count "+MasterClass.alreadySaved.size());

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