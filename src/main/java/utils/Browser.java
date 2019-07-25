package utils;

public enum Browser {

    FIREFOX(false),
    CHROME(false),
//    SAFARI(false),
//    OPERA(false),
    IE(false);

    private boolean isLocked;

    Browser(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public static synchronized Browser getAvailable() {
        Browser[] browsers = Browser.values();

        while (true) {
            for (Browser browser : browsers) {
                if (!browser.isLocked) {
                    browser.setLocked(true);
                    return browser;
                }
            }
        }
    }

    public static void releaseBrowser(Browser browser) {
        browser.setLocked(false);
    }

    public static void releaseBrowsers() {
        Browser[] browsers = Browser.values();

        for (Browser browser : browsers) {
            browser.setLocked(false);
        }
    }

    @Override
    public String toString() {
        return "Browser{" +
                "isLocked=" + isLocked +
                '}';
    }
}
