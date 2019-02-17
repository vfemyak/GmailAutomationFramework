package elements;

import org.openqa.selenium.WebElement;
import wrappers.Element;

public class TextInput extends Element implements WebElement {

    public TextInput(WebElement webElement) {
        super(webElement);
    }

    public void sendKeys(int limitChars, CharSequence... arg0) {
    }

}
