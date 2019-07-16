package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public class DriverManager {
    public static final Logger logger = LogManager.getLogger(DriverManager.class);

    private DriverManager() {
    }

    private static ThreadLocal<WebDriver> driverPool = new ThreadLocal<WebDriver>();

    public static WebDriver getDriver(DriverType type) {
        if (driverPool.get() == null) {
            logger.info("Driver initialize successful");
            driverPool.set(initWebDriver(type));
        } else logger.error(" Driver doesn't initialized");
        return driverPool.get();
    }

//    public static WebDriver getAvailableDriver(){
//        //todo тут має юзатися синхронайз метод, який буде брати по черзі браузери і локати
//        // їх(ставити наприклад true), а по закінченню тесту, дописати метод який буде розлокувати
//        // (ставити в false)
//    }

    static WebDriver initWebDriver(DriverType type) {
        WebDriver webDriver = DriverFactory.getDriver(type);

        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        return webDriver;
    }
}