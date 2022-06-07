package Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebdriverUtil {
    String browser;
    String browserLocation;
    boolean acceptInsecureCerts;
    boolean ignoreSecurityDomain;
    String baseUrl;

    public WebdriverUtil(String baseUrl) throws IOException {
        Properties prop = new Properties();
        InputStream input = null;
        input = new FileInputStream("src/test/resources/selenium.properties");
        prop.load(input);
        this.browser = prop.getProperty("browser");
        this.acceptInsecureCerts = Boolean.parseBoolean(prop.getProperty("acceptInsecureCerts"));
        this.ignoreSecurityDomain = Boolean.parseBoolean(prop.getProperty("ignoreSecurityDomain"));
        this.baseUrl = baseUrl;
        String var4 = this.browser;
        byte var5 = -1;
        switch(var4.hashCode()) {
            case 2149:
                if (var4.equals("CH")) {
                    var5 = 1;
                }
                break;
            case 2240:
                if (var4.equals("FF")) {
                    var5 = 0;
                }
                break;
            case 2332:
                if (var4.equals("IE")) {
                    var5 = 2;
                }
        }

        switch(var5) {
            case 0:
                this.browserLocation = prop.getProperty("FFDriverLocation");
                break;
            case 1:
                this.browserLocation = prop.getProperty("CHDriverLocation");
                break;
            case 2:
                this.browserLocation = prop.getProperty("IEDriverLocation");
        }

    }

    public WebdriverUtil() throws IOException {
        Properties prop = new Properties();
        InputStream input = null;
        input = new FileInputStream("src/test/resources/selenium/selenium.properties");
        prop.load(input);
        this.browser = prop.getProperty("browser");
        this.acceptInsecureCerts = Boolean.parseBoolean(prop.getProperty("acceptInsecureCerts"));
        this.ignoreSecurityDomain = Boolean.parseBoolean(prop.getProperty("ignoreSecurityDomain"));
        String var3 = this.browser;
        byte var4 = -1;
        switch(var3.hashCode()) {
            case 2149:
                if (var3.equals("CH")) {
                    var4 = 1;
                }
                break;
            case 2240:
                if (var3.equals("FF")) {
                    var4 = 0;
                }
                break;
            case 2332:
                if (var3.equals("IE")) {
                    var4 = 2;
                }
        }

        switch(var4) {
            case 0:
                this.browserLocation = prop.getProperty("FFDriverLocation");
                break;
            case 1:
                this.browserLocation = prop.getProperty("CHDriverLocation");
                break;
            case 2:
                this.browserLocation = prop.getProperty("IEDriverLocation");
        }

    }

    public WebDriver setWebdriver() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("acceptInsecureCerts", this.acceptInsecureCerts);
        WebDriver driver = this.SelectBrowser(this.browser, capabilities);
        driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        return driver;
    }

    public WebDriver setWebdriverAndLaunch() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("acceptInsecureCerts", this.acceptInsecureCerts);
        WebDriver driver = this.SelectBrowser(this.browser, capabilities);
        driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
        driver.get(this.baseUrl);
        return driver;
    }



    public WebDriver SelectBrowser(String browser, DesiredCapabilities capabilities) {
        Object driver;
        if (!browser.equalsIgnoreCase("FF") && !browser.equalsIgnoreCase("Firefox")) {
            if (!browser.equalsIgnoreCase("IE") && !browser.equalsIgnoreCase("Internet explorer")) {
                if (!browser.equalsIgnoreCase("CH") && !browser.equalsIgnoreCase("Chrome")) {
                    driver = new FirefoxDriver(capabilities);
                } else {
                    System.setProperty("webdriver.chrome.driver", this.browserLocation);
                    driver = new ChromeDriver();
                }
            } else {
                System.setProperty("webdriver.ie.driver", this.browserLocation);
                capabilities.setCapability("ignoreProtectedModeSettings", this.ignoreSecurityDomain);
                driver = new InternetExplorerDriver(capabilities);
            }
        } else {
            System.setProperty("webdriver.gecko.driver", this.browserLocation);
            driver = new FirefoxDriver(capabilities);
        }

        return (WebDriver)driver;
    }

    public void launchBrwoser(WebDriver driver) {
        driver.get(this.baseUrl);
    }

    public void closeBrowser(WebDriver driver) {
        driver.close();
    }
}
