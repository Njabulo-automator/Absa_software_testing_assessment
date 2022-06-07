package Utilities;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
//import za.co.absa.functionstechnology.automation.selenium.LocatorUtil;

public class CommonUtil {
    //static Logger LOG = Logger.getLogger(za.co.absa.functionstechnology.automation.selenium.CommonUtil.class);
    private static final Logger LOG = LogManager.getLogger(CommonUtil.class);
    WebDriver driver;
    public static final String SCREENSHOTSPATH_PROPERTY_NAME = "webdriver.screenshotspath";
    public static final String SCREENSHOTSPATH_PROPERTY_DEFAULT_VALUE = "screenshots";
    int a;


    public CommonUtil(WebDriver driver) {
        this.driver = driver;
    }

    String FileName_Value;
    String Split_Value;


    public void clickOnElement(String element) {
        try {
            By object = LocatorUtil.getLocator(element);
            this.driver.findElement(object).click();
        } catch (Exception var3) {
            LOG.info("Unable to catch object see error" + var3.getMessage());
        }

    }

    public String GetCurrentUrl(){
        String url =driver.getCurrentUrl();

        return url;
    }


    public WebElement waitForElement(String element) throws Exception {

        try {
            By object = LocatorUtil.getLocator(element);
            //this.driver.findElement(object).click();

            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement Element1 = wait.until(ExpectedConditions.visibilityOf(this.driver.findElement(object)));
            return Element1;
        } catch (Exception e) {
            LOG.info("Unable to catch object see error" + e.getMessage());
            return null;
        }

    }


    public void clickOnElement1_or_Element2(String element1, String element2) throws Exception {
        boolean isElementPresent = true;

        try {
            By object = LocatorUtil.getLocator(element1);
            this.driver.findElement(object).click();
        } catch (Exception var3) {
            isElementPresent = false;
        }
        if (isElementPresent == false) {
            By object2 = LocatorUtil.getLocator(element2);
            this.driver.findElement(object2).click();
        }

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

    public void typeOnElement(String element, String value) {
        try {
            By object = LocatorUtil.getLocator(element);
            this.driver.findElement(object).sendKeys(new CharSequence[]{value});
        } catch (Exception var4) {
            LOG.info("Unable to find object: " + element.toString());
        }

    }

    public void typeOnElement_Control_All(String element, String value) {
        try {
            By object = LocatorUtil.getLocator(element);
            this.driver.findElement(object).sendKeys(Keys.CONTROL + "a");
            this.driver.findElement(object).sendKeys(value);
        } catch (Exception var4) {
            LOG.info("Unable to find object: " + element.toString());
        }

    }


    public boolean isPresent(String element) {
        try {
            By object = LocatorUtil.getLocator(element);
            this.driver.findElement(object).isDisplayed();
            return true;
        } catch (Exception var3) {
            LOG.info("Unable to find object: " + element.toString());
            return false;
        }
    }

    public String getText(String element) {
        try {
            By object = LocatorUtil.getLocator(element);
            return this.driver.findElement(object).getText();
        } catch (Exception var3) {
            LOG.info("Unable to find object: " + element.toString());
            return "Unable to find text";
        }
    }

    public boolean isEnabled(String element) {
        try {
            By object = LocatorUtil.getLocator(element);
            return this.driver.findElement(object).isEnabled();
        } catch (Exception var3) {
            LOG.info("Unable to find object: " + element.toString());
            return false;
        }
    }

    public boolean isTextVisible(String element, String value) {
        try {
            String text = this.getText(element);
            return text.toUpperCase().contains(value);
        } catch (Exception var4) {
            return false;
        }
    }

    public void clear(String element) {
        try {
            By object = LocatorUtil.getLocator(element);
            this.driver.findElement(object).clear();
        } catch (Exception var3) {
            LOG.info("Unable to find object: " + element.toString());
        }

    }

    public void selectFromList(String list, String value) {
        try {
            By listObject = LocatorUtil.getLocator(list);
            (new Select(this.driver.findElement(listObject))).selectByVisibleText(value);
        } catch (Exception var4) {
            LOG.info("Unable to find object: " + list.toString());
        }

    }

    public String captureScreenshot(String fileName) throws IOException {
        File src = (File) ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);
        String filePath = getScreenshotsPath();
        if ((new File(filePath + File.separator + fileName)).exists() || (new File(filePath + File.separator + fileName + ".png")).exists()) {
            fileName = fileName + "_" + System.currentTimeMillis();
        }

        fileName = FilenameUtils.getExtension(fileName).isEmpty() ? fileName + ".png" : fileName;
        filePath = getScreenshotsPath() + File.separator + fileName;

        try {
            FileUtils.copyFile(src, new File(filePath));
            return filePath;
        } catch (IOException var5) {
            throw new IOException("Failed to save screenshot to " + filePath, var5);
        }
    }

