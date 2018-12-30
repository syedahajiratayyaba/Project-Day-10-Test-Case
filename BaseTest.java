package com.ibm.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ibm.pages.UserPage;
import com.ibm.utilities.DBUtil;
import com.ibm.utilities.ExcelReader;
import com.ibm.utilities.ExcelUtil;
import com.ibm.utilities.PropertiesFileHandler;

public class BaseTest extends ExcelReader{
	WebDriverWait wait;
	WebDriver driver;	 
    @Test()
    
    public void testcase10() throws InterruptedException, IOException, SQLException{
    	
    	FileInputStream file = new FileInputStream("./TestData/data.properties");
    	Properties prop = new Properties();
    	prop.load(file);
    	String url = prop.getProperty("url");
    	String username = prop.getProperty("user");
    	String password = prop.getProperty("password");
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 60);
		
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		Login login = new Login(driver, wait);
		driver.get(url);
		
		login.enterEmailAddress(username);
		login.enterPassword(password);
		login.clickOnLogin();
		
		WebElement marketingEle=driver.findElement(By.linkText("Marketing"));
		marketingEle.click();
		
		WebElement couponsEle=driver.findElement(By.linkText("Coupons"));
		couponsEle.click();
		int exp= DBUtil.countQuery("SELECT count(name) from as_coupons");
		System.out.println("COunt beofre adding coupon"+exp);
		Thread.sleep(3000);
		WebElement newCouponEle=driver.findElement(By.cssSelector("i.fa.fa-plus"));
		newCouponEle.click();
		
		WebElement couponName=driver.findElement(By.name("name"));
		couponName.sendKeys("Syeda");
		
		WebElement codeEle=driver.findElement(By.name("code"));
		codeEle.sendKeys("123");
		
		WebElement discountEle=driver.findElement(By.name("discount"));
		discountEle.sendKeys("5");
		
		Thread.sleep(3000);
		
		WebElement saveEle=driver.findElement(By.cssSelector("i.fa.fa-save"));
		saveEle.click();
		Thread.sleep(3000);
		
		int act=DBUtil.countQuery("SELECT count(name) from as_coupons");
		System.out.println("COunt after adding coupon"+act);
		
		Assert.assertEquals(act,exp+1);
		
		Thread.sleep(3000);
    }

}