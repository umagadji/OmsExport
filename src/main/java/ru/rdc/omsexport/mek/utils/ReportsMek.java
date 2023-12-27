package ru.rdc.omsexport.mek.utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rdc.omsexport.mek.models.ErrExtended;
import ru.rdc.omsexport.utils.AlertDialogUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

//Класс для получения отчетов по МЭК
@Service
public class ReportsMek {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Autowired
    public ReportsMek() {
    }

    //Метод создает ячейку
    private void createCell(SXSSFRow row, int columnCount, Object valueOfCell, CellStyle style) {
        SXSSFCell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else if (valueOfCell instanceof LocalDate) {
            cell.setCellValue((LocalDate) valueOfCell);
        } else if (valueOfCell instanceof Double) {
            cell.setCellValue((Double) valueOfCell);
        } else if (valueOfCell instanceof Boolean){
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    public void createErrReport(List<ErrExtended> list, Stage stage) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet("MEK");
        SXSSFRow headerRow = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        createCell(headerRow,0, "desc", headerStyle);
        createCell(headerRow,1, "error", headerStyle);
        createCell(headerRow,2, "npolis", headerStyle);
        createCell(headerRow,3, "fio", headerStyle);
        createCell(headerRow,4, "birthDate", headerStyle);
        createCell(headerRow,5, "date_in", headerStyle);
        createCell(headerRow,6, "date_out", headerStyle);
        createCell(headerRow,7, "refreason", headerStyle);
        createCell(headerRow,8, "sumvUsl", headerStyle);
        createCell(headerRow,9, "codeUsl", headerStyle);
        createCell(headerRow,10, "sankSum", headerStyle);
        createCell(headerRow,11, "diagnosis", headerStyle);
        createCell(headerRow,12, "nameMO", headerStyle);
        createCell(headerRow,13, "docCode", headerStyle);
        createCell(headerRow,14, "idstrax", headerStyle);
        createCell(headerRow,15, "nhistory", headerStyle);
        createCell(headerRow,16, "errorCode", headerStyle);
        createCell(headerRow,17, "inogor", headerStyle);
        createCell(headerRow,18, "smo", headerStyle);
        createCell(headerRow,19, "type", headerStyle);

        int rowCount = 1;

        CellStyle rowStyle = workbook.createCellStyle();

        for (ErrExtended item : list) {
            SXSSFRow row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, item.getS_com(), rowStyle);
            createCell(row, columnCount++, item.getError(), rowStyle);
            createCell(row, columnCount++, item.getNpolis(), rowStyle);
            createCell(row, columnCount++, item.getFio(), rowStyle);
            createCell(row, columnCount++, item.getBirthDate(), rowStyle);
            createCell(row, columnCount++, item.getDate_in(), rowStyle);
            createCell(row, columnCount++, item.getDate_out(), rowStyle);
            createCell(row, columnCount++, item.getRefreason(), rowStyle);
            createCell(row, columnCount++, item.getSumvUsl(), rowStyle);
            createCell(row, columnCount++, item.getCodeUsl(), rowStyle);
            createCell(row, columnCount++, item.getSankSum(), rowStyle);
            createCell(row, columnCount++, item.getDiagnosis(), rowStyle);
            createCell(row, columnCount++, item.getNameMO(), rowStyle);
            createCell(row, columnCount++, item.getDocCode(), rowStyle);
            createCell(row, columnCount++, item.getIdstrax(), rowStyle);
            createCell(row, columnCount++, item.getNhistory(), rowStyle);
            createCell(row, columnCount++, item.getErrorCode(), rowStyle);
            createCell(row, columnCount++, item.isInogor(), rowStyle);
            createCell(row, columnCount++, item.getSmo(), rowStyle);
            createCell(row, columnCount++, item.getType(), rowStyle);
        }

        String fileName = "report-" + LocalDateTime.now().format(formatter) + ".xlsx";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите место сохранения файла");
        fileChooser.setInitialFileName(fileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            try (FileOutputStream outputStream = new FileOutputStream(selectedFile);
                 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
                workbook.write(bufferedOutputStream);
                workbook.close();
                AlertDialogUtils.showInfoAlert("Информация", null, "Файл отчета успешно сохранен в: " + selectedFile.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
