package com.Hook;


import java.io.IOException;


import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.driverfactory.Driver_class;
import com.utilities.Loggerload;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;


public class Hook {
	
	@Before(order = 0)
	public static void setUp() throws IOException {
		Driver_class.setUpDriver();
		Loggerload.info("driver setup");
	}

	@Before(order = 1)
	public static void setup2() {
		Driver_class.getDriver();
		Loggerload.info("getdriver");
	}

	@After
	public static void tearDown(Scenario scenario) throws IOException {
		
		if (scenario.isFailed()) {
			final byte[] screenshot = ((TakesScreenshot) Driver_class.getDriver()).getScreenshotAs(OutputType.BYTES);
			scenario.attach(screenshot, "image/png", scenario.getName());
		}

		Driver_class.tearDown();
	}
}

