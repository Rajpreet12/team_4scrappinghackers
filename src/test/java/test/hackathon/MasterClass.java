package test.hackathon;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MasterClass {

	public static WebDriver driver;
	public static final String URL="https://www.tarladalal.com/RecipeAtoZ.aspx";

	public static List<Integer> alreadySaved;


	public static void main(String[] args) throws Exception{

		String file="/Users/ashwini/Desktop/hackathon/IngredientsAndComorbidities-ScrapperHackathon.xlsx";
		String sheet="Final list for LFV Elimination ";

		FilterVo filterVo= LFVFilterReader.read(file, sheet);

		System.out.println("LFV eliminate ingr -->"+filterVo.getLstEliminate());
		System.out.println("LFV add ingr -->" +filterVo.getLstAdd());

		alreadySaved=DBOps.getSavedRecipeIds();

		getAllReceipesByFilter( filterVo);

	}

	public static void getAllReceipesByFilter(FilterVo filterVo)
	{
		WebDriverManager.firefoxdriver().setup();

		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(2));

		String search[]= {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","Misc"};

		driver.get(URL);

		for (String currentSearchTerm : search) 
		{
			handledSingleSearchTerm(currentSearchTerm,filterVo);
		}

		driver.quit();

	}

	private static void handledSingleSearchTerm(String currentSearchTerm,FilterVo filterVo) {

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

					nameEle.click();

					//driver.navigate().to("https://www.tarladalal.com/pyaaz-ki-kachori-3636r");

					RecipeVo sinleRecipeOutput = Library.getRecipeDetails(driver);

					sinleRecipeOutput.setRecipe_ID(rcpId);

					//	System.out.println("_________________________Recipe :_______________________________");
					//	System.out.println(sinleRecipeOutput);

					if(!isIngPresent(sinleRecipeOutput.getPlainIngredientsList(), filterVo.getLstEliminate(),"for elemination"))
					{
						if(isIngPresent(sinleRecipeOutput.getPlainIngredientsList(), filterVo.getLstAdd(),"for to add"))
						{
							System.out.println("this recipe is having at least one to add ingredient [required recipe] !");

							if(!isNeedToAvoidThisRecipe(sinleRecipeOutput.getTags(), FilterVo.LFV_Avoid))
								DBOps.insertRecipe(sinleRecipeOutput);
							else
							{
								System.out.println("[###3 ignore recipe] this recipe is HAVING avoiding terms !");

							}
						}
						else
						{
							System.out.println("[##2 ignore recipe] this recipe is NOT having any required(add) igredients !");

						}
					}
					else
					{
						System.out.println("[#1 ignore recipe] this recipe is HAVING some of eleminated ingredient !");
					}


					driver.navigate().to(URL+"?beginswith="+currentSearchTerm+"&pageindex="+pageNumber);
				}

			} catch (Exception e) {

				e.printStackTrace();

				System.out.println(" ========== Error, skipping that recipe Currently Searching -> "+currentSearchTerm +" Page : "+(pageNumber)+" ========== ");
			}
		}
	}


	private static boolean isNeedToAvoidThisRecipe(List<String> allTags,List<String> lFV_Avoid) {
		
		
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

	private static boolean isIngPresent(List<String> inputRecIngr, List<String> criteriaList, String toLog) {

		for (String singleIng : inputRecIngr) {

			for (String eleIng : criteriaList) {

				if(singleIng.toLowerCase().contains(eleIng.toLowerCase()))
				{
					System.out.println(" found  "+eleIng+ "... in "+singleIng +"["+toLog+"]");
					return true;
				}
			}
		}

		return false;
	}
}
