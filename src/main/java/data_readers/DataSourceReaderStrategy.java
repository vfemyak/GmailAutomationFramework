package data_readers;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.List;

public class DataSourceReaderStrategy {
    private DataSourceReader dataSourceReader;

    public DataSourceReaderStrategy(DataSourceReader dataSourceReader) {
        this.dataSourceReader = dataSourceReader;
    }

    public void setDataSourceReader(DataSourceReader dataSourceReader) {
        this.dataSourceReader = dataSourceReader;
    }

    public List findAll() throws IOException, InvalidFormatException {
        return dataSourceReader.findAll();
    }
}
