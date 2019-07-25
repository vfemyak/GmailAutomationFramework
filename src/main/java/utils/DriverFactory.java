package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DriverFactory {
    private static final Map<Browser, Supplier<WebDriver>> driverMap = new HashMap<>();

    private static final Supplier<WebDriver> chromeDriverSupplier = () -> {
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        return new ChromeDriver();
    };

    private static final Supplier<WebDriver> ieDriverSupplier = () -> {
        System.setProperty("webdriver.ie.driver", "src\\main\\resources\\IEDriverServer.exe");
        return new InternetExplorerDriver();
    };

    private static final Supplier<WebDriver> firefoxDriverSupplier = () -> {
        System.setProperty("webdriver.gecko.driver", "src\\main\\resources\\geckodriver.exe");
        return new FirefoxDriver();
    };

    private static final Supplier<WebDriver> safariDriverSupplier = () -> {
        System.setProperty("", "");
        return new SafariDriver();
    };

    private static final Supplier<WebDriver> operaDriverSupplier = () -> {
        System.setProperty("", "");
        return new OperaDriver();
    };

    static {
        driverMap.put(Browser.CHROME, chromeDriverSupplier);
        driverMap.put(Browser.IE, ieDriverSupplier);
        driverMap.put(Browser.FIREFOX, firefoxDriverSupplier);
    }

    public static WebDriver getDriver(Browser type) {
        return driverMap.get(type).get();
    }

    public static WebDriver getDriver() {
        return driverMap.get(Browser.getAvailable()).get();
    }
}
