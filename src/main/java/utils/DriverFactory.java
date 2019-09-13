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
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\Drivers\\Windows\\chromedriver.exe");
        } else if (os.contains("osx")) {
            System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\Drivers\\MacOS\\chromedriver");
        } else if (os.contains("nix") || os.contains("aix") || os.contains("nux")) {
            //Operating system is based on Linux/Unix/*AIX
        }
        return new ChromeDriver();
    };

    private static final Supplier<WebDriver> ieDriverSupplier = () -> {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            System.setProperty("webdriver.ie.driver", "src\\main\\resources\\Drivers\\Windows\\IEDriverServer.exe");
        } else if (os.contains("osx")) {
            //Operating system is Apple OSX based
        } else if (os.contains("nix") || os.contains("aix") || os.contains("nux")) {
            //Operating system is based on Linux/Unix/*AIX
        }
        return new InternetExplorerDriver();
    };

    private static final Supplier<WebDriver> firefoxDriverSupplier = () -> {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            System.setProperty("webdriver.gecko.driver", "src\\main\\resources\\Drivers\\Windows\\geckodriver.exe");
        } else if (os.contains("osx")) {
            System.setProperty("webdriver.gecko.driver", "src\\main\\resources\\Drivers\\MacOS\\geckodriver");
        } else if (os.contains("nix") || os.contains("aix") || os.contains("nux")) {
            //Operating system is based on Linux/Unix/*AIX
        }
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
}
