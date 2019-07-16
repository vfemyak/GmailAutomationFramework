import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.WebDriver;
import utils.DriverManager;

import static utils.DriverType.*;

public class TestBase {

    protected WebDriver driver = DriverManager.getDriver(CHROME);

    @AfterEach
    public void close() {
        driver.quit();
    }
}
