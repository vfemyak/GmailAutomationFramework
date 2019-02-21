import BO.AuthorisationBO;
import BO.SendMessageBO;
import data_readers.CsvDataReader;
import data_readers.DataSourceReaderStrategy;
import models.Letter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SendDraftLetterTest {

    @DataProvider(name = "Authentication",parallel = true)
    public static Object[][] credentials() throws IOException, InvalidFormatException {

        //choose strategy
        DataSourceReaderStrategy dataReader = new DataSourceReaderStrategy(new CsvDataReader());

        List<String[]> arrayList = new ArrayList<>();
        arrayList = dataReader.findAll();

        return arrayList.toArray(new Object[0][]);
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
