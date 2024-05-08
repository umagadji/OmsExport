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
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.services.CardsService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

    //Для автоматического сохранения файла после обработки файлов
    public void getAllUslAutomatic(Stage stage) {
        List<Cards> list = cardsService.getCardsList();
        yearMonth = getYearMonth(list);
        generateCardsExcel(list, stage);
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

        createCell(row,	0, "id", style);
        createCell(row,	1, "n_tal", style);
        createCell(row,	2, "spolis", style);
        createCell(row,	3, "npolis", style);
        createCell(row,	4, "vpolis", style);
        createCell(row,	5, "smocod", style);
        createCell(row,	6, "smo_nm", style);
        createCell(row,	7, "fam", style);
        createCell(row,	8, "im", style);
        createCell(row,	9, "ot", style);
        createCell(row,	10, "is_male", style);
        createCell(row,	11, "dat_rojd", style);
        createCell(row,	12, "novor", style);
        createCell(row,	13, "fam_n", style);
        createCell(row,	14, "im_n", style);
        createCell(row,	15, "ot_n", style);
        createCell(row,	16, "is_male_n", style);
        createCell(row,	17, "dat_rojd_n", style);
        createCell(row,	18, "inogor", style);
        createCell(row,	19, "mr", style);
        createCell(row,	20, "doctype", style);
        createCell(row,	21, "docser", style);
        createCell(row,	22, "docnum", style);
        createCell(row,	23, "docdate", style);
        createCell(row,	24, "docorg", style);
        createCell(row,	25, "snils", style);
        createCell(row,	26, "adres", style);
        createCell(row,	27, "smo_terr", style);
        createCell(row,	28, "date_in", style);
        createCell(row,	29, "date_out", style);
        createCell(row,	30, "otd", style);
        createCell(row,	31, "n_cab", style);
        createCell(row,	32, "cab_name", style);
        createCell(row,	33, "metPrKod", style);
        createCell(row,	34, "n_met", style);
        createCell(row,	35, "met_name", style);
        createCell(row,	36, "nom_reg", style);
        createCell(row,	37, "lpu", style);
        createCell(row,	38, "lpu_name", style);
        createCell(row,	39, "lpu_shnm", style);
        createCell(row,	40, "lpu_n_ln", style);
        createCell(row,	41, "rslt", style);
        createCell(row,	42, "ishod", style);
        createCell(row,	43, "code_usl", style);
        createCell(row,	44, "mkb_code", style);
        createCell(row,	45, "tarif", style);
        createCell(row,	46, "coeff", style);
        createCell(row,	47, "kol_usl", style);
        createCell(row,	48, "code_md", style);
        createCell(row,	49, "vr_fio", style);
        createCell(row,	50, "prvs", style);
        createCell(row,	51, "vr_spnm", style);
        createCell(row,	52, "mkb_code_s", style);
        createCell(row,	53, "met_cmnt", style);
        createCell(row,	54, "n_mkp", style);
        createCell(row,	55, "mkb_code_p", style);
        createCell(row,	56, "char_zab", style);
        createCell(row,	57, "visitid", style);
        createCell(row,	58, "correct", style);
        createCell(row,	59, "comment", style);
        createCell(row,	60, "t_type", style);
        createCell(row,	61, "is_onkl", style);
        createCell(row,	62, "muvr", style);
        createCell(row,	63, "profil", style);
        createCell(row,	64, "usl_idsp", style);
        createCell(row,	65, "mcod", style);

        /*createCell(row,0, "n_mkp", style);
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
        createCell(row,30, "tarif", style);*/
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
        } else if (valueOfCell instanceof Float){
            cell.setCellValue((Float) valueOfCell);
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

            createCell(row, columnCount++, item.getId(), style);
            createCell(row, columnCount++, item.getN_tal(), style);
            createCell(row, columnCount++, item.getSpolis(), style);
            createCell(row, columnCount++, item.getNpolis(), style);
            createCell(row, columnCount++, item.getVpolis(), style);
            createCell(row, columnCount++, item.getSmocod(), style);
            createCell(row, columnCount++, item.getSmo_nm(), style);
            createCell(row, columnCount++, item.getFam(), style);
            createCell(row, columnCount++, item.getIm(), style);
            createCell(row, columnCount++, item.getOt(), style);
            createCell(row, columnCount++, item.is_male(), style);
            createCell(row, columnCount++, item.getDat_rojd().format(formatter), style);
            createCell(row, columnCount++, item.isNovor(), style);
            createCell(row, columnCount++, item.getFam_n(), style);
            createCell(row, columnCount++, item.getIm_n(), style);
            createCell(row, columnCount++, item.getOt_n(), style);
            createCell(row, columnCount++, item.is_male_n(), style);

            if (item.getDat_rojd_n() != null) {
                createCell(row, columnCount++, item.getDat_rojd_n().format(formatter), style);
            } else {
                createCell(row, columnCount++, null, style);
            }

            createCell(row, columnCount++, item.isInogor(), style);
            createCell(row, columnCount++, item.getMr(), style);
            createCell(row, columnCount++, item.getDoctype(), style);
            createCell(row, columnCount++, item.getDocser(), style);
            createCell(row, columnCount++, item.getDocnum(), style);
            createCell(row, columnCount++, item.getDocdate(), style);
            createCell(row, columnCount++, item.getDocorg(), style);
            createCell(row, columnCount++, item.getSnils(), style);
            createCell(row, columnCount++, item.getAdres(), style);
            createCell(row, columnCount++, item.getSmo_terr(), style);
            createCell(row, columnCount++, item.getDate_in().format(formatter), style);
            createCell(row, columnCount++, item.getDate_out().format(formatter), style);
            createCell(row, columnCount++, item.getOtd(), style);
            createCell(row, columnCount++, item.getN_cab(), style);
            createCell(row, columnCount++, item.getCab_name(), style);
            createCell(row, columnCount++, item.getMetPrKod(), style);
            createCell(row, columnCount++, item.getN_met(), style);
            createCell(row, columnCount++, item.getMet_name(), style);
            createCell(row, columnCount++, item.getNom_reg(), style);
            createCell(row, columnCount++, item.getLpu(), style);
            createCell(row, columnCount++, item.getLpu_name(), style);
            createCell(row, columnCount++, item.getLpu_shnm(), style);
            createCell(row, columnCount++, item.getLpu_n_ln(), style);
            createCell(row, columnCount++, item.getRslt(), style);
            createCell(row, columnCount++, item.getIshod(), style);
            createCell(row, columnCount++, item.getCode_usl(), style);
            createCell(row, columnCount++, item.getMkb_code(), style);
            createCell(row, columnCount++, item.getTarif(), style);
            createCell(row, columnCount++, item.getCoeff(), style);
            createCell(row, columnCount++, item.getKol_usl(), style);
            createCell(row, columnCount++, item.getCode_md(), style);
            createCell(row, columnCount++, item.getVr_fio(), style);
            createCell(row, columnCount++, item.getPrvs(), style);
            createCell(row, columnCount++, item.getVr_spnm(), style);
            createCell(row, columnCount++, item.getMkb_code_s(), style);
            createCell(row, columnCount++, item.getMet_cmnt(), style);
            createCell(row, columnCount++, item.getN_mkp(), style);
            createCell(row, columnCount++, item.getMkb_code_p(), style);
            createCell(row, columnCount++, item.getChar_zab(), style);
            createCell(row, columnCount++, item.getVisitid(), style);
            createCell(row, columnCount++, item.isCorrect(), style);
            createCell(row, columnCount++, item.getComment(), style);
            createCell(row, columnCount++, item.getT_type(), style);
            createCell(row, columnCount++, item.is_onkl(), style);
            createCell(row, columnCount++, item.isMuvr(), style);
            createCell(row, columnCount++, item.getProfil(), style);
            createCell(row, columnCount++, item.getUsl_idsp(), style);
            createCell(row, columnCount++, item.getMcod(), style);

            /*createCell(row, columnCount++, item.getN_mkp(), style);
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
            createCell(row, columnCount++, item.getTarif(), style);*/
        }
    }

    //Метод для сохранения данных из cards в Excel. Используется только для автоматического создания файла после обработки файлов
    private String generateCardsExcel(List<Cards> list, Stage stage) {
        String fileName = "report-" + yearMonth + "-" + LocalDateTime.now().format(formatter) + "-" + System.currentTimeMillis() + ".xlsx";

        File dir = new File(AppConstants.outputFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(AppConstants.outputFilePath + fileName);

        try (FileOutputStream outputStream = new FileOutputStream(file);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            writeCardsHeader();
            writeCards(list);
            workbook.write(bufferedOutputStream);
            workbook.close();
            AlertDialogUtils.showInfoAlert("Информация", null, "Файл с данными cards успешно сохранен в: " + file.getAbsolutePath() + " в " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileName;
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
