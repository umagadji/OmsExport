package ru.rdc.omsexport.local_dbf_readers;

import com.linuxense.javadbf.DBFReader;
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.local_db_models.Mkb;
import ru.rdc.omsexport.utils.AlertDialogUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Класс читает таблицу из DBF-файла и создает коллекцию с данными. Таблица mkb
public class MkbDBFReader {
    public static List<Mkb> readDbf(String path, String charsetName) {
        List<Mkb> list = new ArrayList<>();
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
                Mkb item = new Mkb();

                if (!rowList.get(k).get("LCOD").equals("null")) {
                    item.setLcod(rowList.get(k).get("LCOD"));
                }

                if (!rowList.get(k).get("NAME").equals("null")) {
                    item.setName(rowList.get(k).get("NAME"));
                }

                if (!rowList.get(k).get("POL").equals("null")) {
                    item.setPol(rowList.get(k).get("POL"));
                }

                if (!rowList.get(k).get("POL_M").equals("null")) {
                    item.setPol_m(Integer.parseInt(rowList.get(k).get("POL_M")));
                }

                if (!rowList.get(k).get("DETI").equals("null")) {
                    item.setDeti(rowList.get(k).get("DETI"));
                }

                if (!rowList.get(k).get("BAZ").equals("null")) {
                    item.setBaz(rowList.get(k).get("BAZ"));
                }

                if (!rowList.get(k).get("BAZ_M").equals("null")) {
                    item.setBaz_m(Integer.parseInt(rowList.get(k).get("BAZ_M")));
                }

                if (!rowList.get(k).get("TERR").equals("null")) {
                    item.setTerr(Integer.parseInt(rowList.get(k).get("TERR")));
                }

                if (!rowList.get(k).get("TERR_M").equals("null")) {
                    item.setTerr_m(Integer.parseInt(rowList.get(k).get("TERR_M")));
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
