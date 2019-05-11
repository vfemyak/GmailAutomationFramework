import Services.AuthorisationService;
import Services.MessageService;
import models.Letter;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.assertTrue;

public class SendDraftLetterTestJUnit5 extends TestBase {

    Letter letter = new Letter("vfemyak@gmail.com", "tessst task3", "Testtting");
    private AuthorisationService authorisationService = new AuthorisationService(driver);
    private MessageService messageService = new MessageService(driver, letter);

    @ParameterizedTest
    @CsvSource({
            "vfemyaktest, test1234test",
            "vfemyaktest1, test1234test1"
    })
    @Execution(ExecutionMode.CONCURRENT)
    public void sendDraftLetter_Positive_TestCase(String login, String password) {

        driver.get("https://www.google.com/gmail/");

        authorisationService.logIn(login, password);

        messageService.writeMessageAndClose();
        messageService.openDraftMessage();
        assertTrue(messageService.isMessageFieldsValid(letter));  //validating fields
        messageService.sendDraftLetter();
        assertTrue(messageService.isLetterSent());  //checking if the message was sent
    }

}
