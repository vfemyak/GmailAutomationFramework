package Actions;

import Pages.GmailHomePage;
import io.qameta.allure.Step;
import models.Letter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MessageService {

    private GmailHomePage gmailHomePage;

    private static Logger logger = LogManager.getLogger(MessageService.class);

    private WebDriver driver;
    private WebDriverWait wait;
    private Letter letter;          //contains letter fields

    public MessageService(WebDriver driver, Letter letter) {
        gmailHomePage = new GmailHomePage(driver);
        this.driver = driver;
        this.letter = letter;
        this.wait = new WebDriverWait(driver, 10);
    }

    @Step("Message has been closed")
    public void writeMessageAndClose() {
        writeLetter(letter);
        gmailHomePage.saveMessage();
        wait.until(ExpectedConditions.invisibilityOf(gmailHomePage.getMessageTextarea()));  //waiting for closing letter form
        logger.info("Message has been closed");
    }

    @Step("Message has been written")
    public void writeLetter(Letter letter) {
        gmailHomePage.clickCompose();
        wait.until(ExpectedConditions.visibilityOf(gmailHomePage.getSaveAndCloseButton()));  //waiting for opening letter form
        gmailHomePage.writeLetter(letter);
        logger.info("Message has been written");
    }

    @Step("Draft folder has been opened")
    public void openDraftMessage() {
        gmailHomePage.openDraftFolder();
        logger.info("Draft folder has been opened");
        gmailHomePage.openDraftMessage();
        wait.until(ExpectedConditions.elementToBeClickable(gmailHomePage.getSendButton()));  //waiting for opening letter form
    }

    @Step("Message has been sent")
    public void sendDraftLetter() {
        gmailHomePage.sendMessage();
        logger.info("Message has been sent");
    }
}
