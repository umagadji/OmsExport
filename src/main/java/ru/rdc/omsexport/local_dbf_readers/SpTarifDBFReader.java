package ru.rdc.omsexport.local_dbf_readers;

import com.linuxense.javadbf.DBFReader;
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.local_db_models.SpTarif;
import ru.rdc.omsexport.utils.AlertDialogUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Класс читает таблицу из DBF-файла и создает коллекцию с данными. Таблица sp_tarif
public class SpTarifDBFReader {
    public static List<SpTarif> readDbf(String path, String charsetName) {
        List<SpTarif> list = new ArrayList<>();
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
                SpTarif item = new SpTarif();

                if (!rowList.get(k).get("NAME_ISSL").equals("null")) {
                    item.setName_issl(rowList.get(k).get("NAME_ISSL"));
                }

                if (!rowList.get(k).get("KSG").equals("null")) {
                    item.setKsg(rowList.get(k).get("KSG"));
                }

                if (!rowList.get(k).get("PRICE").equals("null")) {
                    item.setPrice(Double.parseDouble(rowList.get(k).get("PRICE")));
                }

                if (!rowList.get(k).get("TYPE").equals("null")) {
                    item.setType(Integer.parseInt(rowList.get(k).get("TYPE")));
                }

                if (!rowList.get(k).get("IDPR").equals("null")) {
                    item.setIdpr(Integer.parseInt(rowList.get(k).get("IDPR")));
                }

                if (!rowList.get(k).get("IDUMP").equals("null")) {
                    item.setIdump(Integer.parseInt(rowList.get(k).get("IDUMP")));
                }

                if (!rowList.get(k).get("KOL_USL").equals("null")) {
                    item.setKol_usl(Double.parseDouble(rowList.get(k).get("KOL_USL")));
                }

                if (!rowList.get(k).get("T_TYPE").equals("null")) {
                    item.setT_type(Integer.parseInt(rowList.get(k).get("T_TYPE")));
                }

                if (!rowList.get(k).get("IDVMP").equals("null")) {
                    item.setIdvmp(Integer.parseInt(rowList.get(k).get("IDVMP")));
                }

                if (!rowList.get(k).get("IDPC").equals("null")) {
                    item.setIdpc(rowList.get(k).get("IDPC"));
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
