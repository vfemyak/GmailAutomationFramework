package elements;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import wrappers.Element;

public class TextInput extends Element {

    public TextInput(WebElement webElement) {
        super(webElement);
    }

    public void sendKeys(int limitChars, CharSequence... arg0) {
        Assert.assertFalse(limitChars < arg0[0].length(),"Too mach symbols");
        super.sendKeys(arg0);
    }


}
