import Services.AuthorisationService;
import Services.MessageService;
import models.Letter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.assertTrue;

public class SendDraftLetterTest_JUnit5 extends TestBase {

    Letter letter = new Letter("vfemyak@gmail.com", "tessst task3", "Testtting");
    private AuthorisationService authorisationService = new AuthorisationService(driver);
    private MessageService messageService = new MessageService(driver, letter);

    @ParameterizedTest
    @CsvSource({
            "vfemyaktest, test1234test",
            "vfemyaktest1, test1234test1"
    })
    public void sendDraftLetter_Positive_TestCase(String login, String password) {

        driver.get("https://www.google.com/gmail/");

        authorisationService.logIn(login, password);

        messageService.writeMessageAndClose();
        messageService.openDraftMessage();
        assertTrue(messageService.isValidateFields(letter));  //validating fields
        messageService.sendDraftLetter();
        assertTrue(messageService.isSent());  //checking if the message was sent
    }

}
