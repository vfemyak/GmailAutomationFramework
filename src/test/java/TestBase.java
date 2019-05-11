import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.WebDriver;
import utils.DriverManager;

public class TestBase {

    protected WebDriver driver = DriverManager.getDriver();

//    @Parameterized.Parameters
//    public static Object[][] credentials() throws IOException, InvalidFormatException {
//
//        //choose strategy
//        DataSourceReaderStrategy dataReader = new DataSourceReaderStrategy(new CsvDataReader());
//
//        List<String[]> arrayList = new ArrayList<>();
//        arrayList = dataReader.findAll();
//
//        return arrayList.toArray(new Object[0][]);
//    }

    @AfterEach
    public void close() {
        driver.quit();
    }
}
