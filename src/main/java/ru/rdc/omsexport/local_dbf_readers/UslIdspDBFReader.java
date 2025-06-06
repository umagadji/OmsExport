package ru.rdc.omsexport.local_dbf_readers;

import com.linuxense.javadbf.DBFReader;
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.local_db_models.UslIdsp;
import ru.rdc.omsexport.local_db_models.UslKratnostMulti;
import ru.rdc.omsexport.utils.AlertDialogUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Класс читает таблицу из DBF-файла и создает коллекцию с данными. Таблица usl_idsp
public class UslIdspDBFReader {
    public static List<UslIdsp> readDbf(String path, String charsetName) {
        List<UslIdsp> list = new ArrayList<>();
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
                UslIdsp item = new UslIdsp();

                if (!rowList.get(k).get("KSG").equals("null")) {
                    item.setKsg(rowList.get(k).get("KSG"));
                }

                if (!rowList.get(k).get("VID_USL").equals("null")) {
                    item.setVid_usl(Integer.parseInt(rowList.get(k).get("VID_USL")));
                }

                if (!rowList.get(k).get("NAME_VID").equals("null")) {
                    item.setName_vid(rowList.get(k).get("NAME_VID"));
                }

                if (!rowList.get(k).get("IDSP_NAME").equals("null")) {
                    item.setIdsp_name(rowList.get(k).get("IDSP_NAME"));
                }

                if (!rowList.get(k).get("IDSP").equals("null")) {
                    item.setIdsp(Integer.parseInt(rowList.get(k).get("IDSP")));
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
