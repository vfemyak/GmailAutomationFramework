package data_readers;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.List;

public interface DataSourceReader<T> {
    List<T[]> findAll() throws IOException, InvalidFormatException;
}
