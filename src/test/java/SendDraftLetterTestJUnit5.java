import Actions.AuthorisationService;
import Actions.MessageService;
import Asserts.MessageAsserter;
import models.Letter;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class SendDraftLetterTestJUnit5 extends TestBase {

    Letter letter = new Letter("vfemyak@gmail.com", "tessst task3", "Testtting");
    private AuthorisationService authorisationService = new AuthorisationService(driver);
    private MessageService messageService = new MessageService(driver, letter);
    private MessageAsserter messageAsserter = new MessageAsserter();

    @ParameterizedTest
    @CsvFileSource(resources = "/test_data/users_data.csv")
    @Execution(ExecutionMode.CONCURRENT)
    public void sendDraftLetter_Positive_TestCase(String login, String password) {

        driver.get("https://www.google.com/gmail/");

        authorisationService.logIn(login, password);

        messageService.writeMessageAndClose();
        messageService.openDraftMessage();
        messageAsserter.assertMessageFieldsValid(letter);  //validating fields

        messageService.sendDraftLetter();
        messageAsserter.assertLetterSent();  //checking if the message was sent
    }

}
