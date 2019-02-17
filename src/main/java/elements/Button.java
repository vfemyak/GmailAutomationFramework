package elements;

import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import wrappers.Element;

public class Button extends Element implements WebElement {

    public Button(WebElement webElement) {
        super(webElement);
    }

    @Override
    public void sendKeys(CharSequence... arg0) {
        System.out.println("You cannot write smth to button");
    }

    public boolean isClickable(WebDriver driver){
        try {
            (new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(webElement));
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public void safeClick(WebDriver driver) throws ElementNotInteractableException{
        if (isClickable(driver)) {
            super.click();
        }
        else System.out.println("This element is not interactable");
    }
}
