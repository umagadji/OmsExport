package ru.rdc.omsexport.ds_pkg.ds_utils;

import ru.rdc.omsexport.ds_pkg.ds_models.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

//Класс для создания элементов ASUM файла по стационару
public class CreateAsumElementsDS {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void createSlKoef(RowDS row, UslDS usl) {
        if (!row.getKslp_code().trim().equals("")) {
            SlKoefDS slKoefDS = new SlKoefDS();
            slKoefDS.setIdusl(usl.getIdusl());
            slKoefDS.setIdsl(row.getKslp_code());
            slKoefDS.setZ_sl(row.getKslp_koeff());
            slKoefDS.setId(UUID.randomUUID().toString().toUpperCase());
            slKoefDS.setName_sl(row.getKslp_text());
            slKoefDS.setCost12("0");

            usl.setSlKoefDS(slKoefDS);
        }
    }

    //Создает операции
    public static void createHrrgd(RowDS row, UslDS usl) {
        if (!row.getOperac_code().trim().equals("")) {
            HrrgdDS hrrgd = new HrrgdDS();
            hrrgd.setIdusl(usl.getIdusl());
            hrrgd.setDate_o(usl.getDate_in());
            hrrgd.setHkod(row.getOperac_code());
            hrrgd.setName_o(row.getOperac_text());
            hrrgd.setNotksg("0");
            hrrgd.setPrice("0.00");
            hrrgd.setIdvidvme("");

            usl.setHrrgdDS(hrrgd);
        }
    }

    //Создает критерий
    public static void createCrit(RowDS row, UslDS usl) {
        //Проверяем есть ли КСЛП, Критерии, Операции
        if (!row.getCrit_code().trim().equals("")) { //Если getCrit_code() не пустой, значит есть критерий
            CritDS crit = new CritDS();
            crit.setIdusl(usl.getIdusl());
            crit.setCrit(row.getCrit_code());
            crit.setCname(row.getCrit_text());

            usl.setCritDS(crit);
        }
    }

    //Метод создает услугу
    public static UslDS createUsl(RowDS row, SluchDS sluch) {
        //СОЗДАЕМ УСЛУГУ
        UslDS usl = new UslDS();

        //ЗАПОЛНЯЕМ УСЛУГУ
        usl.setIdstrax(sluch.getId());
        usl.setIdusl(UUID.randomUUID().toString().toUpperCase());
        usl.setIdserv("0");
        usl.setSpolis(sluch.getSpolis());
        usl.setNpolis(sluch.getNpolis());
        usl.setGlpu(sluch.getGlpu());
        usl.setMcod(sluch.getMcod());
        usl.setPodr(sluch.getPodr());
        usl.setProfil(sluch.getProfil());
        usl.setDet(sluch.getDet());
        usl.setDate_in(sluch.getDate_1());
        usl.setDate_out(sluch.getDate_2());
        usl.setDs(sluch.getDs1());
        usl.setCode_usl(row.getUsl_ksg());
        usl.setEd_col(row.getKd());
        usl.setKol_usl(row.getKol_usl());
        usl.setTarif(row.getTarif());
        usl.setSumv_usl(row.getTarif());
        usl.setZak(row.getZakonch());
        usl.setStand("false");
        usl.setPrvs(sluch.getPrvs());
        usl.setCode_md(sluch.getIddokt());
        usl.setComentu("");
        usl.setUid("");
        usl.setDopsch(sluch.getDopsch());
        usl.setDir2("0");
        usl.setGr_zdorov("0");
        usl.setStudent("0");
        usl.setVid_vme("");

        if (row.getKslp_koeff().trim().equals("")) {
            usl.setKoefk("0.0");
        } else {
            usl.setKoefk(row.getKslp_koeff());
        }

        usl.setPouh("0");
        usl.setOtkaz2("0");
        usl.setNazna4("");
        usl.setP_per("0");
        usl.setNpl("0");
        usl.setIdsh("");
        usl.setIdsh2("");
        usl.setDn("0");
        usl.setDs_onk("0");
        usl.setP_cel("");
        usl.setProfil_k(row.getProfil_kd());
        usl.setIdsl("");
        usl.setMuvr("0");
        usl.setMuvr_lpu("");
        usl.setDate_usl("");

        //Пока ничего лучше не придумал как добавить к услуге врача
        /*usl.setVrachi(new VrachiDS(
                row.getDoctor_code()
                ,row.getDoctor()
                ,"05011D"
                ,row.getVr_spec()
                ,row.getVr_spec_name()
                ,"false"
                ,"2"
                ,""
                ,""
        ));*/

        return usl;
    }

