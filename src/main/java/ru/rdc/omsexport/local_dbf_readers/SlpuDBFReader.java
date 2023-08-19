package ru.rdc.omsexport.local_dbf_readers;

import com.linuxense.javadbf.DBFReader;
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.local_db_models.Slpu;
import ru.rdc.omsexport.utils.AlertDialogUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Класс читает таблицу из DBF-файла и создает коллекцию с данными. Таблица s_lpu
public class SlpuDBFReader {
    public static List<Slpu> readDbf(String path, String charsetName) {
        List<Slpu> list = new ArrayList<>();
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
                Slpu item = new Slpu();

                if (!rowList.get(k).get("MCOD").equals("null")) {
                    item.setMcod(rowList.get(k).get("MCOD"));
                }

                if (!rowList.get(k).get("GLPU").equals("null")) {
                    item.setGlpu(rowList.get(k).get("GLPU"));
                }

                if (!rowList.get(k).get("TYPE").equals("null")) {
                    item.setType(Integer.parseInt(rowList.get(k).get("TYPE")));
                }

                if (!rowList.get(k).get("IDUMP").equals("null")) {
                    item.setIdump(Integer.parseInt(rowList.get(k).get("IDUMP")));
                }

                if (!rowList.get(k).get("NAME").equals("null")) {
                    item.setName(rowList.get(k).get("NAME"));
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
