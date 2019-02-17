package elements;

import org.openqa.selenium.WebElement;
import wrappers.Element;

public class TextArea extends Element implements WebElement {

    public TextArea(WebElement webElement) {
        super(webElement);
    }

    public void sendKeys(int limitChars, CharSequence... arg0) {
        //TODO подумати над реалізацією
        //Assert.assertFalse(limitChars < arg0[0].length(),"Too mach symbols");
        super.sendKeys(arg0);
    }

    public String getEmail(){
        return super.getAttribute("email");
    }

    public String getValue(){
        return super.getAttribute("value");
    }

}
