package wrappers;

import org.openqa.selenium.*;

import java.util.List;

public class Element implements IElement {
        protected WebElement webElement;
        public Element(WebElement webElement) {
            this.webElement = webElement;
        }

        public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
            return null;
        }

        public void clear() {
            webElement.clear();
        }

        public void click() {
            webElement.click();
        }

        public WebElement findElement(By arg0) {
            return webElement.findElement(arg0);
        }

        public List<WebElement> findElements(By arg0) {
            return webElement.findElements(arg0);
        }

        public String getAttribute(String arg0) {
            return webElement.getAttribute(arg0);
        }

        public String getCssValue(String arg0) {
            return webElement.getCssValue(arg0);
        }

        public Point getLocation() {
            return webElement.getLocation();
        }

        public Rectangle getRect() {
            return webElement.getRect();
        }

        public Dimension getSize() {
            return webElement.getSize();
        }

        public String getTagName() {
            return webElement.getTagName();
        }

        public String getText() {
            return webElement.getText();
        }

        public boolean isDisplayed() {
            return webElement.isDisplayed();
        }

        public boolean isEnabled() {
            return webElement.isEnabled();
        }

        public boolean isSelected() {
            return webElement.isSelected();
        }

        public void sendKeys(CharSequence... arg0) {
            webElement.sendKeys(arg0);
        }

        public void submit() {
        webElement.submit();
    }

}