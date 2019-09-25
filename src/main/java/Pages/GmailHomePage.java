package Pages;

import elements.Button;
import elements.TextArea;
import elements.TextInput;
import models.Letter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import wrappers.CustomFieldDecorator;

import java.util.List;

public class GmailHomePage {

    private Letter letter = new Letter();
    private WebDriver driver;

    @FindBy(xpath = "//div[@class='z0']/descendant::div[@role='button']")
    private Button composeButton;

    @FindBy(css = "div.wO textarea.vO")
    private TextArea toTextarea;

    @FindBy(xpath = "//div[@class='oL aDm az9']/span[@email]")
    private TextArea toTextareaCompare;

    @FindBy(xpath = "//input[@name='subjectbox']")
    private TextArea subjectTextarea;

    @FindBy(xpath = "//table[@class='iN']/descendant::*[@role='textbox']")
    private TextArea messageTextarea;

    @FindBy(xpath = "//td[@class='Hm']/img[@class='Ha']")
    private Button saveAndCloseButton;

    @FindBy(css = "a[href='https://mail.google.com/mail/u/0/#drafts']")
    private Button draftFolder;

    @FindBy(xpath = "//div[@gh='tl']//tr[1]")     //select first draft
    private Button lastDraftMessage;

    @FindBy(xpath = "//table[@class='IZ']/descendant::*[@role='button']")
    private Button sendButton;

    @FindBy(xpath = "//*[@id='link_vsm']")
    private WebElement isSentLabel;

    @FindBy(xpath = "//*[@id='aso_search_form_anchor']//div//input")
    private TextInput searchInput;

    @FindBy(css = "button.gb_pf")
    private Button searchButton;

    @FindBy(xpath = "//*[@class='T-I J-J5-Ji T-Pm T-I-ax7 L3 J-JN-M-I']/div[1]/span")
    private List<Button> chooseButtons;

    @FindBy(xpath = "//*[@class='asl T-I-J3 J-J5-Ji']/parent::*")
    private List<Button> spamButtons;

    @FindBy(xpath = "//div[@class = 'ae4 UI']//tr[contains(@class, 'zA')]")
    private List<WebElement> messageCollection;

    @FindBy(css = ".ae4.UI")
    private WebElement messageContainer;

    public GmailHomePage(WebDriver driver) {
        PageFactory.initElements(new CustomFieldDecorator(driver), this);
        this.driver = driver;
    }

    public void clickCompose() {
        composeButton.click();
    }

    public void writeLetter(Letter letter) {
        toTextarea.sendKeys(30, letter.getTo());
        subjectTextarea.sendKeys(letter.getSubject());
        messageTextarea.sendKeys(letter.getMessage());
    }

    public void saveMessage() {
        saveAndCloseButton.click();
    }

    public void openDraftFolder() {
        draftFolder.click();
    }

    public void openDraftMessage() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lastDraftMessage.click();
    }

    public void sendMessage() {
        sendButton.click();
    }

    public Letter getLetter() {
        letter.setTo(toTextareaCompare.getEmail());
        letter.setSubject(subjectTextarea.getValue());
        letter.setMessage(messageTextarea.getText());

        return letter;
    }

    public WebElement getIsSentLabel() {
        return isSentLabel;
    }

    public Button getSaveAndCloseButton() {
        return saveAndCloseButton;
    }

    public TextArea getMessageTextarea() {
        return messageTextarea;
    }

    public Button getSendButton() {
        return sendButton;
    }

    public TextInput getSearchInput() {
        return searchInput;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public List<Button> getChooseButtons() {
        return chooseButtons;
    }

    public List<Button> getSpamButtons() {
        return spamButtons;
    }

    public List<WebElement> getMessageCollection() {
        return messageCollection;
    }

    public WebElement getMessageContainer() {
        return messageContainer;
    }
}
