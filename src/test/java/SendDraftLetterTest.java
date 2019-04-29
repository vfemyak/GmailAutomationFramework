import Services.AuthorisationService;
import Services.MessageService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import data_readers.CsvDataReader;
import data_readers.DataSourceReaderStrategy;
import models.Letter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import utils.Parallelized;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(Parallelized.class)
//@Listeners({CustomListener.class})
public class SendDraftLetterTest extends TestBase {

    Letter letter = new Letter("vfemyak@gmail.com", "tessst task3", "Testtting");
    private AuthorisationService authorisationService = new AuthorisationService(driver);
    private MessageService messageService = new MessageService(driver, letter);

    private String login;
    private String password;

    public SendDraftLetterTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Test()
    public void sendDraftLetter_Positive_TestCase() {

        driver.get("https://www.google.com/gmail/");

        authorisationService.logIn(login, password);

        messageService.writeMessageAndClose();
        messageService.openDraftMessage();
        assertTrue(messageService.isValidateFields(letter));  //validating fields
        messageService.sendDraftLetter();
        assertTrue(messageService.isSent());  //checking if the message was sent
    }

}
