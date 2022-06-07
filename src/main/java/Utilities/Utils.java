package Utilities;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;


public class Utils {
    WebDriver driver;
    CommonUtil commonUtil;
    //LoginObj loginPageObjects;
    CommonUtil element;
    public String username;
    public String password;
    public String baseurl;
    public String Value;
    public Utils utils;
    public ExcelUtils excelUtils;
    public WebdriverUtil webdriverUtil;
    public FileInputStream fis = null;
    public FileOutputStream fos = null;
    public XSSFWorkbook workbook = null;
    public XSSFSheet sheet = null;
    public XSSFRow row = null;
    public XSSFCell cell = null;
    String xlFilePath;
    String reportName;

    public Utils(WebDriver driver)  {
        this.driver = driver;
        commonUtil = new CommonUtil(driver);
       // loginPageObjects = new LoginObj();
        this.element = new CommonUtil(driver);
       // driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    }

    private static final Logger LOG = LogManager.getLogger(Utils.class);
    public WebElement DetectElement(String element) {
        WebElement Element = null;
        try {
            By object = LocatorUtil.getLocator(element);
            Element = driver.findElement(object);
        } catch (Exception e) {
            LOG.info("Unable to catch object see error" + e.getMessage());
        }
        return Element;
    }

    public void waitTillElementVisible(String element){
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until( ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
    }

    public String GetAttribute_Value(String element, String Att)
    {
        try
        {
            By object = LocatorUtil.getLocator(element);
            Value= this.driver.findElement(object).getAttribute(Att);

        } catch (Exception var3)
        {
            LOG.info("Unable to catch object see error" + var3.getMessage());
        }
        return Value;
    }

    public void clickOnElementByText(String element, String value) {
        try {
            if (element.contains("{value}")) {
                element = element.replace("{value}", value);
            }

            By object = LocatorUtil.getLocator(element);
            this.driver.findElement(object).click();
        } catch (Exception var4) {
            LOG.info("Unable to catch object see error" + var4.getMessage());
        }

    }


    public void ClickonElementwithJS(String element){
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        //Thread.sleep(1000);
    }

    public String captureScreenshot(String fileName) throws Exception {
        File scrFile = (File)((TakesScreenshot)this.driver).getScreenshotAs(OutputType.FILE);
        String encodedBase64 = null;
        FileInputStream fileInputStreamReader = null;
        try {
            fileInputStreamReader = new FileInputStream(scrFile);
            byte[] bytes = new byte[(int)scrFile.length()];
            fileInputStreamReader.read(bytes);
            encodedBase64 = new String( Base64.encodeBase64(bytes));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "data:image/png;base64,"+encodedBase64;
    }
    
    
    public static String takeSnapShot(WebDriver webdriver, String fileScreenName) throws Exception {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        timeStamp = timeStamp.replace(".", "_");
        String Screenshotpath;
//Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
//Call getScreenshotAs method to create image file
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
//Move image file to new destination
        Screenshotpath = System.getProperty("user.dir") + "\\Screenshots\\" + fileScreenName + timeStamp + ".png";
        File DestFile = new File(Screenshotpath);
//Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);
        return Screenshotpath;
    }

    private static String getScreenshotsPath1() {
        String workingDirectory = System.getProperty("user.dir");
        String screenshotPath = System.getProperty("webdriver.screenshotspath");
        if (screenshotPath == null) {
            screenshotPath = "Reports\\TestRunner";
        }

        return screenshotPath;
    }

    public static int getRandomId() {
        Random rand = new Random();
        return rand.nextInt(100000) + 1;
    }

    public void timeSelector(String editBoxClickElement, String editBoxInput, String value) {
        try {
            commonUtil.clickOnElement(editBoxClickElement);
            commonUtil.typeOnElement(editBoxInput, value);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void scrollDown() throws InterruptedException, AWTException {
        Robot r = new Robot(  );
        r.keyPress( KeyEvent.VK_DOWN );
        Thread.sleep( 2000 );
    }


    public void switchtoHometab() throws InterruptedException {
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0));
        Thread.sleep( 5000 );
    }



    public static void initiateExtentReport(String suiteName, Class currentClass) {
        if (suiteName != null && !suiteName.isEmpty()) {
           // ExtentReportManager.getInstance( TestRunner.class );

        } else {
            //ExtentReportManager.getInstance(currentClass);
        }
    }

    public String getSuiteName(ITestContext context) {
        if (context != null && context.getCurrentXmlTest() != null) {
            return context.getCurrentXmlTest().getSuite().getName();
        }
        return "";
    }
    public String captureScreenShotForDesktop(String fileName) throws Exception {

        Robot robot = new Robot();
        String filePath = getScreenshotsPath();
        if ((new File(filePath + File.separator + fileName)).exists() || (new File(filePath + File.separator + fileName + ".jpg")).exists()) {
            fileName = fileName + "_" + System.currentTimeMillis();
        }
        fileName = FilenameUtils.getExtension(fileName).isEmpty() ? fileName + ".png" : fileName;
        filePath = getScreenshotsPath() + File.separator + fileName;
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit()
                .getScreenSize());
        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
        ImageIO.write(screenFullImage, "png", new File(filePath));
        try {

            return filePath;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save screenshot to " + filePath, e);
        }
    }
    public  void MoveToElement(WebDriver driver,String element) {
        try {

            Actions actions = new Actions(driver);
            By object = LocatorUtil.getLocator(element);
            //this.driver.findElement(object).click();
            actions.moveToElement(this.driver.findElement(object));
            actions.perform();

        } catch (Exception var3) {
            LOG.info("Unable to catch object see error: " + var3.getMessage());
        }
    }

    public  void KeyBoard_Enter(WebDriver driver,String element) {
        try {

            Actions actions = new Actions(driver);
            By object = LocatorUtil.getLocator(element);
            //this.driver.findElement(object).click();
            actions.moveToElement(this.driver.findElement(object));
            actions.sendKeys(Keys.ARROW_DOWN).build().perform();

        } catch (Exception var3) {
            LOG.info("Unable to catch object see error: " + var3.getMessage());
        }



    }


    public boolean setCellData(String sheetName, String colName, int rowNum, String value)
    {
        try
        {
            int col_Num = -1;
            sheet = workbook.getSheet(sheetName);

            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals(colName))
                {
                    col_Num = i;
                }
            }

            sheet.autoSizeColumn(col_Num);
            row = sheet.getRow(rowNum - 1);
            if(row==null)
                row = sheet.createRow(rowNum - 1);

            cell = row.getCell(col_Num);
            if(cell == null)
                cell = row.createCell(col_Num);

            cell.setCellValue(value);

            fos = new FileOutputStream(xlFilePath);
            workbook.write(fos);
            fos.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return  false;
        }
        return true;
    }

    private static String getScreenshotsPath() {
        String workingDirectory = System.getProperty("user.dir");
        String screenshotPath = System.getProperty("webdriver.screenshotspath");
        if (screenshotPath == null) {
            screenshotPath = "screenshots";
        }

        return workingDirectory + File.separator + screenshotPath;
    }

    public void scrollUp() throws InterruptedException, AWTException {
        Robot r = new Robot(  );
        r.keyPress( KeyEvent.VK_UP );
        Thread.sleep( 2000 );
    }

    public String properties(String propKey)
    {

        String propVal = null;

        try
        {
            //Initializing an object of properties
            Properties prop = new Properties();
            //Create a Object of a Class
            FileInputStream input = new FileInputStream(System.getProperty("user.dir")+"\\resource\\UOM.properties");
            //Load properties file using the load method
            prop.load(input);
            //Calling the values from config.properties file
            propVal  = prop.getProperty(propKey);

        }
        catch(IOException e)
        {
            e.printStackTrace();
            return e.getMessage();
        }
        return propVal;

    }

}

