package ru.rdc.omsexport.mek.utils;

import com.linuxense.javadbf.DBFReader;
import ru.rdc.omsexport.cards_model.Cards;
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.mek.models.Plan;
import ru.rdc.omsexport.utils.AlertDialogUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

//Читает из plan.dbf
public class ReadPlanDBF {
    public static List<Plan> readPlanDBF(String path, String charsetName) {
        List<Plan> list = new ArrayList<>();
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
                Plan item = new Plan();

                if (!rowList.get(k).get("CODE_USL").trim().equals("null")) {
                    item.setCode_usl(rowList.get(k).get("CODE_USL"));
                }

                if (!rowList.get(k).get("NAME_ISSL").trim().equals("null")) {
                    item.setName_issl(rowList.get(k).get("NAME_ISSL"));
                }

                if (!rowList.get(k).get("TYPE").trim().equals("null")) {
                    item.setType(rowList.get(k).get("TYPE"));
                }

                list.add(item);
            }

            dbfReader.close();
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            System.out.println("Файл " + path + " не найден");
            AlertDialogUtils.showErrorAlert("Ошибка", null, "Нет файлов для чтения или папка " + AppConstants.planFilePath + " отсутствует");
        }

        return list;
    }
}
