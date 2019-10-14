package dev.sanero.module;


import dev.sanero.util.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReader {
    public static List<ScreenInformation> getListScreenInformation() {
        List<ScreenInformation> screenInformationList = new ArrayList<>();
        try {
            InputStream inputStream = new FileInputStream(new File("files/test.xlsx"));

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String korName = getCellData(row.getCell(1));
                if (StringUtils.isNullOrEmpty(korName)) {
                    continue;
                }
                String code = getCellData(row.getCell(2));
                String mapperFile = getCellData(row.getCell(3));
                String event = getCellData(row.getCell(4));
                String method = getCellData(row.getCell(6));
                ScreenInformation screenInfo = new ScreenInformation(korName, code, mapperFile, event, method);
                screenInformationList.add(screenInfo);
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenInformationList;
    }

    private static String getCellData(Cell cell) {
        String data = "";
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                data = Double.toString(cell.getNumericCellValue());
                break;
            case STRING:
                data = cell.getStringCellValue();
                break;
            default:
                data = "";
                break;
        }
        return data;
    }
}
