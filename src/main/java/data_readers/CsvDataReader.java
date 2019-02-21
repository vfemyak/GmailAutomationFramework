package data_readers;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvDataReader implements DataSourceReader{

    private final static String USERS_DATA_CSV_FILE = "src/test/test_data/users_data.csv";

    public CsvDataReader() {
    }

    @Override
    public List<String[]> findAll() throws IOException {
        List<String[]> arrayList = new ArrayList<>();
        String[] nextLine;

        CSVReader csvReader = new CSVReader(new FileReader(USERS_DATA_CSV_FILE));

        while ((nextLine = csvReader.readNext())!= null){
            arrayList.add(nextLine);
        }
        return arrayList;
    }
}
