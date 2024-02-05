package ru.rdc.omsexport.utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rdc.omsexport.cards_model.Cards;
import ru.rdc.omsexport.cards_model.CommentCountsDTO;
import ru.rdc.omsexport.services.CardsService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

//Класс для получения отчетов по услугам
@Service
public class ReportsClass {
    private SXSSFWorkbook workbook;
    private SXSSFSheet sheet;

    private final CardsService cardsService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private String yearMonth = ""; //Год и месяц для названия файла отчета

    @Autowired
    public ReportsClass(CardsService cardsService) {
        this.cardsService = cardsService;
        workbook = new SXSSFWorkbook();
    }

    public void getIncorrectUslFromDatabase(Stage stage) {
        //Получаем некорректные услуги из базы данных
        List<Cards> list = cardsService.findAllByCorrect(false);
        yearMonth = getYearMonth(list);
        generateCardsExcelFile(list, stage);
    }

    public void getAllUsl(Stage stage) {
        List<Cards> list = cardsService.getCardsList();
        yearMonth = getYearMonth(list);
        generateCardsExcelFile(list, stage);
    }

    public void getErrorsType(Stage stage) {
        List<CommentCountsDTO> list = cardsService.getCommentsCounter();
        generateCommentCountersExcelFile(list, stage);
    }

    //Метод для получения даты и месяца отчетного периода из списка услуг для отчетов
    private String getYearMonth(List<Cards> list) {
        //Устанавливаем дату и месяц для названия файла
        LocalDate date = list.get(0).getDate_in(); //Получаем дату начала первой услуги в таблице cards
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMM");
        return date.format(dateTimeFormatter);
    }

    //Метод создает заголовки в Excel документе
    private void writeCardsHeader() {
        sheet = workbook.createSheet("Cards");
        SXSSFRow row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        createCell(row,0, "n_mkp", style);
        createCell(row,1, "comment", style);
        createCell(row,2, "vpolis", style);
        createCell(row,3, "snpol", style);
        createCell(row,4, "fam", style);
        createCell(row,5, "im", style);
        createCell(row,6, "ot", style);
        createCell(row,7, "dat_rojd", style);
        createCell(row,8, "date_in", style);
        createCell(row,9, "date_out", style);
        createCell(row,10, "code_usl", style);
        createCell(row,11, "kol_usl", style);
        createCell(row,12, "otd", style);
        createCell(row,13, "vr_fio", style);
        createCell(row,14, "correct", style);
        createCell(row,15, "inogor", style);
        createCell(row,16, "is_male", style);
        createCell(row,17, "lpu", style);
        createCell(row,18, "lpu_name", style);
        createCell(row,19, "lpu_shnm", style);
        createCell(row,20, "met_name", style);
        createCell(row,21, "met_pr_kod", style);
        createCell(row,22, "mkb_code", style);
        createCell(row,23, "mkb_code_p", style);
        createCell(row,24, "n_cab", style);
        createCell(row,25, "novor", style);
        createCell(row,26, "profil", style);
        createCell(row,27, "prvs", style);
        createCell(row,28, "smo_nm", style);
        createCell(row,29, "smocod", style);
        createCell(row,30, "tarif", style);
    }

    //Метод создает ячейку
    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        //sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
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

