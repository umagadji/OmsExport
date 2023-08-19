package ru.rdc.omsexport.local_dbf_readers;

import com.linuxense.javadbf.DBFReader;
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.local_db_models.Smo;
import ru.rdc.omsexport.utils.AlertDialogUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Класс читает таблицу из DBF-файла и создает коллекцию с данными. Таблица smo
public class SmoDBFReader {
    public static List<Smo> readDbf(String path, String charsetName) {
        List<Smo> list = new ArrayList<>();
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
                Smo item = new Smo();

                if (!rowList.get(k).get("TF_OKATO").equals("null")) {
                    item.setTf_okato(rowList.get(k).get("TF_OKATO"));
                }

                if (!rowList.get(k).get("SMOCOD").equals("null")) {
                    item.setSmocod(rowList.get(k).get("SMOCOD"));
                }

                if (!rowList.get(k).get("NAM_SMOK").equals("null")) {
                    item.setNam_smok(rowList.get(k).get("NAM_SMOK"));
                }

                if (!rowList.get(k).get("OGRN").equals("null")) {
                    item.setOgrn(rowList.get(k).get("OGRN"));
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
