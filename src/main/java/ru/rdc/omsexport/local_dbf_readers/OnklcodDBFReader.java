package ru.rdc.omsexport.local_dbf_readers;

import com.linuxense.javadbf.DBFReader;
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.local_db_models.Onklcod;
import ru.rdc.omsexport.utils.AlertDialogUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Класс читает таблицу из DBF-файла и создает коллекцию с данными. Таблица onklcod
public class OnklcodDBFReader {
    public static List<Onklcod> readDbf(String path, String charsetName) {
        List<Onklcod> list = new ArrayList<>();
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
                Onklcod item = new Onklcod();

                if (!rowList.get(k).get("LCOD").equals("null")) {
                    item.setLcod(rowList.get(k).get("LCOD"));
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
