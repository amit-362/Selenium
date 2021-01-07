package com.automationpractice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Files;

public class PurchasingItemsTests {

	public final String url = "http://automationpractice.com/index.php";

	@Test
	public void purchasingTest() throws Exception {
		System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		System.out.println("Chrome driver launched");

		// Maximize browser window
		driver.manage().window().maximize();

		// launch the url
		driver.get(url);

		// Get excel file
		FileInputStream fis = new FileInputStream("E:\\information.xlsx");

		XSSFWorkbook wb = new XSSFWorkbook(fis);

		// Get sheet from workbook by index
		XSSFSheet sheet = wb.getSheet("Information");

		// count the total number of rows present in sheet
		int rowcount = sheet.getLastRowNum();
		System.out.println("Total number of rows present in excel are:" + rowcount);

		// count the total number of coloumn present in sheet
		int columncount = sheet.getRow(0).getLastCellNum();
		System.out.println("Total number of col present in excel are:" + columncount);

		// click on signin
		driver.findElement(By.xpath("//a[@class='login']")).click();

		Thread.sleep(3000);

		for (int i = 1; i <= rowcount; i++) {
			// enter email
			driver.findElement(By.xpath("//input[@id='email']"))
					.sendKeys(sheet.getRow(i).getCell(0).getStringCellValue());

			// enter password
			driver.findElement(By.xpath("//input[@id='passwd']"))
					.sendKeys(sheet.getRow(i).getCell(3).getStringCellValue());

			// Click on sign in
			driver.findElement(By.xpath("//button[@id='SubmitLogin']")).click();

			// wait for some time to load page
			driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

			// click on list T-shirt
			driver.findElement(By.xpath("//div[@id='block_top_menu']/ul/li[3]/a[@title='T-shirts']")).click();

			// geting the faded short T-shirt string from blocks

			String T_shirt = driver
					.findElement(By.xpath("//div[@class='right-block']/h5/a[@title='Faded Short Sleeve T-shirts']"))
					.getText();
			System.out.println("Tshirt name which you selected is: " + T_shirt);

			// wait for some time to load page
			driver.manage().timeouts().pageLoadTimeout(6, TimeUnit.SECONDS);

			// mouse hover to t-shirt
			Actions actions = new Actions(driver);
			WebElement we = driver.findElement(By.xpath("//a[@title='Faded Short Sleeve T-shirts']/img"));
			actions.moveToElement(we).build().perform();

			// click on add to cart
			driver.findElement(By.xpath("//a[@title='Add to cart']/span[.='Add to cart']")).click();

			// wait for some time to load page
			driver.manage().timeouts().pageLoadTimeout(3, TimeUnit.SECONDS);

			// capture total product value
			String total_product = driver
					.findElement(By.xpath("//div[@id='layer_cart']/div[1]/div[2]/div[1]/strong[@class='dark']"))
					.getText();
			String total_product_price = driver
					.findElement(By.xpath("//div[@id='layer_cart']/div[1]/div[2]/div[1]/span")).getText();

			System.out.println(total_product + " : " + total_product_price);

			// capture total shipping value
			String total_shipping = driver
					.findElement(By.xpath("//div[@id='layer_cart']/div[1]/div[2]/div[2]/strong[@class='dark']"))
					.getText();
			String total_shipping_price = driver
					.findElement(By.xpath("//div[@id='layer_cart']/div[1]/div[2]/div[2]/span")).getText();
			System.out.println(total_shipping + " : " + total_shipping_price);

			// capture total value
			String total = driver
					.findElement(By.xpath("//div[@id='layer_cart']/div[1]/div[2]/div[3]/strong[@class='dark']"))
					.getText();
			String total_price = driver.findElement(By.xpath("//div[@id='layer_cart']/div[1]/div[2]/div[3]/span"))
					.getText();
			System.out.println(total + " : " + total_price);

			// taking screenshot of product description while proceed to chechkout
			this.takeSnapShot(driver, "E:\\test.png");

			// clicking on proceed to checkout after taking product decription screen shot
			driver.findElement(By.xpath("//a[@title='Proceed to checkout']")).click();

			// wait for some time to load page
			driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);

			// clicking on proceed to checkout after delivery address viewing
			driver.findElement(By.xpath("//p[@class='cart_navigation clearfix']/a[@title='Proceed to checkout']"))
					.click();

			// wait for some time to load page
			driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);

			// proceed to checkout after invoice and delivery address
			driver.findElement(By.xpath("//button[@name='processAddress']")).click();

			// wait for some time to load page
			driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);

			// click on the term and condition checkbox
			driver.findElement(By.xpath("//input[@id='cgv']")).click();

			// click on the proceed for shipping
			driver.findElement(By.xpath("//button[@name='processCarrier']")).click();

			// wait for some time to load page
			driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);

			// click on pay by bank wire
			driver.findElement(By.xpath("//a[@title='Pay by bank wire']")).click();

			// wait for some time to load page
			driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);

			// confirming the order button
			driver.findElement(By.xpath("//button/span[.='I confirm my order']")).click();

			// wait for some time to load page
			driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);

			// taking screenshot of order reference page
			this.takeSnapShot(driver, "E:\\order.png");

			// click on signout
			driver.findElement(By.xpath("//div/a[@class='logout']")).click();

			// verification
			// url verification
			String expectedSignoutUrl = "http://automationpractice.com/index.php?controller=authentication&back=history";
			String actualSigoutUrl = driver.getCurrentUrl();
			Assert.assertEquals(actualSigoutUrl, expectedSignoutUrl,
					"Actual Signout page url is not the same as expected Signout Page");
		}
		driver.quit();

	}

	public static void takeSnapShot(WebDriver webdriver, String fileWithPath) throws Exception {

		// Convert web driver object to TakeScreenshot

		TakesScreenshot scrShot = ((TakesScreenshot) webdriver);

		// Call getScreenshotAs method to create image file

		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

		// Move image file to new destination

		File DestFile = new File(fileWithPath);

		// Copy file at destination
		Files.copy(SrcFile, DestFile);

	}
}
