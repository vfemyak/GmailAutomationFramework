import BO.AuthorisationBO;
import BO.SendMessageBO;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.CustomListener;
import utils.DriverManager;
import data_readers.CsvDataReader;
import data_readers.DataSourceReaderStrategy;
import models.Letter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.WebDriver;
import utils.Parallelized;
//import org.testng.Assert;
//import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(Parallelized.class)
//@Listeners({CustomListener.class})
public class SendDraftLetterTest {

    private String login;
    private String password;
    private WebDriver driver;

    public SendDraftLetterTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] credentials() throws IOException, InvalidFormatException {

        //choose strategy
        DataSourceReaderStrategy dataReader = new DataSourceReaderStrategy(new CsvDataReader());

        List<String[]> arrayList = new ArrayList<>();
        arrayList = dataReader.findAll();

        return arrayList.toArray(new Object[0][]);
    }

    @Before
    public void init() {
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        driver = DriverManager.getInstance().getDriver();
    }

    @Test()
    public void gmailLoginTest() {

        driver.get("https://www.google.com/gmail/");

        Letter letter = new Letter("vfemyak@gmail.com", "tessst task3", "Testtting");

        AuthorisationBO authorisationBO = new AuthorisationBO(driver);
        authorisationBO.LogIn(login, password);

        SendMessageBO sendMessageBO = new SendMessageBO(driver, letter);
        sendMessageBO.writeMessageAndClose();
        sendMessageBO.openDraftMessage();
        Assert.assertTrue(sendMessageBO.isValidateFields(letter));  //validating fields
        sendMessageBO.sendDraftLetter();
        Assert.assertTrue(sendMessageBO.isSent());  //checking if the message was sent
    }

    @After
    public void close() {
        driver.quit();
    }
}
