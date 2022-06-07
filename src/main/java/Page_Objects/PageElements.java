package Page_Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.lang.String;

public class PageElements {
    WebDriver driver;

    public PageElements(WebDriver driver1) {
        this.driver = driver1;
        PageFactory.initElements(driver, this);

    }



}
