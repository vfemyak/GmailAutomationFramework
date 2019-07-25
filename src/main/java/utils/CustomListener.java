package utils;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class CustomListener implements ITestListener {

    private static Logger logger = LogManager.getLogger(CustomListener.class);

    @Override
    public void onTestStart(ITestResult iTestResult) {

    }

    @Override
    @Step("Test SUCCESS: {0}")
    public void onTestSuccess(ITestResult iTestResult) {
        logger.info("Test SUCCESS:" + iTestResult.getName());
    }

    @Override
    @Step("Test FAILED: {0}")
    public void onTestFailure(ITestResult iTestResult) {
        makeScreenshot();
        logger.error("Test FAILED:" + iTestResult.getName());
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }

    @Attachment(value = "Page screenshot", type = "image/png")
    private byte[] makeScreenshot() {
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
    }
}
