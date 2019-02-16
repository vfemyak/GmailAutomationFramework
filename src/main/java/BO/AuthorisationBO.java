package BO;

import POM.GmailLoginPage;
import POM.GmailPasswordPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AuthorisationBO {

    private GmailLoginPage loginPage;
    private GmailPasswordPage passwordPage;
    private static Logger logger = LogManager.getLogger(AuthorisationBO.class);

    private WebDriver driver;

    public AuthorisationBO (WebDriver driver){
        loginPage = new GmailLoginPage(driver);
        passwordPage = new GmailPasswordPage(driver);
        this.driver = driver;
    }

    public void LogIn(String name, String password){
        loginPage.typeLoginAndSubmit(driver, name);

        new WebDriverWait(driver,10)      //waiting for next page
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type=\'password\']")));

        passwordPage.typePasswordAndSubmit(password);

        new WebDriverWait(driver,10)      //waiting for home page
                .until(ExpectedConditions.visibilityOfElementLocated
                        (By.xpath("//div[@class=\'z0\']/descendant::div[@role=\'button\']")));
        logger.info("Successful authorization");
    }

}
