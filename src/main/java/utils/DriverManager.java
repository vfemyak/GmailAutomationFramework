package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class DriverManager {
    public static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final String WEB_DRIVER = "webdriver.chrome.driver";
    private static final String WEB_DRIVER_PATH = "src\\main\\resources\\chromedriver.exe";

    private DriverManager() {
    }

    private static ThreadLocal<WebDriver> driverPool = new ThreadLocal<WebDriver>();

    public static WebDriver getDriver() {
        if (driverPool.get() == null) {
            logger.info("Driver initialize successful");
            driverPool.set(initWebDriver());
        } else logger.error(" Driver doesn't initialized");
        return driverPool.get();
    }

    static WebDriver initWebDriver() {
        System.setProperty(WEB_DRIVER, WEB_DRIVER_PATH);

        WebDriver webDriver = new ChromeDriver();

        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        return webDriver;
    }
}