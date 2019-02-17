package elements;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import wrappers.Element;
import wrappers.IElement;

public class TextInput extends Element implements WebElement {

    public TextInput(WebElement webElement) {
        super(webElement);
    }

    public void sendKeys(int limitChars, CharSequence... arg0) {
        //TODO
        //Assert.assertFalse(limitChars < arg0[0].length(),"Too mach symbols");
        super.sendKeys(arg0);
    }

}
