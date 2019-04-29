package Pages;

import elements.Button;
import elements.TextInput;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import wrappers.CustomFieldDecorator;

public class GmailLoginPage {

    @FindBy(css = "input[type=\'email\']")
    private TextInput loginInput;

    @FindBy(xpath = "//div[@class=\'dG5hZc\']//span")
    private Button nextButton;

    public GmailLoginPage(WebDriver driver){
        PageFactory.initElements(new CustomFieldDecorator(driver),this);
    }

    public void typeLoginAndSubmit (WebDriver driver, String login){
        loginInput.sendKeys(login);
        nextButton.safeClick(driver);
    }
}
