package data_readers;

import com.opencsv.CSVReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelDataReader implements DataSourceReader {

    private final static String USERS_DATA_EXCEL_FILE = "src/test/test_data/users_data.xlsx";

    public ExcelDataReader() {
    }

    @Override
    public List findAll() throws IOException, InvalidFormatException {
        List<String[]> arrayList = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(new File(USERS_DATA_EXCEL_FILE));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        int rowNum = sheet.getLastRowNum();
        int columnNum = sheet.getRow(0).getLastCellNum();

        String[][] nextLine = new String[rowNum+1][columnNum];

        int i = 0;
        for (Row row: sheet) {
            int j = 0;
            for(Cell cell: row) {
                nextLine[i][j] = dataFormatter.formatCellValue(cell);
                j++;
            }
            arrayList.add(nextLine[i]);
            i++;
        }

//        workbook.close();     //file will be damaged after this action
        return arrayList;
    }
}
