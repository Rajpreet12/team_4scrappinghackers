//package com.LFV;
//
//
//	
//
//	import org.openqa.selenium.By;
//	import org.openqa.selenium.WebDriver;
//	import org.openqa.selenium.WebElement;
//	import org.openqa.selenium.chrome.ChromeDriver;
//	import org.openqa.selenium.support.ui.ExpectedConditions;
//	import org.openqa.selenium.support.ui.WebDriverWait;
//
//	import com.receipecategory.ReceipeCategory;
//
//	import java.time.Duration;
//	import java.util.ArrayList;
//	import java.util.HashSet;
//	import java.util.List;
//	import java.util.Set;
//
//	public class Not_Vegan {
//
//	    WebDriver driver;
//
//	    public Not_Vegan() {
//	        // Initialize WebDriver
//	        driver = new ChromeDriver();
//	        driver.manage().window().maximize();
//	        driver.get("https://www.tarladalal.com/");
//	        driver.findElement(By.linkText("Recipe A To Z")).click();
//	    }
//
//	    public List<String> getAllRecipes() {
//	        Set<String> nonVeganRecipes = new HashSet<>();
//	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
//
//	        // Outer loop: Loop through all alphabet links
//	        for (int alphabetID = 1; alphabetID <= 26; alphabetID++) {
//	            String alphabetLinkXPath = "//*[@id='ctl00_cntleftpanel_mnuAlphabetsn" + alphabetID + "']";
//	            WebElement alphabetLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(alphabetLinkXPath)));
//	            alphabetLink.click();
//
//	            // Inner loop: Loop through pages 1 to 22 for the current alphabet
//	            for (int pageIndex = 1; pageIndex <= 22; pageIndex++) {
//	                try {
//	                    List<WebElement> recipeCards = driver.findElements(By.xpath("//*[@class ='rcc_recipecard']"));
//	                    System.out.println("Found " + recipeCards.size() + " recipes on page " + pageIndex + " of alphabet " + alphabetID + ".");
//
//	                    for (WebElement recipeCard : recipeCards) {
//	                        ReceipeCategory recipe = Library.getRecipeDetails(driver);
//
//	                        if (FilterVo.isNot_Vegan(recipe)) {
//	                            nonVeganRecipes.add(recipe.getRecipe_Name());
//	                            System.out.println("Non-vegan recipe found: " + recipe.getRecipe_Name());
//	                        }
//	                    }
//
//	                    // Check if there is a next page
//	                    String nextPageXPath = "//*[@id='paginationDiv']//a[text()='" + (pageIndex + 1) + "']";
//	                    if (driver.findElements(By.xpath(nextPageXPath)).isEmpty()) {
//	                        break; // No more pages
//	                    }
//
//	                    WebElement nextPageButton = driver.findElement(By.xpath(nextPageXPath));
//	                    nextPageButton.click();
//	                    wait.until(ExpectedConditions.stalenessOf(driver.findElement(By.xpath("//*[@class='rescurrpg']"))));
//	                } catch (Exception e) {
//	                    e.printStackTrace();
//	                    break; // Break inner loop if there's an error
//	                }
//	            }
//
//	            // Navigate back to the alphabet page after finishing pagination for the current alphabet
//	            driver.findElement(By.linkText("Recipe A To Z")).click();
//	            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(alphabetLinkXPath)));
//	        }
//
//	        return new ArrayList<>(nonVeganRecipes);
//	    }
//
//	    public static void main(String[] args) {
//	        Not_Vegan scraper = new Not_Vegan();
//	        List<String> nonVeganRecipes = scraper.getAllRecipes();
//	        System.out.println("Total non-vegan recipes found: " + nonVeganRecipes.size());
//	        for (String recipe : nonVeganRecipes) {
//	            System.out.println("Non-vegan recipe found: " + recipe);
//	        }
//	        scraper.driver.quit();  // Ensure the driver is closed after scraping
//	    }
//	}
//
//
//}
