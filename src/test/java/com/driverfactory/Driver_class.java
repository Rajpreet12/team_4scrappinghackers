package com.driverfactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;



public class Driver_class {
    public WebDriver driver;

    public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

    public WebDriver init_driver(String Browser) {
        System.out.println("browser value is :" + Browser);
        if (Browser.equals("chrome")) {
           // WebDriverManager.chromedriver().setup();
            tlDriver.set(new ChromeDriver());
        } else if (Browser.equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            //WebDriverManager.firefoxdriver().setup();
            tlDriver.set(new FirefoxDriver(options));
        } else if (Browser.equals("safari")) {
            //WebDriverManager.safaridriver().setup();
            tlDriver.set(new SafariDriver());
        } else if (Browser.equals("edge")) {
           // WebDriverManager.edgedriver().setup();
            tlDriver.set(new EdgeDriver());
        }else {
            System.out.println("Please pass the correct browser value :" + Browser);
        }
        return getDriver();
    }

    public static synchronized WebDriver getDriver() {
        return tlDriver.get();
    }

	public static void tearDown() {
		// TODO Auto-generated method stub
		
	}



}