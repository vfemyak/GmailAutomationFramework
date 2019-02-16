package BO;

import POM.GmailHomePage;

import models.Letter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SendMessageBO {

    private GmailHomePage gmailHomePage;
    private static Logger logger = LogManager.getLogger(SendMessageBO.class);

    private WebDriver driver;
    private Letter letter;

    public SendMessageBO (WebDriver driver, Letter letter){
        gmailHomePage = new GmailHomePage(driver);
        this.driver = driver;
        this.letter = letter;
    }

    public void sendMessage (){
        writeLetter(letter);
        gmailHomePage.sendMessage();
        logger.info("Message has been sent");
    }

    public void writeMessageAndClose (){
        writeLetter(letter);
        gmailHomePage.saveMessage();
        new WebDriverWait(driver,10)      //waiting for closing letter form
                .until(ExpectedConditions.invisibilityOfElementLocated
                        (By.xpath("//td[@class=\'Hm\']/img[@class=\'Ha\']")));
        logger.info("Message has been closed");
    }

    public void writeLetter (Letter letter){
        gmailHomePage.clickCompose();
        new WebDriverWait(driver,10)      //waiting for opening letter form
                .until(ExpectedConditions.visibilityOfElementLocated
                        (By.xpath("//td[@class=\'Hm\']/img[@class=\'Ha\']")));
        gmailHomePage.writeLetter(letter);
        logger.info("Message has been written");
    }

    public void openDraftMessage(){
        gmailHomePage.openDraftFolder();
        logger.info("Draft folder has been opened");
        gmailHomePage.openDraftMessage();
        new WebDriverWait(driver,10)      //waiting for opening letter form
                .until(ExpectedConditions.visibilityOfElementLocated
                        (By.xpath("//table[@class=\'IZ\']/descendant::*[@role=\'button\']")));
    }

    public boolean isValidateFields(Letter letter){
        if(gmailHomePage.getLetter().checkFields(letter)) {
            logger.info("All fields are valid");
            return true;
        }
        else{
            logger.error("Test failed");
            return false;
        }
    }

    public void sendDraftLetter(){
        gmailHomePage.sendMessage();
        logger.info("Message has been sent");
    }
}
