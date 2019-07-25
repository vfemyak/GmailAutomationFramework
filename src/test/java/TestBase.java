import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.WebDriver;
import utils.Browser;
import utils.DriverManager;

import static utils.Browser.*;

public class TestBase {

    protected WebDriver driver = DriverManager.getDriver();

    @AfterEach
    public void close() {
        Browser.releaseBrowsers();
        driver.quit();
    }
}
