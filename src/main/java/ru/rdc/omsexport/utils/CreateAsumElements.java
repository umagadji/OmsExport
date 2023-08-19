package ru.rdc.omsexport.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rdc.omsexport.asum_models.*;
import ru.rdc.omsexport.cards_model.Cards;
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.local_db_models.MkbExtended;
import ru.rdc.omsexport.local_db_models.Slpu;
import ru.rdc.omsexport.services.MkbExtendedService;
import ru.rdc.omsexport.services.SlpuService;
import ru.rdc.omsexport.services.SmoService;
import ru.rdc.omsexport.services.SpTarifExtendedService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

//Класс для создания элементов ASUM файла
@Service
public class CreateAsumElements {

    private static SmoService smoService;
    private static SpTarifExtendedService spTarifExtendedService;
    private static SlpuService slpuService;
    private static MkbExtendedService mkbExtendedService;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public CreateAsumElements(
            SmoService smoService,
            SlpuService slpuService,
            SpTarifExtendedService spTarifExtendedService,
            MkbExtendedService mkbExtendedService) {
        CreateAsumElements.smoService = smoService;
        CreateAsumElements.slpuService = slpuService;
        CreateAsumElements.spTarifExtendedService = spTarifExtendedService;
        CreateAsumElements.mkbExtendedService = mkbExtendedService;
    }

    //Метод создает услугу
    public static Usl createUsl(Cards cards) {
        //С 07.2023 для создания услуги usl больше не требуется sluch т.к. он нужен был только для для setIdstrax, а он теперь подставляется когда идет запись в xml
        Usl usl = new Usl();

        //Считаем общую сумму услуги
        double sumv_usl = Objects.requireNonNull(spTarifExtendedService.findByKsg(cards.getCode_usl()).orElse(null)).getPrice() * cards.getKol_usl();

        //usl.setIdstrax(sluch.getId()); //setIdstrax - устанавливаем idStrax для услуги, чтобы потом из услуги перенести в случай
        usl.setIdusl(UUID.randomUUID().toString().toUpperCase());
        usl.setIdserv("0");
        usl.setSpolis(cards.getSpolis());
        usl.setNpolis(cards.getNpolis());
        usl.setGlpu(AppConstants.GLPU_RDC);

        if (cards.getOtd() == 1 || cards.getOtd() == 2 || cards.getOtd() == 3 || cards.getOtd() == 4 || cards.getOtd() == 5) {
            usl.setMcod(AppConstants.TFOMS_CODE_RDC);
            usl.setP_cel(AppConstants.USL_PCEL_30);
            usl.setKoefk(AppConstants.USL_KOEFK_30);
        } else if (cards.getOtd() == 6) {
            usl.setMcod(AppConstants.TFOMS_CODE_REC);
            usl.setP_cel(AppConstants.USL_PCEL_31);
            usl.setKoefk(AppConstants.USL_KOEFK_31);
        } else if (cards.getOtd() == 7) {
            usl.setMcod(AppConstants.TFOMS_CODE_KP);
            usl.setP_cel(AppConstants.USL_PCEL_32);
            usl.setKoefk(AppConstants.USL_KOEFK_32);
        } else {
            System.out.println("Неизвестное отделение");
        }

        usl.setPodr("");
        usl.setProfil(Objects.requireNonNull(spTarifExtendedService.findByKsg(cards.getCode_usl()).orElse(null)).getIdpr() + ""); //Берем профиль из услуги из sp_tarif
        usl.setDet("0"); //Выяснить у Артура
        usl.setDate_in(formatter.format(cards.getDate_in()));
        usl.setDate_out(formatter.format(cards.getDate_out()));
        usl.setDs(cards.getMkb_code());
        usl.setCode_usl(cards.getCode_usl());
        usl.setEd_col("0");
        usl.setKol_usl(cards.getKol_usl() + "");
        usl.setTarif(Objects.requireNonNull(spTarifExtendedService.findByKsg(cards.getCode_usl()).orElse(null)).getPrice() + "");
        usl.setSumv_usl(sumv_usl + "");
        usl.setZak("1");
        usl.setStand("false");
        usl.setPrvs(cards.getPrvs() + "");
        usl.setCode_md(cards.getCode_md());
        usl.setComentu("");
        usl.setUid("");
        usl.setDopsch("0");
        usl.setDir2("0");
        usl.setGr_zdorov("0");
        usl.setStudent("0");
        usl.setVid_vme("");
        usl.setPouh("0");
        usl.setOtkaz2("0");
        usl.setNazna4("");
        usl.setP_per("0");
        usl.setNpl("0");
        usl.setIdsh("");
        usl.setIdsh2("");
        usl.setDn("0");
        usl.setDs_onk("0");
        usl.setProfil_k("0");
        usl.setIdsl(UUID.randomUUID().toString().toUpperCase());

        if (cards.isMuvr()) {
            usl.setMuvr("1");
            usl.setMuvr_lpu(Objects.requireNonNull(slpuService.findSlpuByMcod(cards.getLpu_shnm().trim()).orElse(null)).getGlpu());
        } else {
            usl.setMuvr("0");
            usl.setMuvr_lpu("");
        }

        usl.setDate_usl("");

        //Создаем сегмент mr_usl_n и устанавливаем ее услуге Usl
        usl.setMrUslN(createMrUslN(usl));

        return usl;
    }

