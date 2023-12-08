package ru.rdc.omsexport.utils;

import com.linuxense.javadbf.DBFReader;
import ru.rdc.omsexport.cards_model.Cards;
import ru.rdc.omsexport.constants.AppConstants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

//Читает из cards для диагностических отделений данные
public class ReadCardsDBF {
    public static List<Cards> readCardsDBF(String path, String charsetName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd hh:mm:ss z yyyy", Locale.ENGLISH);
        List<Cards> list = new ArrayList<>();
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
                Cards card = new Cards();

                if (!rowList.get(k).get("N_TAL").trim().equals("null")) {
                    card.setN_tal(Long.parseLong(rowList.get(k).get("N_TAL")));
                }

                if (!rowList.get(k).get("SPOLIS").trim().equals("null")) {
                    card.setSpolis(rowList.get(k).get("SPOLIS"));
                } else {
                    card.setSpolis("");
                }

                if (!rowList.get(k).get("NPOLIS").equals("null")) {
                    card.setNpolis(rowList.get(k).get("NPOLIS"));
                }

                if (!rowList.get(k).get("VPOLIS").trim().equals("null")) {
                    card.setVpolis(Integer.parseInt(rowList.get(k).get("VPOLIS")));
                }

                if (!rowList.get(k).get("SMOCOD").trim().equals("null")) {
                    card.setSmocod(rowList.get(k).get("SMOCOD"));
                }

                if (!rowList.get(k).get("SMO_NM").trim().equals("null")) {
                    card.setSmo_nm(rowList.get(k).get("SMO_NM"));
                }

                if (!rowList.get(k).get("FAM").trim().equals("null")) {
                    card.setFam(rowList.get(k).get("FAM"));
                }

                if (!rowList.get(k).get("IM").trim().equals("null")) {
                    card.setIm(rowList.get(k).get("IM"));
                }

                if (!rowList.get(k).get("OT").trim().equals("null")) {
                    card.setOt(rowList.get(k).get("OT"));
                }

                if (!rowList.get(k).get("IS_MALE").equals("null")) {
                    card.set_male(Boolean.parseBoolean(rowList.get(k).get("IS_MALE")));
                }

                if (!rowList.get(k).get("DAT_ROJD").trim().equals("null")) {
                    card.setDat_rojd(LocalDate.parse(rowList.get(k).get("DAT_ROJD"), formatter));
                }

                if (!rowList.get(k).get("NOVOR").equals("null")) {
                    card.setNovor(Boolean.parseBoolean(rowList.get(k).get("NOVOR")));
                }

                if (!rowList.get(k).get("FAM_N").trim().equals("null")) {
                    card.setFam_n(rowList.get(k).get("FAM_N"));
                }

                if (!rowList.get(k).get("IM_N").trim().equals("null")) {
                    card.setIm_n(rowList.get(k).get("IM_N"));
                }

                if (!rowList.get(k).get("OT_N").trim().equals("null")) {
                    card.setOt_n(rowList.get(k).get("OT_N"));
                }

                if (!rowList.get(k).get("IS_MALE_N").equals("null")) {
                    card.set_male_n(Boolean.parseBoolean(rowList.get(k).get("IS_MALE_N")));
                }

                if (!rowList.get(k).get("DAT_ROJD_N").trim().equals("null")) {
                    card.setDat_rojd_n(LocalDate.parse(rowList.get(k).get("DAT_ROJD_N"), formatter));
                }

                //Убирал .trim() чтобы текстовая строка парсилась корректно
                if (!rowList.get(k).get("INOGOR").equals("null")) {
                    card.setInogor(Boolean.parseBoolean(rowList.get(k).get("INOGOR")));
                }

                if (!rowList.get(k).get("MR").trim().equals("null")) {
                    card.setMr(rowList.get(k).get("MR"));
                }

                if (!rowList.get(k).get("DOCTYPE").equals("null")) {
                    card.setDoctype(Integer.parseInt(rowList.get(k).get("DOCTYPE")));
                }

                if (!rowList.get(k).get("DOCSER").equals("null")) {
                    card.setDocser(rowList.get(k).get("DOCSER"));
                }

                if (!rowList.get(k).get("DOCNUM").equals("null")) {
                    card.setDocnum(rowList.get(k).get("DOCNUM"));
                }

                if (!rowList.get(k).get("DOCDATE").trim().equals("null")) {
                    card.setDocdate(LocalDate.parse(rowList.get(k).get("DOCDATE"), formatter));
                }

                if (!rowList.get(k).get("DOCORG").equals("null")) {
                    card.setDocorg(rowList.get(k).get("DOCORG"));
                }

                if (!rowList.get(k).get("SNILS").equals("null")) {
                    card.setSnils(rowList.get(k).get("SNILS"));
                }

                if (!rowList.get(k).get("ADRES").equals("null")) {
                    card.setAdres(rowList.get(k).get("ADRES"));
                }

                if (!rowList.get(k).get("SMO_TERR").equals("null")) {
                    card.setSmo_terr(rowList.get(k).get("SMO_TERR"));
                }

                if (!rowList.get(k).get("DATE_IN").equals("null")) {
                    card.setDate_in(LocalDate.parse(rowList.get(k).get("DATE_IN"), formatter));
                }

                if (!rowList.get(k).get("DATE_OUT").equals("null")) {
                    card.setDate_out(LocalDate.parse(rowList.get(k).get("DATE_OUT"), formatter));
                }

                if (!rowList.get(k).get("N_OTD").equals("null")) {
                    card.setOtd(Integer.parseInt(rowList.get(k).get("N_OTD")));
                }

                if (!rowList.get(k).get("N_CAB").equals("null")) {
                    card.setN_cab(Long.parseLong(rowList.get(k).get("N_CAB")));
                }

                if (!rowList.get(k).get("CAB_NAME").equals("null")) {
                    card.setCab_name(rowList.get(k).get("CAB_NAME"));
                }

                if (!rowList.get(k).get("MET_PR_KOD").equals("null")) {
                    card.setMet_pr_kod(rowList.get(k).get("MET_PR_KOD"));
                }

                if (!rowList.get(k).get("N_MET").equals("null")) {
                    card.setN_met(Integer.parseInt(rowList.get(k).get("N_MET")));
                }

                if (!rowList.get(k).get("MET_NAME").equals("null")) {
                    card.setMet_name(rowList.get(k).get("MET_NAME"));
                }

                if (!rowList.get(k).get("NOM_REG").equals("null")) {
                    card.setNom_reg(Long.parseLong(rowList.get(k).get("NOM_REG")));
                }

                if (!rowList.get(k).get("LPU").equals("null")) {
                    card.setLpu(Long.parseLong(rowList.get(k).get("LPU")));
                }

                if (!rowList.get(k).get("LPU_NAME").equals("null")) {
                    card.setLpu_name(rowList.get(k).get("LPU_NAME"));
                }

                if (!rowList.get(k).get("LPU_SHNM").equals("null")) {
                    card.setLpu_shnm(rowList.get(k).get("LPU_SHNM"));
                }

                if (!rowList.get(k).get("LPU_N_LN").equals("null")) {
                    card.setLpu_n_ln(rowList.get(k).get("LPU_N_LN"));
                }

                if (!rowList.get(k).get("RSLT").equals("null")) {
                    card.setRslt(Long.parseLong(rowList.get(k).get("RSLT")));
                }

                if (!rowList.get(k).get("ISHOD").equals("null")) {
                    card.setIshod(Long.parseLong(rowList.get(k).get("ISHOD")));
                }

                if (!rowList.get(k).get("CODE_USL").equals("null")) {
                    card.setCode_usl(rowList.get(k).get("CODE_USL"));
                }

                if (!rowList.get(k).get("MKB_CODE").equals("null")) {
                    card.setMkb_code(rowList.get(k).get("MKB_CODE"));
                }

                if (!rowList.get(k).get("TARIF").equals("null")) {
                    card.setTarif(Double.parseDouble(rowList.get(k).get("TARIF")));
                }

                if (!rowList.get(k).get("COEFF").equals("null")) {
                    card.setCoeff(Double.parseDouble(rowList.get(k).get("COEFF")));
                }

                if (!rowList.get(k).get("KOL_USL").equals("null")) {
                    card.setKol_usl(Double.parseDouble(rowList.get(k).get("KOL_USL")));
                }

                if (!rowList.get(k).get("CODE_MD").equals("null")) {
                    if (rowList.get(k).get("CODE_MD").trim().length() == 3) {
                        card.setCode_md("0" + rowList.get(k).get("CODE_MD"));
                    } else {
                        card.setCode_md(rowList.get(k).get("CODE_MD"));
                    }
                }

                if (!rowList.get(k).get("VR_FIO").equals("null")) {
                    card.setVr_fio(rowList.get(k).get("VR_FIO"));
                }

                if (!rowList.get(k).get("PRVS").equals("null")) {
                    card.setPrvs(Integer.parseInt(rowList.get(k).get("PRVS")));
                }

                if (!rowList.get(k).get("VR_SPNM").equals("null")) {
                    card.setVr_spnm(rowList.get(k).get("VR_SPNM"));
                }

                if (!rowList.get(k).get("MKB_CODE_S").equals("null")) {
                    card.setMkb_code_s(rowList.get(k).get("MKB_CODE_S"));
                }

                if (!rowList.get(k).get("MET_CMNT").equals("null")) {
                    card.setMet_cmnt(rowList.get(k).get("MET_CMNT"));
                }

                if (!rowList.get(k).get("N_MKP").equals("null")) {
                    card.setN_mkp(Long.parseLong(rowList.get(k).get("N_MKP")));
                }

                if (!rowList.get(k).get("MKB_CODE_P").equals("null")) {
                    card.setMkb_code_p(rowList.get(k).get("MKB_CODE_P"));
                }

                if (!rowList.get(k).get("CHAR_ZAB").equals("null")) {
                    card.setChar_zab(Integer.parseInt(rowList.get(k).get("CHAR_ZAB")));
                }

                if (!rowList.get(k).get("VISITID").equals("null")) {
                    card.setVisitid(Long.parseLong(rowList.get(k).get("VISITID")));
                }

                list.add(card);
            }

            dbfReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл " + path + " не найден");
            AlertDialogUtils.showErrorAlert("Ошибка", null, "Нет файлов для чтения или папка " + AppConstants.cardsDBFPath + " отсутствует");
        }

        return list;
    }
}