    //Возвращает врача на основании записи RowDS
    public static VrachiDS getVrachDS(RowDS rowDS) {
        VrachiDS vrachiDS = new VrachiDS();
        vrachiDS.setKod(rowDS.getDoctor_code());
        vrachiDS.setFio(rowDS.getDoctor());
        vrachiDS.setMcod("05011D");
        vrachiDS.setIdmsp(rowDS.getVr_spec());
        vrachiDS.setSpec(rowDS.getVr_spec_name());
        vrachiDS.setDost("false");
        vrachiDS.setType("2");
        vrachiDS.setVers_spec("");
        vrachiDS.setSs("");

        return vrachiDS;
    }

    //Метод создает новый случай в ZAP
    public static SluchDS createSluch(RowDS row) {
        //СОЗДАЕМ СЛУЧАЙ
        SluchDS sluch = new SluchDS();

        //ЗАПОЛНЯЕМ СЛУЧАЙ
        sluch.setId(UUID.randomUUID().toString().toUpperCase());
        sluch.setIdcase("0");
        sluch.setMcod("05011D");
        sluch.setGlpu("050130");
        sluch.setSpolis(row.getSpolis());
        sluch.setNpolis(row.getNpolis());
        sluch.setNovor(row.getNovor());
        sluch.setSmo(row.getSmo());
        sluch.setFam(row.getFam().trim().toUpperCase());
        sluch.setIm(row.getIm().trim().toUpperCase());
        sluch.setOt(row.getOt().trim().toUpperCase());
        sluch.setW(row.getIs_male());
        sluch.setDr(row.getBirthdate());
        sluch.setFam_p(row.getFam_n());
        sluch.setIm_p(row.getIm_n());
        sluch.setOt_p(row.getOt_n());
        sluch.setW_p(row.getIs_male_n());
        sluch.setDr_p(row.getDat_rojd_n());
        sluch.setUsl_ok("2"); //Дневные стационары
        sluch.setVidpom(row.getVidpom());
        sluch.setNpr_mo(row.getNpr_mo());
        sluch.setOrder("3");
        sluch.setT_order("0");
        sluch.setPodr(row.getN_otd());
        sluch.setProfil(row.getProfil());
        sluch.setDet("0");

        if (!sluch.getPodr().equals("051")) {
            sluch.setNhistory(row.getIbnumber());
        } else {
            sluch.setNhistory(row.getIbnumber());
        }

        sluch.setDate_1(row.getDat_in());
        sluch.setDate_2(row.getDat_out());
        sluch.setDs0(row.getMkb_code_p());
        sluch.setDs1(row.getMkb_code());
        sluch.setDs2("");
        sluch.setCode_mes1("");
        sluch.setCode_mes2("");
        sluch.setRslt(row.getResult_gosp());
        sluch.setIshod(row.getIshod());
        sluch.setPrvs(row.getPrvs());
        sluch.setIddokt(row.getDoctor_code());
        sluch.setOs_sluch("0");
        sluch.setIdsp("33");
        sluch.setEd_col("0.00");
        sluch.setKolusl(row.getKol_usl());
        sluch.setTarif("0.00");
        sluch.setSumv(row.getSumv());
        sluch.setOplata("0");
        sluch.setSump("0.00");
        sluch.setRefreason("0");
        sluch.setSank_mek("0.00");
        sluch.setSank_mee("0.00");
        sluch.setSank_ekmp("0.00");
        sluch.setComentz("");
        sluch.setUid(UUID.randomUUID().toString().toUpperCase());

        if (row.getInogor().trim().equals("0")) {
            sluch.setInogor("false");
        } else if (row.getInogor().trim().equals("1")) {
            sluch.setInogor("true");
        }

        sluch.setPr_nov("0");

        /*//Получаем месяц и год
        Date date = null;
        try {
            date = formatterMain.parse(row.getDat_out());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendarMain.setTime(date);
        //Получаем месяц и год
        sluch.setOtmonth(calendarMain.get(Calendar.MONTH) + 1 + "");
        sluch.setOtyear(calendarMain.get(Calendar.YEAR) + "");*/

        LocalDate dateout = LocalDate.parse(row.getDat_out(), formatter);

        sluch.setOtmonth(String.valueOf(dateout.getMonthValue()));
        sluch.setOtyear(String.valueOf(dateout.getYear()));

        sluch.setDisp("");
        sluch.setVid_hmp("");
        sluch.setMetod_hmp("");
        sluch.setDs3("");
        sluch.setVnov_m("0");
        sluch.setRslt_d("0");
        sluch.setVers_spec("V021");
        sluch.setDopsch("0");
        sluch.setTal_d("");
        sluch.setTal_p("");
        sluch.setVbr("0");
        sluch.setP_otk("0");
        sluch.setNrisoms("");
        sluch.setDs1_pr("0");
        sluch.setDs4("");
        sluch.setNazn("0");
        sluch.setNaz_sp("0");
        sluch.setNaz_v("0");
        sluch.setNaz_pmp("0");
        sluch.setNaz_pk("0");
        sluch.setPr_d_n("0");

        //Получаем дату направления JAVA8
        String naprDateXML = row.getDate_napr();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        sluch.setNpr_date(LocalDate.parse(naprDateXML, formatter1).format(formatter2));
        //Получаем дату направления JAVA8

        sluch.setTal_num("");
        sluch.setDs2_pr("0");
        sluch.setPr_ds2_n("0");
        sluch.setC_zab("3");
        sluch.setMse("0");
        sluch.setVb_p("0");
        sluch.setNaz_usl("");
        sluch.setNapr_date("");
        sluch.setNapr_mo("");
        sluch.setDs_onk("0");
        sluch.setNpr_lpu(row.getNpr_lpu());
        sluch.setNpr_usl_ok("3");
        sluch.setWei("0.0");

        //Устанавливаем пациента для случая
        PacientDS pacientDS = createPatient(row);
        sluch.setPacientDS(pacientDS);


        return sluch;
    }

