package Asserts;

import Pages.GmailHomePage;
import models.Letter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DriverManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Browser.*;

public class MessageAsserter {

    private static Logger logger = LogManager.getLogger(MessageAsserter.class);
    private WebDriver driver = DriverManager.getDriver(CHROME);
    private WebDriverWait wait = new WebDriverWait(driver, 10);
    private GmailHomePage gmailHomePage = new GmailHomePage(driver);

    public void assertLetterSent() {
        assertNotNull(wait.until(ExpectedConditions.
                visibilityOf(gmailHomePage.getIsSentLabel())), "The letter was not sent");
    }

    public void assertMessageFieldsValid(Letter letter) {
        logger.info("Validate message fields");
        assertTrue(gmailHomePage.getLetter().checkMessageFields(letter), "Not all fields are valid");
    }

    public void assertLetterMovedToSpam() {
        assertTrue(gmailHomePage.getMessageCollection().isEmpty());
    }
}
