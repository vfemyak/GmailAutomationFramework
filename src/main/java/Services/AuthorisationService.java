package Services;

import Pages.GmailLoginPage;
import Pages.GmailPasswordPage;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AuthorisationService {

    private GmailLoginPage loginPage;
    private GmailPasswordPage passwordPage;

    private static Logger logger = LogManager.getLogger(AuthorisationService.class);

    private WebDriver driver;
    private WebDriverWait wait;

    public AuthorisationService(WebDriver driver) {
        loginPage = new GmailLoginPage(driver);
        passwordPage = new GmailPasswordPage(driver);
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    @Step("Successful authorization")
    public void logIn(String name, String password) {
        loginPage.typeLoginAndSubmit(driver, name);
        wait.until(ExpectedConditions.visibilityOf(passwordPage.getPasswordInput()));   //waiting for next page

        passwordPage.typePasswordAndSubmit(password);
        wait.until(driver1 -> driver1.getCurrentUrl().toLowerCase().contains("inbox")); //waiting for home page

        logger.info("Successful authorization");
    }

}
