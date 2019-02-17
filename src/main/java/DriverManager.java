import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class DriverManager {
    public static final Logger LOG = LogManager.getLogger(DriverManager.class);

    private DriverManager() {
    }

    private static DriverManager instance = new DriverManager();


    public static DriverManager getInstance() {
        return instance;
    }


    private static ThreadLocal<WebDriver> driverPool = new ThreadLocal<WebDriver>();

    public WebDriver getDriver() {
        if (driverPool.get() == null) {
            LOG.info("Driver initialize successful");
            driverPool.set(initWebDriver());
        }else LOG.error(" Driver doesn't initialized");
        return driverPool.get();
    }

    static WebDriver initWebDriver() {
        WebDriver webDriver = new ChromeDriver();

        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        return webDriver;
    }
}