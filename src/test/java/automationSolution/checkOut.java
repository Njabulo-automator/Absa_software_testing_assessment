package automationSolution;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Utilities.*;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;



public class checkOut {
    WebDriver driver;
    public Properties AutoPropertiesFile = new Properties();
    public ExcelUtils ExcelUtils = new ExcelUtils();
    //public Test_Cases.Common_TestCases Common_TestCases;
    public InputStream input = null;
    public String[][] PW_testDataSheet;
    public String[][] testdata;
    static ExtentTest test;
    static ExtentReports report;
    public Utilities.Utils Utils;



@BeforeClass
    public void setUp() throws IOException {
        input = new FileInputStream(System.getProperty("user.dir")+ "\\src\\test\\resources\\selenium.properties");
        AutoPropertiesFile.load(input);
        testdata = ExcelUtils.readExcelDataFileToArray(AutoPropertiesFile.getProperty("Datafile"), "Credentials");
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss").format(new Date());
        report = new ExtentReports(System.getProperty("user.dir") + "\\Reports\\Checkout_Test" + timeStamp + ".html", false);
        Utils = new Utils(driver);
    }
    @AfterClass
    public void afterTest(){
    report.endTest(test);
    report.flush();

    }

    public static String takeSnapShot(WebDriver webdriver, String fileScreenName) throws Exception{
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        timeStamp = timeStamp.replace(".","_");
        String Screenshotpath;
        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot =((TakesScreenshot)webdriver);
        //Call getScreenshotAs method to create image file
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
        //Move image file to new destination
        Screenshotpath = System.getProperty("user.dir")+"\\Screenshots\\"+fileScreenName+timeStamp+".png";
        File DestFile=new File(Screenshotpath);
        //Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);
        return Screenshotpath;

    }
    public void startUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+ "\\Drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        String url = AutoPropertiesFile.getProperty("baseUrl");
        driver.get(url);
        takeSnapShot(driver, "Application launched");
    }


    public void logIn() throws Exception {
        test = report.startTest("Checkout from the shopping cart");
        //Captures username
        WebElement usernm = driver.findElement(By.name("user-name"));
        usernm.sendKeys(testdata[1][0]);
        //Captures password
        WebElement passwrd = driver.findElement(By.name("password"));
        passwrd.sendKeys(testdata[1][1]);
        //Click login button
        WebElement loginBtn = driver.findElement(By.name("login-button"));
        loginBtn.click();
        String currentUrl = driver.getCurrentUrl();
        String expectedCurrentUrl = "https://www.saucedemo.com/inventory.html";
        if (currentUrl.equalsIgnoreCase(expectedCurrentUrl)){
            test.log(LogStatus.PASS, "Login Screen");
        }
        else {
            test.log(LogStatus.FAIL, "Incorrect Login Screen");
        }
    }
    public void purchaseItem() throws Exception {
        takeSnapShot(driver, "HomePage");
        Thread.sleep(1000);
        //Add item 1 to the cart
        WebElement item1 = driver.findElement(By.name("add-to-cart-sauce-labs-backpack"));
        item1.click();
        Assert.assertEquals(driver.findElement(By.name("remove-sauce-labs-backpack")).getText(), "REMOVE");
        //Add item 2 to the cart
        Thread.sleep(1000);
        WebElement item2 = driver.findElement(By.name("add-to-cart-sauce-labs-bolt-t-shirt"));
        item2.click();
        Assert.assertEquals(driver.findElement(By.id("remove-sauce-labs-bolt-t-shirt")).getText(), "REMOVE");
    }
    public void checkOut() throws Exception {
        takeSnapShot(driver, "Checkout");
        WebElement cart = driver.findElement(By.id("shopping_cart_container"));
        cart.click();
        WebElement checkoutBtn = driver.findElement(By.name("checkout"));
        checkoutBtn.click();
    }
    public void custDetails() throws Exception {
        Assert.assertEquals(driver.findElement(By.className("title")).getText(), "CHECKOUT: YOUR INFORMATION");
        //Capture firstname
        WebElement frstname = driver.findElement(By.name("firstName"));
        frstname.sendKeys(testdata[1][2]);
        //Capture lastname
        WebElement lastname = driver.findElement(By.id("last-name"));
        lastname.sendKeys(testdata[1][3]);
        //Capture postal code
        WebElement zipcode = driver.findElement(By.name("postalCode"));
        zipcode.sendKeys(testdata[1][4]);
        //Click continue button
        takeSnapShot(driver, "CustomerDetails");
        WebElement contnueBtn = driver.findElement(By.name("continue"));
        contnueBtn.click();
    }
    public void verifyItems() throws Exception {
        Assert.assertEquals(driver.findElement(By.className("title")).getText(), "CHECKOUT: OVERVIEW");
        takeSnapShot(driver, "VerifyItems");
        DecimalFormat df = new DecimalFormat("$##.##");
        Double totalItems = df.parse(driver.findElement(By.xpath("//div[@class='cart_list']/div[3]/div[2]/div[2]/div")).getText()).doubleValue();
        Double totalItems2 = df.parse(driver.findElement(By.xpath("//div[@class='cart_item'][2]/div[2]/div[2]/div[1]")).getText()).doubleValue();
        String total = driver.findElement(By.xpath("//div[@class='summary_info']/div[5]")).getText();
        String total2 = total.replace("Item total: $", "");
        System.out.println(totalItems);
        Double sumOfItems = totalItems + totalItems2;
        String sum = String.valueOf(sumOfItems);
        Assert.assertEquals(total2, sum);
        if(total2.equalsIgnoreCase(sum)){
            test.log(LogStatus.PASS, "Price of items equal total", test.addBase64ScreenShot(takeSnapShot(driver, "Test2")));
        }
        else{
            test.log(LogStatus.FAIL, "Price of items does not equal to total");
        }
        System.out.println("Sum of items" +sumOfItems);
        System.out.println("Total"+total2);

        WebElement finishBtn = driver.findElement(By.name("finish"));
        finishBtn.click();
    }
    public void completeOrder() throws Exception {
        Assert.assertEquals(driver.findElement(By.className("title")).getText(), "CHECKOUT: COMPLETE!");
        takeSnapShot(driver, "OrderComplete");
        driver.quit();
    }
    @Test
    public void Execute() throws Exception {
        startUp();
        logIn();
        purchaseItem();
        checkOut();
        custDetails();
        verifyItems();
        completeOrder();
    }
}




