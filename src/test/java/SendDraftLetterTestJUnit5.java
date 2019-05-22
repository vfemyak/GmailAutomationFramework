import Actions.AuthorisationService;
import Actions.MessageService;
import Asserts.MessageAsserter;
import models.Letter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

@Execution(ExecutionMode.CONCURRENT)
public class SendDraftLetterTestJUnit5 extends TestBase {

    Letter letter = new Letter("vfemyaktest2@gmail.com", "tessst task3", "Testtting");
    private AuthorisationService authorisationService = new AuthorisationService(driver);
    private MessageService messageService = new MessageService(driver, letter);
    private MessageAsserter messageAsserter = new MessageAsserter();

    @ParameterizedTest
    @CsvFileSource(resources = "/test_data/users_data.csv")
    public void sendDraftLetter_Positive_TestCase(String login, String password) {

        driver.get("https://www.google.com/gmail/");

        authorisationService.logIn(login, password);

        messageService.writeMessageAndClose();
        messageService.openDraftMessage();
        messageAsserter.assertMessageFieldsValid(letter);  //validating fields

        messageService.sendDraftLetter();
        messageAsserter.assertLetterSent();  //checking if the message was sent
    }

    //
//    @ParameterizedTest
//    @CsvFileSource(resources = "/test_data/users_data.csv")
    @Test
    public void moveLetterToSpam_Positive_TestCase() {

        driver.get("https://www.google.com/gmail/");

        authorisationService.logIn("vfemyaktest2", "test1234test2");

        messageService.checkAllLetterFromUser("vfemyaktest1");
        messageService.moveLetterToSpam();

        messageAsserter.assertLetterMovedToSpam();
    }

}
