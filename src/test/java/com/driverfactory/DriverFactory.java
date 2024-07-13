package com.driverfactory;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.utilities.LoggerLoad;

public class DriverFactory {

	public WebDriver driver;
	public static ThreadLocal<WebDriver> tldriver=new ThreadLocal<>();

	public WebDriver init_driver(String browser)
	{

		if(browser.equalsIgnoreCase("chrome")) {
			//WebDriverManager.chromedriver().setup();
			tldriver.set(new ChromeDriver());
		}
		else if(browser.equalsIgnoreCase("firefox")) {
			//WebDriverManager.firefoxdriver().setup();
			tldriver.set(new FirefoxDriver());
			//FirefoxOptions firefoxOptions=new FirefoxOptions();
			//firefoxOptions.addArguments("--headless");
			//driver=new (new FirefoxDriver(firefoxOptions));
		}
		else if(browser.equalsIgnoreCase("safari")) {
			//WebDriverManager.safaridriver().setup();
			tldriver.set(new SafariDriver());
		}
		else if(browser.equalsIgnoreCase("edge")) {
			//WebDriverManager.edgedriver().setup();
			tldriver.set(new EdgeDriver());
		}
		else
		{
			LoggerLoad.info("Please pass correct Browser value "+browser);
		}
		getdriver().manage().deleteAllCookies();
		getdriver().manage().window().maximize();
		getdriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(50));
		return getdriver();
	}

	public static synchronized WebDriver getdriver()
	{
		return tldriver.get();
	}
	public static synchronized void setDriver(WebDriver driver)
	{
		tldriver.set(driver);
	}
}

