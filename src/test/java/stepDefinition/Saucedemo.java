package stepDefinition;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Saucedemo {


	private WebDriver driver;
	private ExtentReports extent;
	private ExtentTest test;

	By Username = By.xpath("//input[@id='user-name']");
	By Password = By.xpath("//input[@id='password']");
	By LoginButton = By.xpath("//input[@id='login-button']");
	By errormsg =  By.xpath("//h3[contains(text(),'Epic sadface: Username and password do not match any user in this service')]");
	By Productpage = By.xpath("//*[@id=\"header_container\"]/div[2]/span");
	By inventoryitem = By.cssSelector(".inventory_item");
	By itemprice = By.cssSelector(".inventory_item_price");
	By inventorybtn = By.cssSelector(".btn_primary.btn_inventory");
	By itemname = By.cssSelector(".inventory_item_name");
	By inventorybtn2 = By.cssSelector(".btn_secondary.btn_inventory");
	By cart = By.cssSelector(".shopping_cart_link");


	// Step 1: Navigate to the login page and login with incorrect credentials
	@Given("User navigate to SauceDemo URL {string}")
	public void user_navigate_to_sauce_demo_url(String string) throws InterruptedException {
		ExtentSparkReporter reporter = new ExtentSparkReporter("target/extent-reports.html");
		extent = new ExtentReports();
		extent.attachReporter(reporter);
		test = extent.createTest("SauceDemo Login Test");

		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(string);
		test.pass("Navigated to SauceDemo login page");
		Thread.sleep(2000);
	}

	@When("User log in with incorrect username {string} and password {string}")
	public void user_log_in_with_incorrect_username_and_password(String string, String string2) throws InterruptedException {
		WebElement usernameField = driver.findElement(Username);
		WebElement passwordField = driver.findElement(Password);
		WebElement button=driver.findElement(LoginButton);
		usernameField.sendKeys(string);
		Thread.sleep(2000);
		passwordField.sendKeys(string2);
		Thread.sleep(2000);
		button.click();
		Thread.sleep(2000);

	}

	@Then("User should see an error message")
	public void the_user_should_see_an_error_message() {

		WebElement errorMessage = driver.findElement(errormsg);
		String errorText = errorMessage.getText();
		System.out.println("Error message displayed: " + errorText);


		if (errorText.contains(errorText)) {

			System.out.println("Test Passed: Correct error message is displayed.");
		} else {

			System.out.println("Test Failed: Unexpected error message.");
		}

		extent.flush();
		driver.quit();
	}

	// Step 2: Login with correct credentials
	@When("User log in with valid username1 {string} and password1 {string}")
	public void user_log_in_with_valid_username1_and_password1(String string, String string2) throws InterruptedException {
		WebElement usernameField = driver.findElement(Username);
		WebElement passwordField = driver.findElement(Password);
		WebElement button=driver.findElement(LoginButton);
		usernameField.sendKeys(string);
		Thread.sleep(2000);
		passwordField.sendKeys(string2);
		Thread.sleep(2000);
		button.click();
		Thread.sleep(2000);
	}

	@Then("User should be redirected to the inventory page")
	public void the_user_should_be_logged_in_successfully() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(Productpage));  
		test.pass("Logged in with correct credentials");
		System.out.println("Logged in with correct credentials");

	}

	// Step 3: Add the 4 cheapest items to the cart
	@Then("Add the 4 cheapest items to the cart and the total price should be below $50")
	public void the_user_is_logged_in() throws InterruptedException {
		System.out.println("Add 4 cheapest items to the cart");
		Thread.sleep(2000);

		// Locate all product elements and their prices
		List<WebElement> products = driver.findElements(inventoryitem);
		Map<WebElement, Double> productPriceMap = new HashMap<>();

		for (WebElement product : products) {
			String priceText = product.findElement(itemprice)
					.getText().replace("$", "").trim();
			double price = Double.parseDouble(priceText);
			productPriceMap.put(product, price);
		}

		// Sort products by price in ascending order
		List<Map.Entry<WebElement, Double>> sortedProducts = productPriceMap.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toList());

		// Add the 4 cheapest products to the cart
		List<Map.Entry<WebElement, Double>> selectedProducts = sortedProducts.subList(0, Math.min(5, sortedProducts.size()));
		double totalPrice = 0.0;

		for (Map.Entry<WebElement, Double> entry : selectedProducts) {
			entry.getKey().findElement(inventorybtn).click();
			totalPrice += entry.getValue();
			System.out.println("Added: " + entry.getKey().findElement(itemname).getText() + " - Price: $" + entry.getValue());

		}

		// Store the total price for validation
		System.out.println("Total Price After Adding: $" + totalPrice);
		test.pass("Total Price After Adding: $" + totalPrice);
		Thread.sleep(2000);


		// Remove items if the total price exceeds $50
		if (totalPrice > 50) {
			for (int i = selectedProducts.size() - 1; i >= 0; i--) {
				if (totalPrice <= 50) break;

				WebElement productToRemove = selectedProducts.get(i).getKey();
				productToRemove.findElement(inventorybtn2).click();
				totalPrice -= selectedProducts.get(i).getValue();

				System.out.println("Removed: " + productToRemove.findElement(itemname).getText() + " - New Total Price: $" + totalPrice);
				test.pass("Removed: " + productToRemove.findElement(itemname).getText() + " - New Total Price: $" + totalPrice);
			}
		}

		System.out.println("Final Total Price: $" + totalPrice);
		test.pass("Final Total Price: $" + totalPrice);
		Thread.sleep(2000);
		extent.flush();

	}

	//Items added to the cart
	@And("User navigate to the cart page")
	public void cartpage() throws InterruptedException {
		driver.findElement(cart).click();
		System.out.println("Navigated to the cart.");
		Thread.sleep(4000);
		extent.flush();
		driver.quit();
	}

}
