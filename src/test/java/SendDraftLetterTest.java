import BO.AuthorisationBO;
import BO.SendMessageBO;
import models.Letter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class SendDraftLetterTest {

    @DataProvider(name = "Authentication",parallel = true)
    public static Object[][] credentials(){
        return new Object[][]{
                {"vfemyaktest","test1234test"},
                {"vfemyaktest1","test1234test1"},
                {"vfemyaktest2","test1234test2"}
        };
    }

    @BeforeClass
    public static void init(){
        System.setProperty("webdriver.chrome.driver","src\\main\\resources\\chromedriver.exe");
    }

    @Test(dataProvider = "Authentication")
    public void gmailLoginTest(String login, String password) {

        WebDriver driver = DriverManager.getInstance().getDriver();
        driver.get("https://www.google.com/gmail/");

        Letter letter = new Letter("vfemyak@gmail.com", "tessst task3", "Testtting");

        AuthorisationBO authorisationBO = new AuthorisationBO(driver);
        authorisationBO.LogIn(login,password);

        SendMessageBO sendMessageBO = new SendMessageBO(driver,letter);
        sendMessageBO.writeMessageAndClose();
        sendMessageBO.openDraftMessage();
        Assert.assertTrue(sendMessageBO.isValidateFields(letter));  //validating fields
        sendMessageBO.sendDraftLetter();
        Assert.assertTrue(sendMessageBO.isSent());  //checking if the message was sent

        driver.quit();
    }

}
