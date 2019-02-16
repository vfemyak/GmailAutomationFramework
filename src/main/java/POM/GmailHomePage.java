package POM;

import elements.Button;
import elements.TextArea;
import models.Letter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import wrappers.CustomFieldDecorator;

public class GmailHomePage {

    private Letter letter = new Letter();

    @FindBy(xpath = "//div[@class=\'z0\']/descendant::div[@role=\'button\']")
    private Button composeButton;

    @FindBy(css = "div.wO textarea.vO")
    private TextArea toTextarea;

    @FindBy(xpath = "//div[@class='oL aDm az9']/span[@email]")
    private TextArea toTextareaCompare;

    @FindBy(xpath = "//input[@name=\'subjectbox\']")
    private TextArea subjectTextarea;

    @FindBy(xpath = "//table[@class=\'iN\']/descendant::*[@role=\'textbox\']")
    private TextArea messageTextarea;

    @FindBy(xpath = "//td[@class=\'Hm\']/img[@class=\'Ha\']")
    private Button saveAndCloseButton;

    @FindBy(css = "a[href=\'https://mail.google.com/mail/u/0/#drafts\']")
    private Button draftFolder;

    @FindBy(xpath = "//div[@gh=\'tl\']//tr[1]")     //select first draft
    private Button lastDraftMessage;

    @FindBy(xpath = "//table[@class=\'IZ\']/descendant::*[@role=\'button\']")
    private Button sendButton;

    public GmailHomePage(WebDriver driver){
        PageFactory.initElements(new CustomFieldDecorator(driver),this);
    }

    public void clickCompose(){
        composeButton.click();
    }

    public void writeLetter(Letter letter){
        toTextarea.sendKeys(20, letter.getTo());
        subjectTextarea.sendKeys(letter.getSubject());
        messageTextarea.sendKeys(letter.getMessage());
    }

    public void saveMessage(){
        saveAndCloseButton.click();
    }

    public void openDraftFolder(){
        draftFolder.click();
    }

    public void openDraftMessage(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lastDraftMessage.click();
    }
    public void sendMessage(){
        sendButton.click();
    }

    public Letter getLetter(){
        letter.setTo(toTextareaCompare.getEmail());
        letter.setSubject(subjectTextarea.getValue());
        letter.setMessage(messageTextarea.getText());

        return letter;
    }

        //TODO: перемістити асерти в тест
//        Assert.assertEquals(to,toTextareaCompare.getEmail());
//        Assert.assertEquals(subject,subjectTextarea.getValue());
//        Assert.assertEquals(message, messageTextarea.getText());
}
