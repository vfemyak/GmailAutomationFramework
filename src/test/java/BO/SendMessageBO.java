package BO;

import POM.GmailHomePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SendMessageBO {

    private GmailHomePage gmailHomePage;
    private static Logger logger = LogManager.getLogger(SendMessageBO.class);

    public SendMessageBO (WebDriver driver){
        gmailHomePage = new GmailHomePage(driver);
    }

    public void sendMessage (WebDriver driver, String to, String subject, String message){
        writeLetter(driver,to,subject,message);
        gmailHomePage.sendMessage();
        logger.info("Message has been sent");
    }

    public void writeMessageAndClose (WebDriver driver, String to, String subject, String message){
        writeLetter(driver,to,subject,message);
        gmailHomePage.saveMessage();
        (new WebDriverWait(driver,10))      //waiting for closing letter form
                .until(ExpectedConditions.invisibilityOfElementLocated
                        (By.xpath("//td[@class=\'Hm\']/img[@class=\'Ha\']")));
        logger.info("Message has been closed");
    }

    public void writeLetter (WebDriver driver, String to, String subject, String message){
        gmailHomePage.clickCompose();
        (new WebDriverWait(driver,10))      //waiting for opening letter form
                .until(ExpectedConditions.visibilityOfElementLocated
                        (By.xpath("//td[@class=\'Hm\']/img[@class=\'Ha\']")));
        gmailHomePage.writeLetter(to,subject,message);
        logger.info("Message has been written");
    }

    public void checkAndSendDraftMessage(WebDriver driver, String to, String subject, String message){
        gmailHomePage.openDraftFolder();
        logger.info("Draft folder has been opened");
        gmailHomePage.openDraftMessage();
        (new WebDriverWait(driver,10))      //waiting for opening letter form
                .until(ExpectedConditions.visibilityOfElementLocated
                        (By.xpath("//table[@class=\'IZ\']/descendant::*[@role=\'button\']")));
        gmailHomePage.checkMessageFields(to, subject, message);
        logger.info("Message has been checked");
        gmailHomePage.sendMessage();
        logger.info("Message has been sent");
    }
}