    private static String getScreenshotsPath() {
        String workingDirectory = System.getProperty("user.dir");
        String screenshotPath = System.getProperty("webdriver.screenshotspath");
        if (screenshotPath == null) {
            screenshotPath = "screenshots";
        }

        return workingDirectory + File.separator + screenshotPath;
    }

    public void clickOnElementByText_NoException(String element, String value) throws Exception {
        try {
            if (element.contains("{value}")) {
                element = element.replace("{value}", value);
            }
            Actions actions = new Actions(driver);
            By object = LocatorUtil.getLocator(element);
            actions.moveToElement(this.driver.findElement(object));
            actions.perform();
            this.driver.findElement(object).click();
        } catch (Exception var3) {
            LOG.info("Unable to catch object see error" + var3.getMessage());
        }

    }


    public void secondsDelay(int sec) {
        int timeInMilliSeconds;
        try {
            timeInMilliSeconds = sec * 1000;
//            LOG.info("Going for  "+ timeInMilliSeconds + "delay" );

            Thread.sleep(timeInMilliSeconds);
        } catch (Exception e) {
            LOG.info(Arrays.toString(e.getStackTrace()));
        }
    }


    public void Explicitwait_loadingscreen(String element) throws Exception {
        try {
            By object = LocatorUtil.getLocator(element);
            boolean Display = this.driver.findElement(object).isDisplayed();
            // System.out.println("Display value is: "+Display);
            String s1 = Boolean.toString(Display);
            if (s1.equalsIgnoreCase("true")) {
                //  System.out.println("Display inside value is: " + Display);
                //Thread.sleep(1000);
                Explicitwait_loadingscreen(element);
            }
            //Boolean Display = driver.findElement(By.xpath("//label[contains(text(),'Active loading indicator')]")).isDisplayed();
        } catch (Exception var3) {
            LOG.info("Unable to find object: " + element.toString());
        }
    }

