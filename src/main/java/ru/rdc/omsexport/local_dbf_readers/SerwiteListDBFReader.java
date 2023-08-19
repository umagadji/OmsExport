package ru.rdc.omsexport.local_dbf_readers;

import com.linuxense.javadbf.DBFReader;
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.local_db_models.Serwitelist;
import ru.rdc.omsexport.utils.AlertDialogUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Класс читает таблицу из DBF-файла и создает коллекцию с данными. Таблица usl_kr_multi
public class SerwiteListDBFReader {
    public static List<Serwitelist> readDbf(String path, String charsetName) {
        List<Serwitelist> list = new ArrayList<>();
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
                Serwitelist item = new Serwitelist();

                if (!rowList.get(k).get("SER").equals("null")) {
                    item.setSer(rowList.get(k).get("SER"));
                }

                if (!rowList.get(k).get("NAM_RAION").equals("null")) {
                    item.setNam_region(rowList.get(k).get("NAM_RAION"));
                }

                if (!rowList.get(k).get("TIP").equals("null")) {
                    item.setTip(rowList.get(k).get("TIP"));
                }

                if (!rowList.get(k).get("LPU").equals("null")) {
                    item.setLpu(Integer.parseInt(rowList.get(k).get("LPU")));
                }

                if (!rowList.get(k).get("OMSDOCTYPE").equals("null")) {
                    item.setOmsdoctype(Integer.parseInt(rowList.get(k).get("OMSDOCTYPE")));
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
