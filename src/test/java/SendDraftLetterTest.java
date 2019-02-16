import BO.AuthorisationBO;
import BO.SendMessageBO;
import models.Letter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class SendDraftLetterTest {

    private static WebDriver driver;

    private final String login = "vfemyaktest";
    private final String password = "test1234test";
    private final String to = "vfemyak@gmail.com";
    private final String subject = "tessst task3";
    private final String message = "Testtting";


    @BeforeClass
    public static void init(){
        System.setProperty("webdriver.chrome.driver","src\\main\\resources\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
        driver.get("https://www.google.com/gmail/");
    }

    @Test
    public void gmailLoginTest() {

        Letter letter = new Letter("vfemyak@gmail.com", "tessst task3", "Testtting");

        AuthorisationBO authorisationBO = new AuthorisationBO(driver);
        authorisationBO.LogIn(login,password);

        SendMessageBO sendMessageBO = new SendMessageBO(driver,letter);
        sendMessageBO.writeMessageAndClose();
        sendMessageBO.openDraftMessage();
        Assert.assertTrue(sendMessageBO.isValidateFields(letter));
        sendMessageBO.sendDraftLetter();

        //TODO додати асерт на перевірку чи повідомлення надіслано

    }

    @AfterClass
    public static void tearDown(){
        driver.quit();
    }
}
