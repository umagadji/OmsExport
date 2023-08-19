package ru.rdc.omsexport.local_dbf_readers;

import com.linuxense.javadbf.DBFReader;
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.local_db_models.Profot;
import ru.rdc.omsexport.utils.AlertDialogUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

//Класс читает таблицу из DBF-файла и создает коллекцию с данными. Таблица profot
public class ProfotDBFReader {
    public static List<Profot> readDbf(String path, String charsetName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd hh:mm:ss z yyyy", Locale.ENGLISH);
        List<Profot> list = new ArrayList<>();
        List<Map<String, String>> rowList = new ArrayList<>();
        DBFReader dbfReader = null;
        try {
            dbfReader = new DBFReader(new FileInputStream(path), Charset.forName(charsetName));

            Object[] rowValues;
            while ((rowValues = dbfReader.nextRecord()) != null) {
                Map<String, String> rowMap = new HashMap<String, String>();
                for (int i = 0; i < rowValues.length; i++) {
                    rowMap.put(dbfReader.getField(i).getName(), String.valueOf(rowValues[i]).trim());
                }
                rowList.add(rowMap);
            }

            for (int k = 0; k < rowList.size(); k++) {
                Profot item = new Profot();

                if (!rowList.get(k).get("IDPR").equals("null")) {
                    item.setIdpr(Integer.parseInt(rowList.get(k).get("IDPR")));
                }

                if (!rowList.get(k).get("PRNAME").equals("null")) {
                    item.setPrname(rowList.get(k).get("PRNAME"));
                }

                if (!rowList.get(k).get("DATEBEG").equals("null")) {
                    item.setDatebeg(LocalDate.parse(rowList.get(k).get("DATEBEG"), formatter));
                }

                if (!rowList.get(k).get("DATEEND").equals("null")) {
                    item.setDateend(LocalDate.parse(rowList.get(k).get("DATEEND"), formatter));
                }

                if (!rowList.get(k).get("ACTIV").equals("null")) {
                    item.setActiv(Boolean.parseBoolean(rowList.get(k).get("ACTIV")));
                }

                list.add(item);
            }

            dbfReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл " + path + " не найден");
            AlertDialogUtils.showErrorAlert("Ошибка", null, "Нет файлов для чтения или папка " + AppConstants.localDBFPath + " отсутствует");
        }

        return list;
    }
}
