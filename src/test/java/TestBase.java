import data_readers.CsvDataReader;
import data_readers.DataSourceReaderStrategy;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import utils.DriverManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
