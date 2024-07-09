package com.driverfactory;
import java.time.Duration;
import java.util.ResourceBundle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


import com.utilities.Loggerload;

public class Driver_class {

	public static WebDriver driver;
	static ResourceBundle rb; // for reading properties file
	static String br; // to store browser name
	static String base_url; // to store base URL
	static String inalid_url;// to store invalid URL
	static String imageSource; // to store actual image URL

	//used to manage the creation nd configuratio of webdriver instance
	public static WebDriver getdesireDriver() {

		rb = ResourceBundle.getBundle("config");
		br = rb.getString("browser");

		if (br.equals("chrome")) {
			// WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if (br.equals("firefox")) {
			driver = new FirefoxDriver();
		} else if (br.equals("edge")) {
			driver = new EdgeDriver();
		}

		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

		return driver;
	}

	public static void openPage() {
		base_url = rb.getString("url");
		getDriver().get(base_url);
	}

	public static String getTitle() {
		return getDriver().getTitle();
	}

	public static void NavBack() {
		getDriver().navigate().back();
	}

	public static void toRefresh() {
		getDriver().navigate().refresh();
	}

	public static WebDriver getDriver() {
		return driver;
	}

	public static void setUpDriver() {

		if (getDriver() == null) {
			getdesireDriver();
		}
	}

	public static void invalidURL() {
		inalid_url = rb.getString("invalidUrl");
		getDriver().get(inalid_url);
	}

	public static void getImageSource() {
		imageSource = rb.getString("sourceImage");
        getDriver().get(imageSource);
	}

	public static void tearDown() {
		if (getDriver() != null) {
			Loggerload.info("close the Web browser");
			getDriver().close();
			getDriver().quit();
		}
		driver = null;
	}

}
