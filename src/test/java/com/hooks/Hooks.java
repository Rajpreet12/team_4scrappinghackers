package com.hooks;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.driverfactory.DriverFactory;
import com.recipe.MasterClass;
import com.recipe.database.DatabaseOperations;
import com.recipe.vos.FilterVo;
import com.utilities.ConfigReader;
import com.utilities.ExcelReader;

public class Hooks {

	private DriverFactory driverfactory;
	private WebDriver driver;

	private ConfigReader configreader;
	Properties prop;

	@BeforeSuite(alwaysRun = true)
	public void beforeSuiteWork()
	{
		configreader=new ConfigReader();

		try {
			prop=configreader.loadConfig();
		} catch (Throwable e) {

			e.printStackTrace();
		}


		String file="/Users/ashwini/Desktop/hackathon/IngredientsAndComorbidities-ScrapperHackathon_New.xlsx";
		String sheet="Final list for LFV Elimination ";

		FilterVo filterVo= ExcelReader.read(file, sheet);

		System.out.println("LFV eliminate ingr -->"+filterVo.getLstEliminate());
		System.out.println("LFV add ingr -->" +filterVo.getLstAdd());
		System.out.println("LFV avoid receipe -->"+filterVo.getRecipeToAvoid());
		//System.out.println("LFV optional -->" +filterVo.getOptinalRecipes());


		filterVo.getLstAdd().add("tea");
		filterVo.getLstAdd().add("coffee");
		filterVo.getLstAdd().add("herbal drink");
		filterVo.getLstAdd().add("chai");

		filterVo.getLstEliminate().add("sugar substitute");

		MasterClass.filterVo=filterVo;


		MasterClass.alreadySaved=DatabaseOperations.getAlreadyCheckedRecipeIds();

		System.out.println("alreadySaved count "+MasterClass.alreadySaved.size());
	}


	@BeforeTest
	public void launchBrowser()
	{
		//		String targetBrowser=configreader.getBrowser();
		//		System.out.println("targetBrowser--> "+targetBrowser);
		//		
		//		driverfactory=new DriverFactory();
		//
		//		driver=driverfactory.init_driver(targetBrowser);	
	}


	@AfterTest
	public void quitBrowser()
	{
		driver.quit();
	}
}