    public void Dropdown_Select(String element1, String element2, String Value) throws InterruptedException {
        By object1 = null;
        a = 0;
        first:
        try {
            object1 = LocatorUtil.getLocator(element1);
            By object2 = LocatorUtil.getLocator(element2);
            this.driver.findElement(object1).click();
            //  this.driver.findElement(object1).clear();
            this.driver.findElement(object1).sendKeys(Keys.CONTROL + "a");
            this.driver.findElement(object1).sendKeys(Keys.DELETE);
            this.driver.findElement(object1).sendKeys(new CharSequence[]{Value});
            Thread.sleep(2000);
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_DOWN);
            //By object = LocatorUtil.getLocator(element2);
            String S1 = this.driver.findElement(object2).getText();

            if (S1.contains(Value)) {
                System.out.println("S1: " + S1);
                robot.keyPress(KeyEvent.VK_ENTER);
                a = 2;
            } else if (a == 2) {
                break first;
            } else {
                a++;
                Thread.sleep(1000);
                Dropdown_Select(element1, element2, Value);
            }
        } catch (Exception var3) {
            this.driver.findElement(object1).sendKeys(Keys.CONTROL + "a");
            this.driver.findElement(object1).sendKeys(Keys.DELETE);
            Thread.sleep(1000);
            a++;
            // System.out.println("a: " + a);
            LOG.info("Unable to find object: " + element2.toString());
            if (a == 2) {
                break first;
            } else {
                Dropdown_Select(element1, element2, Value);
            }
        }
    }

    public void Explicitwait_Clickable(String element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            String[] arrSplit = element.split("\\|\\|");
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(arrSplit[1])));
        } catch (Exception var3) {
            LOG.info("Unable to catch object see error" + var3.getMessage());
        }
    }


    public String Get_Text(String element) {
        String Actual_Text = "";
        try {
            String[] arrSplit = element.split("\\|\\|");
            WebElement TxtBoxContent = driver.findElement(By.xpath(arrSplit[1]));
            Actual_Text = TxtBoxContent.getText();
            return Actual_Text;

        } catch (Exception var3) {
            LOG.info("Unable to catch object see error" + var3.getMessage());
            return Actual_Text;
        }

    }

    public void scroll(String element) {
        try {
            By object = LocatorUtil.getLocator(element);
            JavascriptExecutor jse = (JavascriptExecutor) driver;

            jse.executeScript("arguments[0].scrollIntoView();", driver.findElement(object));
        } catch (Exception var3) {
            LOG.info("Unable to catch object see error" + var3.getMessage());
        }

    }

    public String ReturnedFile() {

        String filename = null;
        //String filePath="C:\\Users\\ab015b2\\Downloads\\";
        File dir = new File("C:/Users/ab015b2/Downloads");
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith("Export-Unit_of_Measure");
            }
        };
        String[] children = dir.list(filter);
        if (children == null) {
            System.out.println("Either dir does not exist or is not a directory");
            return "File not found";
        } else {
            //for (int i = 0; i< children.length; i++) {
            filename = children[0];
            //filename=filePath+filename;
            //System.out.println(filename);
            //}
        }
        return filename;
    }

    public void waitForFileToDownload() throws IOException, InterruptedException {
        String downloadPath = "C:/Users/ab015b2/Downloads";
        File dir = new File(downloadPath);
        File[] dirContents = dir.listFiles();

        for (int i = 0; i < dirContents.length; i++) {
            if (dirContents[i].getName().contains("Export-Unit_of_Measure-") || dirContents[i].getName().contains("Export-Risk_Event")) {
                break;
            } else {
                Thread.sleep(30000);
                System.out.println("No file in the download directory");

            }
        }
    }

    public String Filename_Return(String element, String Filename) {
        try {
            File directoryPath = new File(element);
            //List of all files and directories
            String contents[] = directoryPath.list();
            // System.out.println("List of files and directories in the specified directory:");
            for (int i = 0; i < contents.length; i++) {
                //System.out.println("File Name is:"+contents[i]);
                if (contents[i].contains(Filename)) {
                    //System.out.println("Present");
                    FileName_Value = contents[i];
                    break;
                }
            }
        } catch (Exception var3) {
            LOG.info("Unable to catch object see error" + var3.getMessage());
        }
        return FileName_Value;
    }

    //mover downloaded file from download folder to data folder
    public void MoveFile(String fileName) throws IOException {


        // Download directory
        File downloaded = new File("C:/Users/ab015b2/Downloads/" + fileName);
        //location destination directory
        File refile = new File("src/test/resources/Test Data/Unit_of_Measure_Report.xls");

        FileUtils.copyFile(downloaded, refile);
    }

    //mover downloaded file from download folder to data folder
    public void MoveRiskFile(String fileName) throws IOException {


        // Download directory
        File downloaded = new File("C:/Users/ab015b2/Downloads/" + fileName);
        //location destination directory
        File refile = new File("src/test/resources/Test Data/Export-Risk_Event.xls");

        FileUtils.copyFile(downloaded, refile);
    }

    public void delete_report_files(String downloadDir) {

        File path = new File(downloadDir);
        File[] files = path.listFiles();
        for (File file : files) {
            System.out.println("Deleted filename :" + file.getName());
            file.delete();
        }
    }

    public void Dateformat(String date) throws ParseException {

        //Date date = new Date();

        //Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = dateFormat.format(date);
        System.out.println("Converted String: " + strDate);
    }

    public String convertDateToString(String sDate2) throws ParseException {
        //Date date = new Date();

        SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        Date date2 = formatter2.parse(sDate2);

        DateFormat StringDate = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a z");
        //StringDate.setTimeZone(TimeZone.getTimeZone("CET"));
        String strDate = StringDate.format(date2);
        return strDate;
    }

    public String convertDateOnlyToString_(String sDate2) throws ParseException {
        //Date date = new Date();

        SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yyyy");
        Date date2 = formatter2.parse(sDate2);

        DateFormat StringDate = new SimpleDateFormat("MMM dd, yyyy");
        //StringDate.setTimeZone(TimeZone.getTimeZone("CET"));
        String strDate = StringDate.format(date2);
        return strDate;
    }

    public String splitDate1(String date) {


            String[] arrSplit_3 = date.split("CAT");
        String date1 = null;

        for (int i = 0; i < arrSplit_3.length; i++) {
                date1 = arrSplit_3[i];
            }
        return date1;
    }

    public String splitDate2(String date) {


        String[] arrSplit_3 = date.split("CAT");
        String date1 = null;

        for (int i = 0; i < arrSplit_3.length; i++) {
            date1 = arrSplit_3[i];
        }
        return date1;
    }
}