    //Метод создает сегмент mr_usl_n для услуги
    public static MrUslN createMrUslN(Usl usl) {
        MrUslN mrUslN = new MrUslN();

        mrUslN.setIdusl(usl.getIdusl());
        mrUslN.setMr_n("1");
        mrUslN.setPrvs_mr_n(usl.getPrvs());
        mrUslN.setCode_md_m(usl.getCode_md());
        mrUslN.setFio_md("");

        return mrUslN;
    }

    //Метод создает новый случай в ZAP
    public static Sluch createSluch(List<Usl> uslList) {
        //С 07.2023 для создания случая нужен не CardsList, а UslList. Т.к. теперь идет разделение по иногородним (если у пациента в месяц был иногородний и МАКС-М шифр)
        Sluch sluch = new Sluch();

        List<Cards> cardsList = new ArrayList<>();

        //С 07.2023 из uslList получаем каждый cards и создаем cardsList, дальше как обычно используем для создания случая
        for (int i = 0; i < uslList.size(); i++) {
            cardsList.add(uslList.get(i).getCards());
        }

        //Ищем минимальную дату date_in из всех услуг в cardsList
        LocalDate minDate = cardsList.stream()
                .map(Cards::getDate_in)          // Stream<LocalDate>
                .min(Comparator.naturalOrder())  // Optional<LocalDate>
                .get();

        LocalDate maxDate = cardsList.stream()
                .map(Cards::getDate_out)         // Stream<LocalDate>
                .max(Comparator.naturalOrder())  // Optional<LocalDate>
                .get();

        sluch.setId(UUID.randomUUID().toString().toUpperCase());
        sluch.setIdcase("0");

        if (cardsList.get(0).getOtd() == 1 || cardsList.get(0).getOtd() == 2 ||
                cardsList.get(0).getOtd() == 3 || cardsList.get(0).getOtd() == 4 || cardsList.get(0).getOtd() == 5) {
            sluch.setMcod(AppConstants.TFOMS_CODE_RDC);
            sluch.setUsl_ok(AppConstants.SLUCH_USL_OK_30);
            sluch.setRslt(AppConstants.SLUCH_RSLT_30);
            sluch.setIshod(AppConstants.SLUCH_ISHOD_30);
        } else if (cardsList.get(0).getOtd() == 6) {
            sluch.setMcod(AppConstants.TFOMS_CODE_REC);
            sluch.setUsl_ok(AppConstants.SLUCH_USL_OK_31);
            sluch.setRslt(AppConstants.SLUCH_RSLT_31);
            sluch.setIshod(AppConstants.SLUCH_ISHOD_31);
        } else if (cardsList.get(0).getOtd() == 7) {
            sluch.setMcod(AppConstants.TFOMS_CODE_KP);
            sluch.setUsl_ok(AppConstants.SLUCH_USL_OK_32);
            sluch.setRslt(AppConstants.SLUCH_RSLT_32);
            sluch.setIshod(AppConstants.SLUCH_ISHOD_32);
        } else {
            System.out.println("Неизвестное отделение");
        }

        //Берем профиль из услуги из sp_tarif
        sluch.setProfil(Objects.requireNonNull(spTarifExtendedService.findByKsg(cardsList.get(0).getCode_usl()).orElse(null)).getIdpr() + "");

        //Устанавливаем вид помощи
        if(cardsList.get(0).getMcod().trim().equals(AppConstants.TFOMS_CODE_KP) && sluch.getProfil().trim().equals("97")) {
            sluch.setVidpom(AppConstants.VID_POM_12);
        } else if (cardsList.get(0).getMcod().trim().equals(AppConstants.TFOMS_CODE_RDC) && sluch.getProfil().trim().equals("67")) {
            //У Артура почему-то патанатомия с профилем 34, пока так и оставим
            sluch.setVidpom(AppConstants.VID_POM_12);
        } else {
            sluch.setVidpom(AppConstants.VID_POM_13);
        }

        sluch.setGlpu(AppConstants.GLPU_RDC);
        sluch.setSpolis(cardsList.get(0).getSpolis());
        sluch.setNpolis(cardsList.get(0).getNpolis());

        if (cardsList.get(0).isNovor()) {
            sluch.setNovor("1");
            sluch.setFam_p(cardsList.get(0).getFam());
            sluch.setIm_p(cardsList.get(0).getIm());
            sluch.setOt_p(cardsList.get(0).getOt());
            sluch.setDr_p(formatter.format(cardsList.get(0).getDat_rojd()));
            sluch.setFam(cardsList.get(0).getFam_n());
            sluch.setIm(cardsList.get(0).getIm_n());
            sluch.setOt(cardsList.get(0).getOt_n());
            sluch.setDr(formatter.format(cardsList.get(0).getDat_rojd_n()));

            if (cardsList.get(0).is_male()) {
                sluch.setW("1");
            } else {
                sluch.setW("2");
            }

            if (cardsList.get(0).is_male_n()) {
                sluch.setW_p("1");
            } else {
                sluch.setW_p("2");
            }
            //Если не новорожденный и фамилия представителя равна 0
        } else if (!cardsList.get(0).isNovor() && cardsList.get(0).getFam_n().trim().length() == 0) {
            sluch.setNovor("0");
            sluch.setFam(cardsList.get(0).getFam());
            sluch.setIm(cardsList.get(0).getIm());
            sluch.setOt(cardsList.get(0).getOt());
            sluch.setDr(formatter.format(cardsList.get(0).getDat_rojd()));
            sluch.setFam_p(cardsList.get(0).getFam_n());
            sluch.setIm_p(cardsList.get(0).getIm_n());
            sluch.setOt_p(cardsList.get(0).getOt_n());
            sluch.setDr_p("");

            if (cardsList.get(0).is_male()) {
                sluch.setW("1");
            } else {
                sluch.setW("2");
            }

            if (cardsList.get(0).is_male_n()) {
                sluch.setW_p("");
            } else {
                sluch.setW_p("");
            }
        }

        sluch.setSmo(cardsList.get(0).getSmocod());
        sluch.setNpr_mo(Objects.requireNonNull(slpuService.findSlpuByMcod(cardsList.get(0).getLpu_shnm()).orElse(null)).getGlpu()); //Glpu из таблицы
        sluch.setNpr_lpu(Objects.requireNonNull(slpuService.findSlpuByMcod(cardsList.get(0).getLpu_shnm()).orElse(null)).getMcod()); //Короткое название из АРМ Контент
        sluch.setOrder(AppConstants.SLUSH_FOR_POM);
        sluch.setT_order("0");
        sluch.setPodr("");
        sluch.setDet("0");
        sluch.setNhistory(cardsList.get(0).getN_tal() + "");
        sluch.setDate_1(formatter.format(minDate));
        sluch.setDate_2(formatter.format(maxDate));

        //То есть с первой медуслуги для случая берётся основной диагноз, со второй - первичный, с третьей - вспомогательный.
        //Если коллекция содержит > 1 записей, т.е. услуг
        if (cardsList.size() > 1) {
            //То основным ставим диагноз первой услуги
            sluch.setDs1(cardsList.get(0).getMkb_code());
            //Первичным диагноз второй услуги
            sluch.setDs0(cardsList.get(1).getMkb_code());
        } else if (cardsList.size() == 1) {
            sluch.setDs0(cardsList.get(0).getMkb_code());
            sluch.setDs1(cardsList.get(0).getMkb_code());
        }

        sluch.setDs2(null); //Сопутствующий пока не ставим
        sluch.setCode_mes1("");
        sluch.setCode_mes2("");
        sluch.setPrvs(cardsList.get(0).getPrvs() + "");

        sluch.setIddokt(cardsList.get(0).getCode_md() + "");

        sluch.setOs_sluch("0");

        //СТАРЫЙ ВАРИАНТ ПРОСТАВЛЕНИЯ СПОСОБА ОПЛАТЫ ДО 06.2023 ГОДА
        /*if (cardsList.get(0).getN_otd() == 1 || cardsList.get(0).getN_otd() == 2 || cardsList.get(0).getN_otd() == 3 || cardsList.get(0).getN_otd() == 4 || cardsList.get(0).getN_otd() == 5) {
            sluch.setIdsp("28");
        } else if (cardsList.get(0).getN_otd() == 6 || cardsList.get(0).getN_otd() == 7) {
            //Если консультативные посещения, разовые обращения или посещения на приеме
            if (Objects.requireNonNull(spTarifExtendedService.findByKsg(cardsList.get(0).getCode_usl()).orElse(null)).getT_type() == 29
                    || Objects.requireNonNull(spTarifExtendedService.findByKsg(cardsList.get(0).getCode_usl()).orElse(null)).getT_type() == 19
                    || Objects.requireNonNull(spTarifExtendedService.findByKsg(cardsList.get(0).getCode_usl()).orElse(null)).getT_type() == 1) {
                sluch.setIdsp("29");
                //Если обращения по поводу заболевания или консультативные обращения
            } else if (Objects.requireNonNull(spTarifExtendedService.findByKsg(cardsList.get(0).getCode_usl()).orElse(null)).getT_type() == 39
                            || Objects.requireNonNull(spTarifExtendedService.findByKsg(cardsList.get(0).getCode_usl()).orElse(null)).getT_type() == 9) {
                sluch.setIdsp("30");
            }
        } else {
            System.out.println("Неизвестное отделение");
        }*/

        if (cardsList.get(0).getOtd() == 1 || cardsList.get(0).getOtd() == 2 || cardsList.get(0).getOtd() == 3 || cardsList.get(0).getOtd() == 4 || cardsList.get(0).getOtd() == 5) {
            //sluch.setIdsp("28");
            if (cardsList.get(0).isMuvr()) {
                sluch.setIdsp("28");
            } else {
                sluch.setIdsp("30");
            }
        } else if (cardsList.get(0).getOtd() == 6 || cardsList.get(0).getOtd() == 7) {
            //Если консультативные посещения, разовые обращения или посещения на приеме
            if (Objects.requireNonNull(spTarifExtendedService.findByKsg(cardsList.get(0).getCode_usl()).orElse(null)).getT_type() == 29
                    || Objects.requireNonNull(spTarifExtendedService.findByKsg(cardsList.get(0).getCode_usl()).orElse(null)).getT_type() == 19
                    || Objects.requireNonNull(spTarifExtendedService.findByKsg(cardsList.get(0).getCode_usl()).orElse(null)).getT_type() == 1) {
                if (cardsList.get(0).isInogor()) {
                    sluch.setIdsp("30");
                } else {
                    sluch.setIdsp("29");
                }
                //Если обращения по поводу заболевания или консультативные обращения
            } else if (Objects.requireNonNull(spTarifExtendedService.findByKsg(cardsList.get(0).getCode_usl()).orElse(null)).getT_type() == 39
                    || Objects.requireNonNull(spTarifExtendedService.findByKsg(cardsList.get(0).getCode_usl()).orElse(null)).getT_type() == 9) {
                sluch.setIdsp("30");
            }
        } else {
            System.out.println("Неизвестное отделение");
        }

        sluch.setEd_col("0");

        double kol_usl = 0;
        double sumv = 0;

        //Считаем общее количество kol_usl для всех услуг и общую стоимость
        for (Cards cards : cardsList) {
            sumv = sumv + (cards.getKol_usl() * cards.getTarif());
        }

        kol_usl = kol_usl + cardsList.size();

        sluch.setKolusl(kol_usl + "");
        sluch.setTarif("0");
        sluch.setSumv(sumv + "");
        sluch.setOplata("0");
        sluch.setSump("0");
        sluch.setRefreason("0");
        sluch.setSank_mek("0");
        sluch.setSank_mee("0");
        sluch.setSank_ekmp("0");
        sluch.setComentz("");
        sluch.setUid("");

        //Берем поле isInogor у первого элемента из коллекции
        sluch.setInogor(cardsList.get(0).isInogor() + "");

        sluch.setPr_nov("0");
        sluch.setOtmonth(cardsList.get(0).getDate_out().getMonthValue() + "");
        sluch.setOtyear(cardsList.get(0).getDate_out().getYear() + "");
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
        sluch.setVbr("");
        sluch.setP_otk("");
        sluch.setNrisoms("");
        sluch.setDs1_pr("");
        sluch.setDs4("");
        sluch.setNazn("0");
        sluch.setNaz_sp("0");
        sluch.setNaz_v("0");
        sluch.setNaz_pmp("0");
        sluch.setNaz_pk("0");
        sluch.setNpr_date(formatter.format(minDate));
        sluch.setTal_num("");
        sluch.setDs2_pr("0");
        sluch.setPr_ds2_n("0");
        sluch.setMse("0");
        sluch.setVb_p("0");
        sluch.setNaz_usl("");
        sluch.setNapr_date("");
        sluch.setNapr_mo("");
        sluch.setDs_onk("0");

        //Характер заболевания
        //Если характер заболевания не равен нулю
        if (cardsList.get(0).getChar_zab() != 0) {
            if (cardsList.get(0).getChar_zab() == AppConstants.ARIADNA_CHAR_ZAB_OSTROE) {
                sluch.setC_zab(AppConstants.ASUM_CHAR_ZAB_OSTROE + "");
            } else if (cardsList.get(0).getChar_zab() == AppConstants.ARIADNA_CHAR_ZAB_VPERVYE) {
                sluch.setC_zab(AppConstants.ASUM_CHAR_ZAB_VPERVYE + "");
            } else if (cardsList.get(0).getChar_zab() == AppConstants.ARIADNA_CHAR_ZAB_RANEE) {
                sluch.setC_zab(AppConstants.ASUM_CHAR_ZAB_RANEE + "");
            } else {
                //Если не острый, не впервые установленный и не ранее установленный, то ставим ранее установленный
                sluch.setC_zab(AppConstants.ASUM_CHAR_ZAB_RANEE + "");
            }
        } else {
            sluch.setC_zab(AppConstants.ASUM_CHAR_ZAB_RANEE + "");
        }

        //в Npr_usl_ok ставим idump из mcod таблицы s_lpu
        sluch.setNpr_usl_ok(Objects.requireNonNull(slpuService.findSlpuByMcod(cardsList.get(0).getLpu_shnm()).orElse(null)).getIdump() + "");
        sluch.setWei("0");

        //Вызываем метод создания пациента, передавая на вход первый элемент коллекции cardsDiagnList
        Pacient pacient = createPatient(cardsList.get(0));
        //Устанавливаем пациента для случая
        sluch.setPacient(pacient);

        //Получаем optional для MkbExtended на основании основного диагноза из случая. Ds1 - основной диагноз
        Optional<MkbExtended> mkbExtendedOptional = mkbExtendedService.findByLcod(sluch.getDs1());

        //Если optional вернул МКБ код и если он онкологический, то создаем нужные сегменты
        if (mkbExtendedOptional.isPresent() && mkbExtendedOptional.get().isIs_onk()) {
            sluch.setCons(createCons(sluch));
            sluch.setNapr(createNapr(sluch));
            sluch.setOnkSl(createOnkSl(sluch));
        }

        return sluch;
    }

