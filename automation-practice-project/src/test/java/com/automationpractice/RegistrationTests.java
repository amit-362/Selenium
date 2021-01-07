package com.automationpractice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegistrationTests {

	public final String url = "http://automationpractice.com/index.php";

	@Test
	public void registrationTest() throws Exception {
		System.out.println("Starting the Program");
		// create Driver
		System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		System.out.println("Chrome driver launched");

		// Maximize browser window
		driver.manage().window().maximize();

		// launch the url
		driver.get(url);

		// Get
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
		sleep(3000);

		for (int i = 1; i <= rowcount; i++) {
			// Enter email id
			driver.findElement(By.xpath("//input[@id='email_create']"))
					.sendKeys(sheet.getRow(i).getCell(0).getStringCellValue());

			// click create an account
			driver.findElement(By.xpath("//button[@id='SubmitCreate']")).click();

			// verification
			// url verification
			String expectedUrl = "http://automationpractice.com/index.php?controller=authentication&back=my-account";
			String actualUrl = driver.getCurrentUrl();
			Assert.assertEquals(actualUrl, expectedUrl, "Actual page url is not the same as expected");

			// Personal information section

			// enter first name
			System.out.println("start filling information");

			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html//input[@id='customer_firstname']")));
			// xpath("/html//input[@id='customer_firstname']")
			driver.findElement(By.xpath("/html//input[@id='customer_firstname']"))
					.sendKeys(sheet.getRow(i).getCell(1).getStringCellValue());

			// enter last name
			driver.findElement(By.xpath("//input[@id='customer_lastname']"))
					.sendKeys(sheet.getRow(i).getCell(2).getStringCellValue());

			// enter password
			driver.findElement(By.xpath("//input[@id='passwd']"))
					.sendKeys(sheet.getRow(i).getCell(3).getStringCellValue());

			// Address Section
			// enter first name
			driver.findElement(By.xpath("//input[@id='firstname']"))
					.sendKeys(sheet.getRow(i).getCell(1).getStringCellValue());

			// enter last name
			driver.findElement(By.xpath("//input[@id='lastname']"))
					.sendKeys(sheet.getRow(i).getCell(2).getStringCellValue());

			// enter Address
			driver.findElement(By.xpath("//input[@id='address1']"))
					.sendKeys(sheet.getRow(i).getCell(4).getStringCellValue());

			// enter city
			driver.findElement(By.xpath("//input[@id='city']"))
					.sendKeys(sheet.getRow(i).getCell(5).getStringCellValue());

			// enter state
			/// html//select[@id='id_state']
			Select se = new Select(driver.findElement(By.xpath("/html//select[@id='id_state']")));
			se.selectByValue("2");

			// enter ZipCode
			driver.findElement(By.xpath("//input[@id='postcode']"))
					.sendKeys("" + ((int) sheet.getRow(i).getCell(6).getNumericCellValue()));

			// enter Mobile number
			driver.findElement(By.xpath("//input[@id='phone_mobile']"))
					.sendKeys("" + ((int) sheet.getRow(i).getCell(8).getNumericCellValue()));

			// Click Registation Button
			driver.findElement(By.xpath("//button[@id='submitAccount']")).click();

			// verification
			// url verifivation
			String expectedLoginUrl = "http://automationpractice.com/index.php?controller=my-account";
			String actualLoginUrl = driver.getCurrentUrl();
			Assert.assertEquals(actualLoginUrl, expectedLoginUrl, "Actual page url is not the same as expected");

			// signout button verification
			WebElement logOutButton = driver.findElement(By.xpath("//a[@class='logout']"));
			Assert.assertTrue(logOutButton.isDisplayed(), "Logout button is not visible");

			// click on Signout
			driver.findElement(By.xpath("//a[@class='logout']")).click();

			// verification
			// url verification
			String expectedSignoutUrl = "http://automationpractice.com/index.php?controller=authentication&back=my-account";
			String actualSigoutUrl = driver.getCurrentUrl();
			Assert.assertEquals(actualSigoutUrl, expectedSignoutUrl,
					"Actual Signout page url is not the same as expected Signout Page");

			// console result of user registration
			System.out.println("User Registration succesfully: " + sheet.getRow(i).getCell(1).getStringCellValue() + " "
					+ sheet.getRow(i).getCell(2).getStringCellValue());
		}
		// close the driver
		driver.quit();
	}

	private void sleep(long m) {
		try {
			Thread.sleep(m);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
