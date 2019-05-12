package elements;

import Actions.AuthorisationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import wrappers.Element;

public class TextArea extends Element implements WebElement {

    private static Logger logger = LogManager.getLogger(AuthorisationService.class);

    public TextArea(WebElement webElement) {
        super(webElement);
    }

    public void sendKeys(int limitChars, CharSequence... arg0) {

        int sumChars = 0;
        for (CharSequence cs: arg0)
            sumChars += cs.length();

        if (limitChars < sumChars)
            logger.error("Too mach symbols");
        else {
            super.sendKeys(arg0);
        }
    }

    public String getEmail(){
        return super.getAttribute("email");
    }

    public String getValue(){
        return super.getAttribute("value");
    }

}