    //Создаем сегмент cons
    public static Cons createCons(Sluch sluch) {
        Cons cons = new Cons();

        cons.setIdusl(sluch.getId());
        cons.setId(UUID.randomUUID().toString().toUpperCase());
        cons.setPr_cons("0");
        cons.setDt_cons("");

        return cons;
    }

    //Создаем сегмент napr
    public static Napr createNapr(Sluch sluch) {
        Napr napr = new Napr();

        napr.setIdusl(sluch.getId());
        napr.setId(UUID.randomUUID().toString().toUpperCase());
        napr.setNapr_date(sluch.getDate_1());
        napr.setNapr_v("4");
        napr.setMet_issl("0");
        napr.setNapr_usl("");

        Optional<Slpu> slpuOptional = slpuService.findSlpuByMcod(sluch.getNpr_lpu());

        //Если optional возвращает объект slpu
        slpuOptional.ifPresent(slpu -> napr.setNapr_mo(slpu.getGlpu()));

        return napr;
    }

    //Создаем сегмент onk_sl
    public static OnkSl createOnkSl(Sluch sluch) {
        OnkSl onkSl = new OnkSl();

        onkSl.setIdusl(sluch.getId());
        onkSl.setId(UUID.randomUUID().toString().toUpperCase());
        onkSl.setDs1_t("5");
        onkSl.setStad("0");
        onkSl.setOnk_t("0");
        onkSl.setOnk_n("0");
        onkSl.setOnk_m("0");
        onkSl.setMtstz("0");
        onkSl.setSod("0.0");
        onkSl.setK_fr("0");
        onkSl.setWei("0");
        onkSl.setHei("0");
        onkSl.setBsa("0.00");

        return onkSl;
    }

