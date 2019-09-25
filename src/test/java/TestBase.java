import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.WebDriver;
import utils.Browser;
import utils.DriverManager;

public class TestBase {

    protected Browser browser = Browser.getAvailable();
    protected WebDriver driver = DriverManager.getDriver(browser);

    @AfterEach
    public void close() {
        Browser.releaseBrowser(browser);
        driver.quit();
    }
}