    //Метод создает нового пациента
    public static PacientDS createPatient(RowDS row) {
        //СОЗДАЕМ ПАЦИЕНТА
        PacientDS pacient = new PacientDS();

        //ЗАПОЛНЯЕМ ПАЦИЕНТА
        pacient.setVpolis(row.getVpolis());
        pacient.setSpolis(row.getSpolis());
        pacient.setNpolis(row.getNpolis());
        pacient.setSnpol(row.getSnpol()); //Не нужен в ASUM, нужен для проверки серии и полиса пациента
        pacient.setSmo(row.getSmo());
        pacient.setSmo_ogrn(row.getSmo_ogrn());
        pacient.setSmo_ok(row.getSmo_ok());
        pacient.setSmo_nam(row.getSmo_nam());
        pacient.setNovor(row.getNovor());

        if (row.getInogor().trim().equals("0")) {
            pacient.setInogor("false");
        } else if (row.getInogor().trim().equals("1")) {
            pacient.setInogor("true");
        }

        pacient.setFam(row.getFam());
        pacient.setIm(row.getIm());
        pacient.setOt(row.getOt());
        pacient.setW(row.getIs_male());
        pacient.setDr(row.getBirthdate());
        pacient.setFam_p(row.getFam_n());
        pacient.setIm_p(row.getIm_n());
        pacient.setOt_p(row.getOt_n());
        pacient.setW_p("");
        pacient.setDr_p(row.getDat_rojd_n());
        pacient.setMr(row.getMr());
        pacient.setDoctype("");
        pacient.setDocser(row.getDocser());
        pacient.setDocnum(row.getDocnum());
        pacient.setSnils(row.getSnils());
        pacient.setOkatog("");
        pacient.setOkatop("");
        pacient.setComentz("");
        pacient.setAdres(row.getAddress());
        pacient.setRecid(UUID.randomUUID().toString().toUpperCase());
        pacient.setInv("0");
        pacient.setMse("0");
        pacient.setDocdate("");
        pacient.setDocorg("");

        return pacient;
    }
}
