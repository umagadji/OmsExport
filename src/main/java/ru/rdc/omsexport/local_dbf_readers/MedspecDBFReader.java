package ru.rdc.omsexport.local_dbf_readers;

import com.linuxense.javadbf.DBFReader;
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.local_db_models.Medspec;
import ru.rdc.omsexport.utils.AlertDialogUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Класс читает таблицу из DBF-файла и создает коллекцию с данными. Таблица medspec
public class MedspecDBFReader {
    public static List<Medspec> readDbf(String path, String charsetName) {
        List<Medspec> list = new ArrayList<>();
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
                Medspec item = new Medspec();

                if (!rowList.get(k).get("IDMSP").equals("null")) {
                    item.setIdmsp(rowList.get(k).get("IDMSP"));
                }

                if (!rowList.get(k).get("MSPNAME").equals("null")) {
                    item.setMspname(rowList.get(k).get("MSPNAME"));
                }

                if (!rowList.get(k).get("Q_PROF").equals("null")) {
                    item.setQ_prof(Integer.parseInt(rowList.get(k).get("Q_PROF")));
                }

                if (!rowList.get(k).get("DOST").equals("null")) {
                    item.setDost(Boolean.parseBoolean(rowList.get(k).get("DOST")));
                }

                if (!rowList.get(k).get("IDMSPN").equals("null")) {
                    item.setIdmspn(Integer.parseInt(rowList.get(k).get("IDMSPN")));
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
