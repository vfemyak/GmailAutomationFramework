package elements;

import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import wrappers.Element;

public class Button extends Element {

    public Button(WebElement webElement) {
        super(webElement);
    }

    @Override
    public void sendKeys(CharSequence... arg0) {
        System.out.println("You cannot write smth to button");
    }

    public boolean isClicable(WebDriver driver){
        boolean isClickable = true;
        try {
            (new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(webElement));
        } catch (Exception ex) {
            isClickable = false;
        }
        return isClickable;
    }

    public void safeClick(WebDriver driver) throws ElementNotInteractableException{
        if (isClicable(driver)) {
            super.click();
        }
        else System.out.println("This element is not interactable");
    }

    public boolean isVisible(){
        if (this.isSelected())
            return true;
        else return false;
    }
}
