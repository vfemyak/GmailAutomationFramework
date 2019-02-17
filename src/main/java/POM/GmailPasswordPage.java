package POM;

import elements.Button;
import elements.TextInput;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import wrappers.CustomFieldDecorator;

public class GmailPasswordPage {

    @FindBy(css = "input[type=\'password\']")
    private TextInput passwordInput;

    @FindBy(xpath = "//div[@class=\'dG5hZc\']//span")
    private Button nextButton;

    public GmailPasswordPage(WebDriver driver){
        PageFactory.initElements(new CustomFieldDecorator(driver),this);
    }

    public void typePasswordAndSubmit (String password){
        passwordInput.sendKeys(password);
        nextButton.click();
    }

    public TextInput getPasswordInput() {
        return passwordInput;
    }
}

//todo не передавати driver в конструктор