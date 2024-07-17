package com.Hook;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.LFV.FilterVo;
import com.LFV.MasterClass;
import com.utilities.ConfigReader;
import com.utilities.ExcelReader;

import Database.DatabaseOperations;

public class Hook {

    private WebDriver driver;
    public static ConfigReader configreader1;
     Properties prop;

    public static List<String> allergies = Arrays.asList("Milk", "Soy", "Egg", "Sesame", "Peanuts", "Walnut", "Almond", "Hazelnut", "Pecan", "Cashew", "Pistachio", "Shell fish", "Seafood");

    private static final Logger logger = LoggerFactory.getLogger(Hook.class);

    @BeforeSuite(alwaysRun = true)
    public void beforeSuiteWork() {
        logger.info("Inside beforeSuiteWork...");

        try {
            configreader1 = new ConfigReader();
            prop =  (configreader1).loadConfig();

            String browser = configreader1.getBrowser();
            String mode = configreader1.getBrowserMode();

            logger.info("Browser: {}", browser);
            logger.info("Mode: {}", mode);
        } catch (Throwable e) {
            logger.error("Error loading config: {}", e.getMessage());
        }

        String file = "C:\\Users\\Rajpr\\eclipse-workspace\\Team4_ScrappingHackers\\src\\test\\resources\\data\\IngredientsAndComorbidities-ScrapperHackathon.xlsx";
        String sheet = "Final list for LFV Elimination ";
        String LCHFsheet = "Final list for LCHFElimination ";

        FilterVo filterVo = ExcelReader.read(file, sheet);

        logger.info("LFV eliminate ingredients: {}", filterVo.getLstEliminate());
        logger.info("LFV add ingredients: {}", filterVo.getLstAdd());
        logger.info("LFV avoid recipes: {}", filterVo.getRecipeToAvoid());

        Integer toAddCol = 2;
        Integer avoidTermCol = 3;

        FilterVo filterVo_LFV = ExcelReader.read(file, sheet, toAddCol, avoidTermCol);

        logger.info("LFV Filter...");
        logger.info("LFV eliminate ingredients: {}", filterVo_LFV.getLstEliminate());
        logger.info("LFV add ingredients: {}", filterVo_LFV.getLstAdd());
        logger.info("LFV avoid recipes: {}", filterVo_LFV.getRecipeToAvoid());

        filterVo.getLstAdd().addAll(Arrays.asList("tea", "coffee", "herbal drink", "chai"));
        filterVo_LFV.getLstAdd().addAll(Arrays.asList("tea", "coffee", "herbal drink", "chai"));

        filterVo_LFV.setFilterName("LFV");

        FilterVo filterVo_LCHFE = ExcelReader.read(file, LCHFsheet, null, avoidTermCol);
        logger.info("LCHFE Filter...");
        logger.info("LCHFE eliminate ingredients: {}", filterVo_LCHFE.getLstEliminate());
        logger.info("LCHFE add ingredients: {}", filterVo_LCHFE.getLstAdd());
        logger.info("LCHFE avoid recipes: {}", filterVo_LCHFE.getRecipeToAvoid());
        filterVo_LCHFE.setFilterName("LCHFE");

        MasterClass.filterVo = filterVo_LFV;

        MasterClass.filterVo = filterVo;
        // MasterClass.filterVo = filterVo_LCHFE;

        MasterClass.alreadySaved = DatabaseOperations.getAlreadyCheckedRecipeIds(toString());
        prepareDatabase(MasterClass.filterVo.getFilterName());

        MasterClass.alreadySaved = DatabaseOperations.getAlreadyCheckedRecipeIds(MasterClass.filterVo.getFilterName());

        logger.info("Already saved count: {}", MasterClass.alreadySaved.size());
    }

    private void prepareDatabase(String filterName) {
        String query = "CREATE TABLE  IF NOT EXISTS public.<TABLE_NAME>"
                + "("
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

        DatabaseOperations.createTable("CREATE TABLE IF NOT EXISTS public.\"AlreadyCheckedRecipes_" + filterName + "\""
                + "("
                + "    \"Recipe_Id\" integer NOT NULL,"
                + "    CONSTRAINT \"AlreadyCheckedRecipes_" + filterName + "_pkey\" PRIMARY KEY (\"Recipe_Id\")"
                + ")"
                + "");

        DatabaseOperations.createTable(query.replace("<TABLE_NAME>", filterName + "_elimination"));

        if (filterName.equalsIgnoreCase("lfv"))
            DatabaseOperations.createTable(query.replace("<TABLE_NAME>", filterName + "_to_add"));

        for (String singleAllergyTerm : allergies) {
            DatabaseOperations.createTable(query.replace("<TABLE_NAME>", filterName + "_Allergy_" + singleAllergyTerm.replaceAll(" ", "_")));
        }
    }

    @BeforeTest
    public void launchBrowser() {
        // String targetBrowser = configreader.getBrowser();
        // logger.info("Target browser: {}", targetBrowser);
        // driverfactory = new DriverFactory();
        // driver = driverfactory.init_driver(targetBrowser);
    }

    @AfterTest
    public void quitBrowser() {
        if (driver != null) {
            driver.quit();
            logger.info("Browser closed");
        }
    }
}
