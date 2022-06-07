package Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
    private static final Logger LOG = LogManager.getLogger(ExcelUtils.class);
    private List<String> sheetNamesList = new ArrayList();
    private FileInputStream excelFileInputStream;
    private FileOutputStream excelFileOutputStream;
    Workbook workbook;
    private Sheet writeSheet;
    private Row row;
    private int rowNumber;

    public ExcelUtils() {
    }

    public List<String> getSheetNamesList() {
        return this.sheetNamesList;
    }

    private void getWorkbook(FileInputStream inputStream, String excelFilePath) throws IOException {
        LOG.info("Reading workbook from file:" + excelFilePath);
        if (excelFilePath.endsWith("xlsx")) {
            this.workbook = new XSSFWorkbook(inputStream);
        } else {
            if (!excelFilePath.endsWith("xls")) {
                LOG.error("File extension not provided");
                throw new IllegalArgumentException("Please include the excel file format .e.g xls,xlsx");
            }

            this.workbook = new HSSFWorkbook(inputStream);
        }

    }

    public List readExcelDataFileToList(String dataSheetFileName, String sheetName) throws IOException {
        List<List<String>> twoDdataList = new ArrayList();
        LOG.info("Processing datasheet: " + dataSheetFileName);
        LOG.info("Sheet Name: " + sheetName);
        this.excelFileInputStream = new FileInputStream(new File(dataSheetFileName));
        this.getWorkbook(this.excelFileInputStream, dataSheetFileName);
        Sheet dataSheet = this.workbook.getSheet(sheetName);
        this.sheetNamesList.add(sheetName);
        Iterator<Row> iterator = dataSheet.iterator();
        int numOfColumns = 0;
        LOG.debug("Reading sheet rows");

        while(iterator.hasNext()) {
            Row currentRow = (Row)iterator.next();
            if (currentRow.getRowNum() == 0) {
                LOG.debug("Getting column headers");
                if (currentRow.getLastCellNum() == 0) {
                    throw new IOException("Expected column headers on the first column. Column headers empty");
                }

                numOfColumns = currentRow.getLastCellNum();
            }

            List<String> tempList = this.getRowDataList(currentRow, numOfColumns);
            if (tempList.isEmpty() || tempList.size() == 0) {
                break;
            }

            twoDdataList.add(tempList);
        }

        LOG.debug("Closing file");
        this.excelFileInputStream.close();
        if (twoDdataList.size() == 0) {
            throw new IOException("Data sheet is empty. sheet name:" + sheetName);
        } else {
            return twoDdataList;
        }
    }

    public List<List<List<String>>> readExcelDataFileToList(String dataSheetFileName) throws IOException {
        List<List<List<String>>> dataList = new ArrayList();
        LOG.info("Processing data sheet: " + dataSheetFileName);
        FileInputStream excelFileStream = null;
        if (this.getClass().getClassLoader().getResource(dataSheetFileName) != null) {
            excelFileStream = new FileInputStream(this.getClass().getClassLoader().getResource(dataSheetFileName).getFile());
        } else {
            if (!(new File(dataSheetFileName)).exists()) {
                throw new IOException("Excel data sheet file not found:" + dataSheetFileName);
            }

            excelFileStream = new FileInputStream(new File(dataSheetFileName));
        }

        this.getWorkbook(excelFileStream, dataSheetFileName);
        LOG.info("Getting sheet names in the workbook");

        for(int i = 0; i < this.workbook.getNumberOfSheets(); ++i) {
            LOG.info("Sheet Name: " + this.workbook.getSheetName(i));
            this.sheetNamesList.add(this.workbook.getSheetName(i));
        }

        this.sheetNamesList.forEach((sheetName) -> {
            LOG.info("Processing sheet: " + sheetName);
            Iterator<Row> iterator = this.workbook.getSheet(sheetName).iterator();
            int numOfColumns = 0;
            LOG.debug("Reading sheet rows");
            ArrayList sheetDataList = new ArrayList();

            while(iterator.hasNext()) {
                Row currentRow = (Row)iterator.next();
                if (currentRow.getRowNum() == 0) {
                    LOG.debug("Getting column headers");
                    if (currentRow.getLastCellNum() == 0) {
                    }

                    numOfColumns = currentRow.getLastCellNum();
                }

                List<String> rowList = this.getRowDataList(currentRow, numOfColumns);
                if (!rowList.isEmpty()) {
                    sheetDataList.add(rowList);
                }
            }

            dataList.add(sheetDataList);
        });
        LOG.debug("Closing file stream");
        excelFileStream.close();
        return dataList;
    }

    private List<String> getRowDataList(Row currentRow, int numOfRowColumns) {
        List<String> tempList = new ArrayList();

        for(int i = 0; i < numOfRowColumns; ++i) {
            Cell currentCell = currentRow.getCell(i);
            String cellValue;
            if (currentCell == null) {
                cellValue = "";
            } else {
                switch(currentCell.getCellType()) {
                    case 0:

                            final DataFormatter formatter= new DataFormatter();
                            cellValue=formatter.formatCellValue(currentCell);

                            //Removed below code because it was only converting to decimal without checking the format of the cellvalue type
                            //BigDecimal bigDecimal = new BigDecimal(currentCell.getNumericCellValue());
                            //cellValue = bigDecimal.toString().trim();


                        break;
                    case 1:

                    case 3:
                    default:
                        cellValue = currentCell.getStringCellValue().trim();
                        break;
                    case 2:
                        cellValue = currentCell.getCellFormula();
                        break;
                    case 4:
                        cellValue = Boolean.toString(currentCell.getBooleanCellValue()).trim();
                        break;
                    case 5:
                        cellValue = "";

                }
            }

            tempList.add(cellValue);
        }

        return tempList;
    }

    public String[][] readExcelDataFileToArray(String dataSheetFileName, String sheetName) throws IOException {
        List<List<String>> twoDdataList = this.readExcelDataFileToList(dataSheetFileName, sheetName);
        LOG.debug("Converting data list to two-d array");
        List<String> innerList = new ArrayList();
        int countValidRows = 0;

        for(int i = 0; i < twoDdataList.size(); ++i) {
            ++countValidRows;
            innerList.addAll((Collection)twoDdataList.get(i));
            innerList.removeIf(String::isEmpty);
            if (innerList.size() == 0) {
                break;
            }

            innerList.clear();
        }

        String[][] twoDArray = new String[countValidRows][];

        for(int i = 0; i < twoDArray.length; ++i) {
             innerList = (List)twoDdataList.get(i);
            String[] innerAsArray = (String[])innerList.toArray(new String[innerList.size()]);
            twoDArray[i] = innerAsArray;
        }

        return twoDArray;
    }

    public String[][][] readExcelDataFileToArray(String dataSheetFileName) throws IOException {
        List<List<List<String>>> dataList = this.readExcelDataFileToList(dataSheetFileName);
        LOG.debug("Converting data list to array");
        new ArrayList();
        String[][][] dataArray = new String[dataList.size()][][];

        for(int k = 0; k < dataArray.length; ++k) {
            dataArray[k] = new String[((List)dataList.get(k)).size()][];

            for(int i = 0; i < dataArray[k].length; ++i) {
                List<String> innerList = (List)((List)dataList.get(k)).get(i);
                String[] innerAsArray = (String[])innerList.toArray(new String[innerList.size()]);
                dataArray[k][i] = innerAsArray;
            }
        }

        return dataArray;
    }

    public static String getCellValue(String[] dataRowArray, String[] headers, String columnName) throws Exception {
        LOG.debug(String.format("Getting cell value for column %s", columnName));
        if (dataRowArray.length == 0) {
            throw new Exception("Data provided is empty");
        } else if (headers.length == 0) {
            throw new Exception("Headers are empty");
        } else if (dataRowArray.length != headers.length) {
            throw new Exception("The number of column headers should match the data entries count provided");
        } else {
            int columnIndex = Arrays.asList(headers).indexOf(columnName.trim());
            LOG.debug("Column index is:" + columnIndex);
            if (columnIndex < 0) {
                LOG.error(String.format("Column index %s does not exist in the excel sheet", columnIndex));
                throw new Exception("Column name not found");
            } else {
                return dataRowArray[columnIndex].toString();
            }
        }
    }

    public static int getColumnIndex(String[] headers, String columnName) throws Exception {
        LOG.debug(String.format("Getting cell index for header %s", columnName));
        if (headers.length == 0) {
            throw new Exception("Headers are empty");
        } else {
            int columnIndex = Arrays.asList(headers).indexOf(columnName.trim());
            LOG.debug("Column index is:" + columnIndex);
            if (columnIndex < 0) {
                LOG.error(String.format("Column index %s does not exist in the headers provided sheet", columnIndex));
                throw new Exception(String.format("Column index %s does not exist in the headers provided sheet", columnIndex));
            } else {
                return columnIndex;
            }
        }
    }

    public static String[] getColumHeaders(String[][] dataArray) throws Exception {
        LOG.debug("Getting column headers");
        if (dataArray != null && dataArray[0] != null && dataArray[0].length != 0) {
            return dataArray[0];
        } else {
            LOG.error("No column headers found, data is empty");
            throw new Exception("No column headers found");
        }
    }

    public void writeHeaders(String[] headers) {
        if (headers == null) {
            throw new IllegalArgumentException("Please provide report headers");
        } else {
            this.row = this.writeSheet.createRow(this.rowNumber++);

            for(int i = 0; i < headers.length; ++i) {
                this.writeToCell(headers[i], i);
            }

        }
    }

    public void flush() {
        try {
            this.workbook.write(this.excelFileOutputStream);
            this.excelFileOutputStream.flush();
            this.excelFileOutputStream.close();
        } catch (IOException var2) {
            LOG.error(var2.getMessage());
        }

    }

    public void writeToCell(String value, int cellNumber) {
        Cell cell = this.row.createCell(cellNumber);
        cell.setCellValue(value);
    }

    public void writeToReport(String[] data) {
        for(int i = 0; i < data.length; ++i) {
            this.writeToCell(data[i], i);
        }

    }

    public void createRow() {
        this.row = this.writeSheet.createRow(this.rowNumber++);
    }

    public void writeToReport(Object[][] data) {
        Object[][] var2 = data;
        int var3 = data.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object[] datatype = var2[var4];
            this.createRow();
            int colNum = 0;
            Object[] var7 = datatype;
            int var8 = datatype.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                Object field = var7[var9];
                Cell cell = this.row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String)field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((double)(Integer)field);
                }
            }
        }

        this.flush();
    }

    public void createExcelWorkbook(String fileName) {
        this.createExcelWorkbook(fileName, "Sheet1");
    }

    public void createExcelWorkbook(String fileName, String sheetName) {
        try {
            this.excelFileOutputStream = new FileOutputStream(new File(fileName));
            this.workbook = new XSSFWorkbook();
            this.writeSheet = this.workbook.createSheet(sheetName);
        } catch (Exception var4) {
            LOG.error(var4.getMessage());
        }

    }

    public void ReadExcelDate(String sheetName) throws IOException {
         String excelFilePath="src/test/resources/Test Data/Dev_KRS_Test_Users.xlsx";
         FileInputStream inputStream= new FileInputStream(excelFilePath);
         Workbook workbook=new XSSFWorkbook(inputStream);
         Sheet firstSheet=workbook.getSheetAt(0);
         Iterator<Row> iterator=firstSheet.iterator();

         while(iterator.hasNext()){
             Row nextRow =iterator.next();
             Iterator<Cell> cellIterator=nextRow.cellIterator();

             while(cellIterator.hasNext()){
                 Cell cell=cellIterator.next();
                 switch (cell.getCellFormula()){


                 }
                 System.out.print(" - ");
             }
             System.out.println();
         }
         workbook.close();
         inputStream.close();

         {
         }
    }

}