    //Метод создает нового пациента
    public static Pacient createPatient(Cards cards) {
        Pacient pacient = new Pacient();

        pacient.setVpolis(cards.getVpolis() + "");
        pacient.setSpolis(cards.getSpolis());
        pacient.setNpolis(cards.getNpolis());
        pacient.setSmo(cards.getSmocod());
        pacient.setSmo_ogrn(smoService.findBySmocod(cards.getSmocod()).get().getOgrn());
        pacient.setSmo_ok(smoService.findBySmocod(cards.getSmocod()).get().getTf_okato());
        pacient.setSmo_nam(cards.getSmo_nm());

        pacient.setNovor("0");

        if (cards.isInogor()) {
            pacient.setInogor("true");
        } else {
            pacient.setInogor("false");
        }

        pacient.setFam(cards.getFam());
        pacient.setIm(cards.getIm());
        pacient.setOt(cards.getOt());

        if (cards.is_male()) {
            pacient.setW("1");
        } else {
            pacient.setW("2");
        }

        pacient.setDr(formatter.format(cards.getDat_rojd()));
        pacient.setFam_p("");
        pacient.setIm_p("");
        pacient.setOt_p("");

        if (cards.is_male_n()) {
            pacient.setW_p("1");
        } else {
            pacient.setW_p("2");
        }

        pacient.setMr(cards.getMr());
        pacient.setDoctype(cards.getDoctype() + "");
        pacient.setDocser(cards.getDocser());
        pacient.setDocnum(cards.getDocnum());
        pacient.setSnils(cards.getSnils());
        pacient.setOkatog("");
        pacient.setOkatop("");
        pacient.setComentz("");
        pacient.setAdres(cards.getAdres());
        pacient.setRecid(UUID.randomUUID().toString().toUpperCase());
        pacient.setInv("0");
        pacient.setMse("0");

        return pacient;
    }
}
