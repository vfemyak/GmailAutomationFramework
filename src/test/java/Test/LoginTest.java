package Test;

import BO.AuthorisationBO;
import BO.SendMessageBO;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class LoginTest {

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
    public void gmailLoginTest() throws InterruptedException {

        AuthorisationBO authorisationBO = new AuthorisationBO(driver);
        authorisationBO.LogIn(driver, login,password);

        SendMessageBO sendMessageBO = new SendMessageBO(driver);
        sendMessageBO.writeMessageAndClose(driver,to,subject,message);
        sendMessageBO.checkAndSendDraftMessage(driver,to,subject,message);

        Thread.sleep(4000);         //using this to see result

        driver.quit();
    }
}