    //Метод записывает в Excel
    private void writeCards(List<Cards> list) {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        style.setFont(font);

        for (Cards item : list) {
            SXSSFRow row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, item.getN_mkp(), style);
            createCell(row, columnCount++, item.getComment(), style);
            createCell(row, columnCount++, item.getVpolis(), style);
            createCell(row, columnCount++, item.getSnPol(), style);
            createCell(row, columnCount++, item.getFam(), style);
            createCell(row, columnCount++, item.getIm(), style);
            createCell(row, columnCount++, item.getOt(), style);
            createCell(row, columnCount++, item.getDat_rojd().format(formatter), style);
            createCell(row, columnCount++, item.getDate_in().format(formatter), style);
            createCell(row, columnCount++, item.getDate_out().format(formatter), style);
            createCell(row, columnCount++, item.getCode_usl(), style);
            createCell(row, columnCount++, item.getKol_usl(), style);
            createCell(row, columnCount++, item.getOtd(), style);
            createCell(row, columnCount++, item.getVr_fio(), style);
            createCell(row, columnCount++, item.isCorrect(), style);
            createCell(row, columnCount++, item.isInogor(), style);
            createCell(row, columnCount++, item.is_male(), style);
            createCell(row, columnCount++, item.getLpu(), style);
            createCell(row, columnCount++, item.getLpu_name(), style);
            createCell(row, columnCount++, item.getLpu_shnm(), style);
            createCell(row, columnCount++, item.getMet_name(), style);
            createCell(row, columnCount++, item.getMetPrKod(), style);
            createCell(row, columnCount++, item.getMkb_code(), style);
            createCell(row, columnCount++, item.getMkb_code_p(), style);
            createCell(row, columnCount++, item.getN_cab(), style);
            createCell(row, columnCount++, item.isNovor(), style);
            createCell(row, columnCount++, item.getProfil(), style);
            createCell(row, columnCount++, item.getPrvs(), style);
            createCell(row, columnCount++, item.getSmo_nm(), style);
            createCell(row, columnCount++, item.getSmocod(), style);
            createCell(row, columnCount++, item.getTarif(), style);
        }
    }

    //Сохраняем файл в Excel формат
    private String generateCardsExcelFile(List<Cards> list, Stage stage) {
        String fileName = "report-" + yearMonth + "-" + LocalDateTime.now().format(formatter) + ".xlsx";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите место сохранения файла");
        fileChooser.setInitialFileName(fileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            try (FileOutputStream outputStream = new FileOutputStream(selectedFile);
                 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
                writeCardsHeader();
                writeCards(list);
                workbook.write(bufferedOutputStream);
                workbook.close();
                AlertDialogUtils.showInfoAlert("Информация", null, "Файл отчета успешно сохранен в: " + selectedFile.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /*File dir = new File(AppConstants.reportsFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (FileOutputStream outputStream = new FileOutputStream(AppConstants.reportsFilePath + fileName);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            writeCardsHeader();
            writeCards(list);
            workbook.write(bufferedOutputStream);
            workbook.close();
            AlertDialogUtils.showInfoAlert("Информация", null, "Файл отчета успешно сформирован в: " + AppConstants.reportsFilePath + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        return fileName;
    }

    //Метод создает заголовки в Excel документе
    private void writeCommentCountersHeader() {
        sheet = workbook.createSheet("Errors");
        SXSSFRow row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        createCell(row,0, "Ошибка", style);
        createCell(row,1, "Кол-во", style);
    }

    //Метод записывает в Excel
    private void writeCommentCounters(List<CommentCountsDTO> list) {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        style.setFont(font);

        for (CommentCountsDTO item : list) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, item.getComment(), style);
            createCell(row, columnCount++, item.getCount(), style);
        }
    }

    //Сохраняем файл в Excel формат
    private String generateCommentCountersExcelFile(List<CommentCountsDTO> list, Stage stage) {
        String fileName = "report-" + LocalDateTime.now().format(formatter) + ".xlsx";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите место сохранения файла");
        fileChooser.setInitialFileName(fileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            try (FileOutputStream outputStream = new FileOutputStream(selectedFile);
                 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
                writeCommentCountersHeader();
                writeCommentCounters(list);
                workbook.write(bufferedOutputStream);
                workbook.close();
                AlertDialogUtils.showInfoAlert("Информация", null, "Файл отчета успешно сохранен в: " + selectedFile.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /*File dir = new File(AppConstants.reportsFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (FileOutputStream outputStream = new FileOutputStream(AppConstants.reportsFilePath + fileName);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            writeCommentCountersHeader();
            writeCommentCounters(list);
            workbook.write(bufferedOutputStream);
            workbook.close();
            AlertDialogUtils.showInfoAlert("Информация", null, "Файл отчета успешно сформирован в: " + AppConstants.reportsFilePath + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        return fileName;
    }

}
