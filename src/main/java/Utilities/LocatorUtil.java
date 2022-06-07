package Utilities;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LocatorUtil {
   // static Logger LOG = Logger.getLogger(za.co.absa.functionstechnology.automation.selenium.LocatorUtil.class);
   private static final Logger LOG = LogManager.getLogger(LocatorUtil.class);
    public LocatorUtil() {
    }


    public static By getLocator(String locator) throws Exception {
        String locatorType = locator.split("\\|\\|")[0];
        String locatorValue = locator.split("\\|\\|")[1];
        if (locatorType.toLowerCase().equals("id")) {
            return By.id(locatorValue);
        } else if (locatorType.toLowerCase().equals("name")) {
            return By.name(locatorValue);
        } else if (!locatorType.toLowerCase().equals("classname") && !locatorType.toLowerCase().equals("class")) {
            if (!locatorType.toLowerCase().equals("tagname") && !locatorType.toLowerCase().equals("tag")) {
                if (!locatorType.toLowerCase().equals("linktext") && !locatorType.toLowerCase().equals("link")) {
                    if (locatorType.toLowerCase().equals("partiallinktext")) {
                        return By.partialLinkText(locatorValue);
                    } else if (!locatorType.toLowerCase().equals("cssselector") && !locatorType.toLowerCase().equals("css")) {
                        if (locatorType.toLowerCase().equals("xpath")) {
                            return By.xpath(locatorValue);
                        } else {
                            LOG.info("Unable to find locator type");
                            throw new Exception("Unknown locator type '" + locatorType + "'");
                        }
                    } else {
                        return By.cssSelector(locatorValue);
                    }
                } else {
                    return By.linkText(locatorValue);
                }
            } else {
                return By.tagName(locatorValue);
            }
        } else {
            return By.className(locatorValue);
        }
    }

    public static void getLocatorWithiFrame(WebDriver driver, String locator) throws Exception {
        String iFrameType = locator.split("\\|\\|")[0];
        String iFrameValue = locator.split("\\|\\|")[1];
        if (iFrameType.toLowerCase().equals("index")) {
            driver.switchTo().frame(Integer.parseInt(iFrameValue));
        } else {
            driver.switchTo().frame(driver.findElement(getLocator(locator)));
        }

    }
}

