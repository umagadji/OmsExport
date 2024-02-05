package ru.rdc.omsexport.utils;

import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rdc.omsexport.asum_models.*;
import ru.rdc.omsexport.cards_model.Cards;
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.controllers.MainController;
import ru.rdc.omsexport.local_db_models.*;
import ru.rdc.omsexport.services.*;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

//Класс предназначен для того чтобы создать ASUM файл для РЭЦ
@Service
public class CreateRecAsumFile {
    Map<String, Vrachi> vrachiMap = new HashMap<>(); //Будет хранить всех уникальных врачей

    private static String yearMonth = ""; //Хранит год и месяц для названия файла

    private MainController mainController;

    private final CardsService cardsService;
    private final SmoService smoService;
    private final SpTarifExtendedService spTarifExtendedService;
    private final SlpuService slpuService;
    private final MkbExtendedService mkbExtendedService;
    private final MedspecService medspecService;

    @Autowired
    public CreateRecAsumFile(
            SmoService smoService,
            SlpuService slpuService,
            MedspecService medspecService,
            SpTarifExtendedService spTarifExtendedService,
            MkbExtendedService mkbExtendedService,
            CardsService cardsService) {
        this.smoService = smoService;
        this.slpuService = slpuService;
        this.medspecService = medspecService;
        this.spTarifExtendedService = spTarifExtendedService;
        this.mkbExtendedService = mkbExtendedService;
        this.cardsService = cardsService;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    //Метод для вывода логов в TextArea в main.fxml
    private void setLogs(String message) {
        //Изменение любых данных в интерфейсе JavaFX нужно делать в отдельном потоке
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainController.getLogs().appendText(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(LocalDateTime.now()) + " " + message + "\n");
            }
        });
    }

    //Метод для вывода логов в консоль
    private void setLogsInConsole(String message) {
        System.out.println(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(LocalDateTime.now()) + " " + message);
    }

    public void setPeriod(LocalDate bgnDate, LocalDate endDate) {
        if (bgnDate != null && endDate != null) {
            LocalDate min = bgnDate;
            if (min.isAfter(endDate)) {
                min = endDate;
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMM");
            yearMonth = min.format(dateTimeFormatter);
        }
    }

    private void setCounterREC(int all, int checked) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainController.getCounterRec().setText("РЭЦ: " + all + " / " + checked);
            }
        });
    }

    private void setSluchCounterRec(int counter) {
        Platform.runLater(() -> {
            mainController.getSluchCounterREC().setText("/ Случаев: " + counter);
        });
    }

    //Значения параметров приходят из html формы с фронта
    //Чтение таблицы cards_rec для РЭЦ
    public List<Cards> readCardsRecDBF() {

        //Коллекция будет содержать услуги по эндокринологии

        setLogs("Начало чтения эндокринологических входящих таблиц выгрузки (DBF).");
        setLogsInConsole("Начало чтения эндокринологических входящих таблиц выгрузки (DBF).");

        setLogs("Расчет для РЭЦ выполняется");
        setLogsInConsole("Расчет для РЭЦ выполняется");
        List<Cards> recList = new ArrayList<>(ReadCardsDBF.readCardsDBF(AppConstants.cardsDBFPath + "cards_rec.dbf", "866"));

        setLogs("Конец чтения эндокринологических входящих таблиц выгрузки (DBF).");
        setLogsInConsole("Конец чтения эндокринологических входящих таблиц выгрузки (DBF).");

        //Возвращаем коллекцию recList
        return recList;
    }

    //Метод будет каждую эндокринологическую услугу из cards отмечать годными для подачи на оплату и устанавливать доп. параметры такие как comment, t_type, is_onkl
    //Передаем String[] checks параметры, чтобы знать для каких отделений делать расчет.
    //Проверяем эндокринологические услуги
    public List<Cards> selectCorrectCardsRec() {
        //Содержит проверенные услуги
        List<Cards> allCardsRecList = readCardsRecDBF();

        allCardsRecList.sort(Comparator.comparing(Cards::getN_tal)
                .thenComparing(Cards::getSnPol)
                .thenComparing(Cards::getCode_md)
                .thenComparing(Cards::getCode_usl)
                .thenComparing(Cards::getDate_in));

        //Для каждой услуги из allCardsRecList выполняем проверки на допустимость подачи на оплату
        setLogs("Начало проверки корректности эндокринологических услуг");
        setLogsInConsole("Начало проверки корректности эндокринологических услуг");
        int counter = 0; //Счетчик проверки услуг
        for (Cards card : allCardsRecList) {

            //Выводим кол-во проверенных услуг
            counter++;
            setCounterREC(allCardsRecList.size(),counter);

            //Услуга является ОППЗ
            boolean isOPPZ = false;
            //Услуга является одним из обращений (пустышка)
            boolean isOPPZOdnoIzPocesh = false;

            //Для упрощения всем РЭЦ. услугам ставим mcod = 050131
            card.setMcod(AppConstants.TFOMS_CODE_REC);

            //ПРОВЕРКИ ИЗ EXCEL. НАЧАЛО
            //Если для пациента указано в отчестве БО, Б-О, Б.О.
            if (card.getOt().equals("БО") || card.getOt().equals("Б-О") || card.getOt().equals("Б.О.")) {
                card.setOt("");
            }

            //Если тип полиса пациента 5 и он единого образца
            if (card.getVpolis() == 5 && card.getNpolis().trim().length() == 16) {
                card.setVpolis(3);
            }

            if (card.getVpolis() == 5 && card.getNpolis().trim().length() != 16) {
                card.setVpolis(1);
            }

            //Если тип полиса пациента 6 и он единого образца
            if (card.getVpolis() == 6 && card.getNpolis().trim().length() == 16) {
                card.setVpolis(3);
            }

            //Если тип полиса пациента 4, он не новорожденный и полис единого образца
            if (card.getVpolis() == 4 && !card.isNovor() && card.getNpolis().trim().length() == 16) {
                card.setVpolis(3);
            }

            //Заменить тип полиса всем записям, где длина полиса = 16
            if (card.getVpolis() == 0 && card.getNpolis().trim().length() == 16) {
                card.setVpolis(3);
            }

            //Заменить тип полиса всем записям, где длина полиса = 9
            if (card.getVpolis() == 0 && card.getSpolis().trim().length() == 0 && card.getNpolis().trim().length() == 9) {
                card.setVpolis(2);
            }

            if (card.getVpolis() == 4 && card.getSpolis().trim().length() == 0 && card.getNpolis().trim().length() == 9) {
                card.setVpolis(2);
            }

            if (card.isCorrect()) {
                if (card.getAdres().trim().length() < 8) {
                    card.setAdres("Дагестан Респ");
                }
            }

            //Для всех полисов с типом 2 и 3, серию делаем пустым
            if (card.getVpolis() == 2 || card.getVpolis() == 3) {
                card.setSpolis("");
            }

            if (card.getLpu_shnm().trim().equals("~~~") || card.getLpu() == 0) {
                card.setCorrect(false);
                card.setComment("Исключается из оплаты: lpu_shnm = ~~~");
            }

            if (card.getNpolis().trim().equals("") || card.getNpolis() == null) {
                card.setCorrect(false);
                card.setComment("Ошибка полиса: Номер полиса пустой");
            }

            if (card.getNpolis().trim().length() != 16 && card.getVpolis() == 3) {
                card.setCorrect(false);
                card.setComment("Ошибка полиса: Неверный ЕНП");
            }

            if (card.getNpolis().trim().length() != 9 && (card.getVpolis() == 1 || card.getVpolis() == 2) && !card.isInogor()) {
                card.setCorrect(false);
                card.setComment("Ошибка полиса: Врем. свид. или старый полис не равен 9 символам");
            }

            if (card.getVpolis() == 4 && card.getNpolis().trim().length() != 16) {
                card.setCorrect(false);
                card.setComment("Ошибка полиса: Полис родителя неверной длины");
            }

            if (card.getVpolis() == 0 && card.getNpolis().trim().length() != 16) {
                card.setCorrect(false);
                card.setComment("Ошибка полиса: Тип полиса пустой и/или неверная длина полиса");
            }

            if (card.getVpolis() == 0) {
                card.setCorrect(false);
                card.setComment("Ошибка полиса: Тип полиса пустой");
            }

            //ПРОВЕРКИ ИЗ EXCEL. КОНЕЦ

            //ПРОВЕРКА СМО. НАЧАЛО
            //Если иногородний
            if (card.isInogor()) {
                //Если код СМО НЕ дагестанский и его длина не равно AppConstants.SMO_CODE_LENGTH
                if (!card.getSmocod().trim().equals(AppConstants.SMO_CODE_MAKS_RD) && card.getSmocod().trim().length() == AppConstants.SMO_CODE_LENGTH) {
                    card.setCorrect(true);
                } else {
                    card.setCorrect(false);
                    card.setComment("Некорректное СМО у иногороднего");
                    setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Некорректное СМО у иногороднего " + card.getSmocod());
                    setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Некорректное СМО у иногороднего " + card.getSmocod());
                }
            } else { //Если не иногородний - Только дагестанский МАКС-М и если не отсечен на предыдущих этапах проверки
                if (card.isCorrect()) {
                    if (card.getSmocod().equals(AppConstants.SMO_CODE_MAKS_RD)) {
                        card.setCorrect(true);
                    } else {
                        card.setCorrect(false);
                        card.setComment("Некорректное СМО у НЕиногороднего " + card.getSmocod());
                        setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Некорректное СМО у НЕиногороднего " + card.getSmocod());
                        setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Некорректное СМО у НЕиногороднего " + card.getSmocod());
                    }
                }
            }

            //Если услуга не исключается, то проверяем на наличие smocod в таблице smo.dbf
            if (card.isCorrect()) {
                Optional<Smo> smoOptional = smoService.findBySmocod(card.getSmocod().trim());
                if (smoOptional.isEmpty()) {
                    card.setCorrect(false);
                    card.setComment("СМО, указанное для пациента, отсутствует в smo.dbf " + card.getSmocod());
                    setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " СМО, указанное для пациента, отсутствует в smo.dbf " + card.getSmocod());
                    setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " СМО, указанное для пациента, отсутствует в smo.dbf " + card.getSmocod());
                }
            }
            //ПРОВЕРКА СМО. КОНЕЦ

            //ПРОВЕРКА МЕДУСЛУГИ. НАЧАЛО
            if (card.isCorrect()) {
                //Если раздел прейскуранта не относится к ОППЗ, проф. приемам и школе диабета
                if (!card.getMetPrKod().trim().equals(AppConstants.ARIADNA_USL_RAZDEL_OPPZ) &&
                        !card.getMetPrKod().trim().equals(AppConstants.ARIADNA_USL_RAZDEL_PROFPR) &&
                        !card.getMetPrKod().trim().equals(AppConstants.ARIADNA_USL_RAZDEL_SCHOOL)) {
                    card.setCorrect(false);
                    card.setComment("Некорректный раздел медуслуги в Ариадне " + card.getMetPrKod() + " для услуги " + card.getCode_usl());
                    setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", услуга " + card.getCode_usl() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Некорректный раздел медуслуги в Ариадне " + card.getMetPrKod());
                    setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", услуга " + card.getCode_usl() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Некорректный раздел медуслуги в Ариадне " + card.getMetPrKod());
                } else {
                    //Если раздел прейскуранта правильный, но длина услуги равна 0
                    if (card.getCode_usl().trim().length() == 0) {
                        card.setCorrect(false);
                        card.setComment("Некорректный код медуслуги ФОМС " + card.getCode_usl());
                        setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Некорректный код медуслуги ФОМС " + card.getCode_usl());
                        setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Некорректный код медуслуги ФОМС " + card.getCode_usl());
                    } else {
                        //Если раздел прейскуранта ОППЗ
                        if (card.getMetPrKod().trim().equals(AppConstants.ARIADNA_USL_RAZDEL_OPPZ)) {
                            //Если код услуги равен стандартной, т.е. 5
                            if (card.getCode_usl().trim().length() == AppConstants.CODE_USL_STANDART_LEN) {
                                //Значит это обращение по поводу заболевания
                                isOPPZ = true;
                                //Устанавливаем количество услуг. Уточнить у Дениса
                                card.setKol_usl(AppConstants.KOLVO_USL_OPPZ);
                            }
                            //Если длина услуга 7
                            if (card.getCode_usl().trim().length() == AppConstants.CODE_USL_STANDART_LEN + 2) {
                                //Значит это одно из посещений (пустышка), количество ставим 0 и прерываем цикл
                                isOPPZOdnoIzPocesh = true;
                                //Исключается из оплаты, если одно из обращений (пустышка)
                                card.setCorrect(false);
                                card.setComment("Не удалось определить ОППЗ-исследование для медуслуги");
                                card.setKol_usl(0);
                                //Здесь можно было бы сделать return. Но для этого нужно реализовать отдельный метод для проверки услуг. Пока оставляем так
                            }
                        }
                    }
                }

                //Если услуга оказалась обращением (не пустышка)
                if (!isOPPZOdnoIzPocesh) {
                    //Создаем Optional для дальнейшей проверки услуги
                    Optional<SpTarifExtended> spTarifNewOptional = spTarifExtendedService.findByKsg(card.getCode_usl().trim());

                    if (spTarifNewOptional.isEmpty()) {
                        card.setCorrect(false);
                        card.setComment("Не удалось определить код медуслуги ФОМС code_usl: " + card.getCode_usl());
                        setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Не удалось определить код медуслуги ФОМС code_usl: " + card.getCode_usl());
                        setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Не удалось определить код медуслуги ФОМС code_usl: " + card.getCode_usl());
                    } else {
                        //Если профиль услуги <= 0
                        if (spTarifNewOptional.get().getIdpr() <= 0) {
                            card.setCorrect(false);
                            card.setComment("Неверный профиль у исследования в sp_tarif: " + card.getCode_usl());
                            setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                    + " Неверный профиль у исследования в sp_tarif: " + card.getCode_usl());
                            setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                    + " Неверный профиль у исследования в sp_tarif: " + card.getCode_usl());
                        } else {
                            //Иначе, если стоимость услуги отличается от той что в sp_tarif
                            if (card.getTarif() != spTarifNewOptional.get().getPrice()) {
                                card.setTarif(spTarifNewOptional.get().getPrice());
                            }
                        }
                    }
                }
            }
            //ПРОВЕРКА МЕДУСЛУГИ. КОНЕЦ

            //ПРОВЕРКА КОДА МКБ УСЛУГИ. НАЧАЛО
            if (card.isCorrect()) { //Если к этому моменту услуга годна для подачи
                //Если длина кода МКБ (основного) равна 0
                if (card.getMkb_code().trim().length() == 0) {
                    card.setCorrect(false);
                    card.setComment("Некорректный код МКБ mkb_code = " + card.getMkb_code());
                    setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Некорректный код МКБ mkb_code = " + card.getMkb_code());
                    setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Некорректный код МКБ mkb_code = " + card.getMkb_code());
                } else { //Иначе, если длина не равна 0
                    //Получаем МКБ из таблицы mkb_extended на основании основного диагноза из услуги
                    Optional<MkbExtended> mkbExtendedOptionalMkbCode = mkbExtendedService.findByLcod(card.getMkb_code().trim());
                    if (mkbExtendedOptionalMkbCode.isEmpty()) {
                        card.setCorrect(false);
                        card.setComment("Код МКБ mkb_code не найден в mkb.dbf = " + card.getMkb_code_p());
                        setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Код МКБ mkb_code не найден в mkb.dbf = " + card.getMkb_code_p());
                        setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Код МКБ mkb_code не найден в mkb.dbf = " + card.getMkb_code_p());
                    }
                }
            }
            //ПРОВЕРКА КОДА МКБ УСЛУГИ. КОНЕЦ

            //ПРОВЕРКА КАБИНЕТА. НАЧАЛО
            if (card.isCorrect()) {
                if (card.getCab_name().trim().length() == 0) {
                    card.setCorrect(false);
                    card.setComment("Некорректный кабинет cab_name = " + card.getCab_name());
                    setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Некорректный кабинет cab_name = " + card.getCab_name());
                    setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Некорректный кабинет cab_name = " + card.getCab_name());
                }
            }
            //ПРОВЕРКА КАБИНЕТА. КОНЕЦ

            //ПРОВЕРКА НАПРАВИВШЕГО ЛПУ. НАЧАЛО
            if (card.isCorrect()) {
                //Если поля lpu, lpu_name, lpu_shnm не заполнены
                if (card.getLpu() <= 0 || card.getLpu_name() == null || card.getLpu_shnm() == null ||
                        card.getLpu_name().trim().length() == 0 || card.getLpu_shnm().length() == 0) {
                    card.setCorrect(false);
                    card.setComment("Некорректное направившее ЛПУ");
                    setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Некорректное направившее ЛПУ " + card.getLpu_name());
                    setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Некорректное направившее ЛПУ " + card.getLpu_name());
                }

                //Проверяем по направившим ЛПУ КП РДЦ и РЭЦ РДЦ
                if (card.getLpu() == 108 || card.getLpu() == 219) {
                    card.setCorrect(false);
                    card.setComment("Направленные от ЛПУ 108, 219 не подаются на оплату");
                    setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Направленные от ЛПУ 108, 219 не подаются на оплату " + card.getLpu_name());
                    setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Направленные от ЛПУ 108, 219 не подаются на оплату " + card.getLpu_name());
                }

                //Получаем optional из таблицы s_lpu по mcod. lpu_shnm - это mcod (подразделение, которое направило)
                Optional<Slpu> slpuOptional = slpuService.findSlpuByMcod(card.getLpu_shnm().trim());
                //Если оно пустое, т.е. если не нашлось в таблице s_lpu записи, значит оно некорректное
                if (slpuOptional.isEmpty()) { //равносильно null
                    card.setCorrect(false);
                    card.setComment("Некорректное значение lpu_shnm == null для направившего ЛПУ lpu_name = " + card.getLpu_name());
                    setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Некорректное значение lpu_shnm == null для направившего ЛПУ lpu_name = " + card.getLpu_name());
                    setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Некорректное значение lpu_shnm == null для направившего ЛПУ lpu_name = " + card.getLpu_name());
                } else {
                    //Если по lpu_shnm в таблице s_lpu не нашлось glpu
                    if (slpuOptional.get().getGlpu() == null || slpuOptional.get().getGlpu().trim().length() != AppConstants.CODE_LPU_LENGTH) {
                        card.setCorrect(false);
                        card.setComment("Некорректное значение glpu в s_lpu для направившего ЛПУ lpu_name = " + card.getLpu_name());
                        setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Некорректное значение glpu в s_lpu для направившего ЛПУ lpu_name = " + card.getLpu_name());
                        setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Некорректное значение glpu в s_lpu для направившего ЛПУ lpu_name = " + card.getLpu_name());
                    }
                }

            }
            //ПРОВЕРКА НАПРАВИВШЕГО ЛПУ. КОНЕЦ

            //ПРОВЕРКА ВРАЧА. НАЧАЛО
            if (card.isCorrect()) {
                if (Integer.parseInt(card.getCode_md()) <= 0 || card.getVr_fio().trim().length() == 0 || card.getPrvs() <= 0 || card.getVr_spnm().trim().length() == 0) {
                    card.setCorrect(false);
                    card.setComment("Неопределён врач code_md = " + card.getCode_md());
                    setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Не определён врач code_md = " + card.getCode_md());
                    setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Не определён врач code_md = " + card.getCode_md());
                } else {
                    //Получаем из таблицы medspec - специальность по полю prvs из входной таблицы cards
                    Optional<Medspec> medspecOptional = medspecService.findByIdmsp(Integer.toString(card.getPrvs()).trim());
                    //Если специальности нет в medspec.dbf или название специальности равна 0
                    if (medspecOptional.isEmpty() || medspecOptional.get().getMspname().trim().length() == 0) {
                        card.setCorrect(false);
                        card.setComment("Некорректная специальность врача в cards.prvs = " + card.getPrvs());
                        setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Некорректная специальность врача в cards.prvs = " + card.getPrvs());
                        setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Некорректная специальность врача в cards.prvs = " + card.getPrvs());
                    }
                }
            }
            //ПРОВЕРКА ВРАЧА. КОНЕЦ

            //ПРОВЕРКА СВОЙСТВ НОВОРОЖДЕННОГО. НАЧАЛО
            //Если не исключается из оплаты и это услуга новорожденного
            if (card.isCorrect() && card.isNovor()) {
                card.setCorrect(false);
                card.setComment("Не должно быть новорожденных пациентов в РЭЦ " + card.getPrvs());
                setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                        + " Не должно быть новорожденных пациентов в РЭЦ " + card.getPrvs());
                setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                        + " Не должно быть новорожденных пациентов в РЭЦ " + card.getPrvs());
            }
            //ПРОВЕРКА СВОЙСТВ НОВОРОЖДЕННОГО. КОНЕЦ

            if (card.isCorrect()) {
                //Если это услуга ОППЗ
                //Проверка наличия ОППЗ с таким же кодом медуслуги на такого же пациента у этого же врача. Начало
                if (isOPPZ) {
                    //Общий список всех услуг РЭЦ фильтруем по полям текущей услуги. В recList хранятся одинаковые услуги для одного пациента для одного врача
                    //Также фильтруем по n_tal
                    List<Cards> recList = allCardsRecList.stream()
                            .filter(cards -> cards.getN_tal() < card.getN_tal())
                            .filter(cards -> cards.getSnPol().equals(card.getSnPol()))
                            .filter(cards -> cards.getCode_md().equals(card.getCode_md()))
                            .filter(cards -> cards.getCode_usl().equals(card.getCode_usl()))
                            .filter(Cards::isCorrect).toList();
                    //Если коллекция не пустая, т.е. есть повторяющиеся услуги
                    //В коллекции будут элементы если есть повторы кроме самой проверяемой услуги cards
                    if (recList.size() > 0) {
                        card.setCorrect(false);
                        card.setComment("У  пациента ОППЗ с таким кодом медуслуги у этого же врача уже есть");
                        setLogs("ИНФОРМАЦИЯ (РЭЦ): " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " У пациента "+ card.getSnPol() +" ОППЗ с таким кодом медуслуги у этого же врача уже есть");
                        setLogsInConsole("ИНФОРМАЦИЯ (РЭЦ): " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " У пациента "+ card.getSnPol() +" ОППЗ с таким кодом медуслуги у этого же врача уже есть");
                    } else {
                        card.setCorrect(true);
                    }
                    //Проверка наличия ОППЗ с таким же кодом медуслуги на такого же пациента у этого же врача. Начало
                } else {
                    //Проверка наличия проф. приема с таким же кодом медуслуги на такого пациента в этот день. Начало

                    //Переменная счетчик, хранит кол-во найденных услуг проф. приемов
                    int kRecIsslProfPr = 0;
                    //Общий список всех услуг РЭЦ фильтруем по полям текущей услуги. В recList хранятся одинаковые услуги для одного пациента для одного врача
                    //Также фильтруем по n_tal
                    List<Cards> recList = allCardsRecList.stream()
                            .filter(cards -> cards.getN_tal() < card.getN_tal())
                            .filter(cards -> cards.getSnPol().equals(card.getSnPol()))
                            .filter(Cards::isCorrect).toList();

                    for (int i = 0; i < recList.size(); i++) {
                        //Если даты совпадают и код услуги совпадает
                        if (card.getDate_in().isEqual(recList.get(i).getDate_in()) && card.getCode_usl().trim().equals(recList.get(i).getCode_usl().trim())) {
                            kRecIsslProfPr++;
                            break;
                        }
                    }
                    //Если kRecIsslProfPr, т.е. была найдена еще одна такая услуга, то исключаем ее из оплаты
                    if (kRecIsslProfPr > 0) {
                        card.setCorrect(false);
                        card.setComment("У пациента исследование с таким кодом в такой день уже есть");
                        setLogs("ИНФОРМАЦИЯ (РЭЦ): " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " У пациента исследование с таким кодом в такой день уже есть");
                        setLogsInConsole("ИНФОРМАЦИЯ (РЭЦ): " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " У пациента исследование с таким кодом в такой день уже есть");
                    } else {
                        //Иначе услуга корректная
                        card.setCorrect(true);
                    }
                    //Проверка наличия проф. приема с таким же кодом медуслуги на такого пациента в этот день. Конец
                }
            }

            //Проверка медуслуг ОППЗ. Начало
            if (card.isCorrect()) {
                //Если услуга ОППЗ
                if (isOPPZ) {

                    //То получаем список со всеми пустышками, соответствующими услуге ОППЗ, т.е. все услуги и -1 кодом
                    List<Cards> recList = allCardsRecList.stream()
                            .filter(cards -> cards.getSnPol().equals(card.getSnPol()))
                            .filter(cards -> cards.getCode_md().equals(card.getCode_md()))
                            .filter(cards -> cards.getCode_usl().trim().equals(card.getCode_usl().trim() + "-1")).toList();

                    //В качестве минимальной даты берем максимально возможное
                    LocalDate minDate = LocalDate.MAX;

                    //Если список пустышек пустой, то значит для проверяемой услуги нет соответствующих ОППЗ и проверяемая услуга исключается из оплаты
                    if (recList.size() == 0) {
                        card.setCorrect(false);
                        card.setComment("Нет соответствующих ОППЗ исследований");
                        setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Нет соответствующих ОППЗ исследований");
                        setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Нет соответствующих ОППЗ исследований");
                    }
                    //Если список пустышек не пустой, то каждую пустышку исключаем из оплаты и пишем сообщение в поле комментарий
                    for (Cards cardOPPZOdnoIzPocesh : recList) {
                        cardOPPZOdnoIzPocesh.setCorrect(false);
                        cardOPPZOdnoIzPocesh.setComment("Является частью ОППЗ: " + card.getSnPol() + " " + card.getCode_md() + " " + card.getCode_usl());
                        //Если дата пустышки раньше чем минимальная, то минимальной датой делаем дату пустышки
                        if (cardOPPZOdnoIzPocesh.getDate_in().isBefore(minDate)) {
                            minDate = cardOPPZOdnoIzPocesh.getDate_in();
                        }
                    }
                    //Если минимальная дата равна максимально возможной, то такая ситуация невозможна и услуга исключается из оплаты
                    if (minDate == LocalDate.MAX) {
                        card.setCorrect(false);
                        card.setComment("Некорректное значение minDateIn при определении соответствующих ОППЗ исследований");
                        setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Некорректное значение minDateIn при определении соответствующих ОППЗ исследований");
                        setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Некорректное значение minDateIn при определении соответствующих ОППЗ исследований");
                    } else {
                        //Иначе устанавливаем дату провеяемой услуги равной минимальной дате, чтобы услуга ОППЗ охватывала период начиная с первой пустышки до конца
                        //Например если были две пустышки с датами date_in = 06.03.2023 date_out = 06.03.2023 и date_in = 10.03.2023 date_out = 10.03.2023
                        //А услуга ОППЗ, была date_in = 12.03.2023 date_out = 12.03.2023, то в итоге пустышки будут исключены из оплаты
                        //а услуга ОППЗ будет иметь даты date_in = 06.03.2023 date_out = 12.03.202
                        card.setDate_in(minDate);
                    }
                    //Если дата начала проверяемой услуги не ранее чем дата конца проверяемой услуги, то исключаем услугу
                    //Т.е. такое возможно если не было пустышки и минимальная дата у ОППЗ не изменилась
                    if (!(card.getDate_in().isBefore(card.getDate_out()))) {
                        card.setCorrect(false);
                        card.setComment("Не соблюдается условие date_in < date_out для ОППЗ");
                        setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Не соблюдается условие date_in < date_out для ОППЗ");
                        setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Не соблюдается условие date_in < date_out для ОППЗ");
                    }
                }
            }
            //Проверка медуслуг ОППЗ. Конец

            //Проверка типа полиса. Начало
            if (card.isCorrect()) {
                // Определить тип полиса
                int vpolis = determineVpolis(card.getSpolis().trim(), card.getNpolis().trim());
                //Если вычисленный тип полиса равен ошибочному
                if (vpolis == AppConstants.TYPE_POLIS_ERROR) {
                    card.setCorrect(false);
                    card.setComment("Не удалось определить тип полиса " + card.getVpolis());
                    setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Не удалось определить тип полиса " + card.getVpolis());
                    setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Не удалось определить тип полиса " + card.getVpolis());
                } else {
                    //Иначе устанавливаем тип полиса равный вычисленному
                    card.setVpolis(vpolis);
                }
            }
            //Проверка типа полиса. Конец
        }
        setLogs("Конец проверки корректности эндокринологических услуг");
        setLogsInConsole("Конец проверки корректности эндокринологических услуг");


        //Сохраняем коллекцию allCardsRecList в БД
        cardsService.saveAllCards(allCardsRecList);

        return allCardsRecList;
    }

    //Определяем тип полиса по серии и номеру полиса
    private int determineVpolis(String sPolis, String nPolis) {
        if (sPolis.trim().length() > 0 && nPolis.trim().length() == 9) {
            return AppConstants.TYPE_POLIS_OLD;
        } else {
            if (sPolis.trim().length() == 0 && nPolis.trim().length() == 9) {
                return AppConstants.TYPE_POLIS_VREMEN;
            } else {
                if (sPolis.trim().length() == 0 && nPolis.trim().length() == 16) {
                    return AppConstants.TYPE_POLIS_ENP;
                } else {
                    return AppConstants.TYPE_POLIS_ERROR;
                }
            }
        }
    }

    //УПАКОВКА УСЛУГ В СЛУЧАИ. НАЧАЛО
    //Создаем record-класс, который будет выступать в качестве ключа для упаковки услуг по полям в конструкторе. Record - новая функциональность в Java 16
    //Для объединения эндокринологических услуг по полям в конструкторе String snpol, String lpu_shnm, int prvs, boolean t_type, boolean is_onkl
    record KeyForBoxing(String snpol, String lpu_shnm, int profil, String code_md, int t_type, boolean is_onkl) {
        public KeyForBoxing(Cards cards) {
            this(cards.getSnPol(), cards.getLpu_shnm(), cards.getProfil(), cards.getCode_md(), cards.getT_type(), cards.is_onkl());
        }
    }

    //Метод группирует список эндокринологических услуг Cards по датам в зависимости от того входит или нет услугу в интервал в days дней
    public Map<LocalDate, List<Cards>> groupCardsRecByDays(List<Cards> items, int days) {
        //Сортирует коллекцию по полю date_in
        items.sort(Comparator.comparing(Cards::getDate_in));

        //Предыдущее решение, тоже актулаьное
        /*//Получаем услугу и минимальной датой
        Cards min = items.get(0);

        //Создаем новую Map, где ключ минимальная дата из группы, а значение список сгруппированных услуг
        Map<LocalDate, List<Cards>> groups = new LinkedHashMap<>();
        //Проходимся по коллекции items
        for (Cards i : items) {
            //Если разница в днях между услугой с минимальной датой и текущей меньше чем days
            if (ChronoUnit.DAYS.between(min.getDate_in(), i.getDate_in()) < days) {
                //То в map добавляем новый элемент по ключу с минимальной датой и услугой
                groups.computeIfAbsent(min.getDate_in(), k -> new ArrayList<>()).add(i);
            } else {
                //Иначе услуга с минимальной датой = i
                min = i;
                //И ее добавляем в Map
                groups.computeIfAbsent(min.getDate_in(), k -> new ArrayList<>()).add(i);
            }
            //ЛИБО ТАК СОКРАЩЕННО
            //min = ChronoUnit.DAYS.between(min.getDate_in(), i.getDate_in()) < days ? min : i;
            //groups.computeIfAbsent(min.getDate_in(), k -> new ArrayList<>()).add(i);
        }*/

        //Для вывода в консоль
        /*for (Map.Entry<LocalDate, List<Cards>> entry : groups.entrySet()) {
            System.out.println(entry.getKey());
            for (Cards card : entry.getValue()) {
                System.out.println(card + " \n");
            }
        }*/
        //return groups;

        //Последнее решение, актуальное
        LocalDate[] keyDate = {items.get(0).getDate_in()};
        return items.stream()
                .collect(Collectors.groupingBy(
                        i -> keyDate[0] = ChronoUnit.DAYS.between(keyDate[0], i.getDate_in()) < days ? keyDate[0] : i.getDate_in(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    //Метод упаковывает услуги пациента в разные случаи по входящему ключу-классу KeyForBoxing
    public Map<KeyForBoxing, List<Cards>> groupingUslInSluch(List<Cards> cardsList) {
        setLogs("Начало упаковки эндокринологических услуг в группы");
        setLogsInConsole("Начало упаковки эндокринологических услуг в группы");
        //Создаем новую Map
        Map<KeyForBoxing, List<Cards>> map = new LinkedHashMap<>();

        //Проходимся по коллекции cardsList используя for-each
        cardsList.forEach(cards -> map
                //computeIfAbsent метод при необходимости создаёт новое или возвращает существующее значение-список, для которого можно сразу же вызвать метод List:add
                .computeIfAbsent(new KeyForBoxing(cards)
                        , keyForBoxing -> new ArrayList<>())
                .add(cards));

        setLogs("Конец упаковки эндокринологических услуг в группы");
        setLogsInConsole("Конец упаковки эндокринологических услуг в группы");
        //Возвращаем map
        return map;
    }

    //Возвращаем коллекцию случаев с упакованными услугами, на основании входного ключа-класса
    public List<Sluch> getSluchList() {
        setLogs("Начало упаковки эндокринологических услуг в случаи");
        setLogsInConsole("Начало упаковки эндокринологических услуг в случаи");
        //Выбираем из БД только те услуги, для которых в поле correct стоит true. Т.е. разрешенные для оплаты
        List<Cards> cardsList = cardsService.getCardsRecList().stream().filter(Cards::isCorrect).toList();

        //Вызываем метод для упаковки услуг по ключу и записываем их коллекцию map
        Map<KeyForBoxing, List<Cards>> map = groupingUslInSluch(cardsList);

        //Создаем коллекцию для случаев, куда будем добавлять все случаи всех пациентов.
        List<Sluch> sluchList = new ArrayList<>();

        //Проходимся по ключам Map
        for (Map.Entry<KeyForBoxing, List<Cards>> entry : map.entrySet()) {
            //Для каждого внешнего ключа KeyForBoxing берем его значение (т.е. List<Cards>) и помещаем его в Map<LocalDate, List<Cards>>
            //Таким образом делим List<Cards> для каждого ключа KeyForBoxing на группы по датам в промежутке 14 дней
            //Тут уже несколько групп услуг разделенных на группы в пределах 14 дней
            Map<LocalDate, List<Cards>> localDateListMap = groupCardsRecByDays(entry.getValue(), 14);

            //Далее проходимся по каждому значению
            for (Map.Entry<LocalDate, List<Cards>> dateListEntry : localDateListMap.entrySet()) {

                //Создаем коллекцию для хранения услуг
                List<Usl> uslList = new ArrayList<>();
                //Проходимся по значениям Map по ключу
                for (Cards cards : dateListEntry.getValue()) {
                    //Создаем услугу
                    Usl usl = CreateAsumElements.createUsl(cards);
                    //Устанавливаем для usl соответствующую cards, чтобы при создании случая из cards брать всю информацию, т.к. в usl нет всех данных
                    usl.setCards(cards);
                    //Добавляем услугу в список услуг
                    uslList.add(usl);

                    //При каждом добавлении услуги, добавляем также врача в мапу vrachiMap (хранит уникальных врачей)
                    vrachiMap.put(cards.getCode_md(), getVrach(cards));
                }

                //Создаем новый случай, подавая на вход коллекцию из Cards
                Sluch sluch = CreateAsumElements.createSluch(uslList);

                //Добавляем список услуг в случай
                sluch.setUslList(uslList);
                //Добавляем случай в список случаев
                sluchList.add(sluch);
            }

            //Для логов, чтобы выводить случай и его услуги
                /*try (FileWriter writer = new FileWriter("C:\\tmp\\log3.txt", false)) {
                    for (Sluch sl : sluchList) {
                        writer.write("SLUCH: " + sl.toString() + "\n");
                        for (Usl usl : sl.getUslList()) {
                            writer.write("      USL: " + usl.toString() + "\n");
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/
        }

        setLogs("Конец упаковки эндокринологических услуг в случаи");
        setLogsInConsole("Конец упаковки эндокринологических услуг в случаи");
        //Возвращаем коллекцию случаев sluchList
        return sluchList;
    }
    //УПАКОВКА УСЛУГ В СЛУЧАИ. КОНЕЦ

    //УПАКОВКА СЛУЧАЕВ в ZAP. НАЧАЛО
    //Создаем record-класс, который будет выступать в качестве ключа для упаковки услуг по полям в конструкторе. Record - новая функциональность в Java 16
    record ZapBoxing(String snpol) {
        public ZapBoxing(Sluch sluch) {
            this(sluch.getSnPol());
        }
    }

    //Метод упаковывает случаи пациентов в разные ZAP по входящему ключу-классу ZapBoxing
    public Map<ZapBoxing, List<Sluch>> groupingSluchInZap(List<Sluch> sluchList) {
        setLogs("Начало упаковки эндокринологических случаев в группы для zap");
        setLogsInConsole("Начало упаковки эндокринологических случаев в группы для zap");

        setLogs("Кол-во случаев по РЭЦ: " + sluchList.size());
        setLogsInConsole("Кол-во случаев по РЭЦ: " + sluchList.size());

        setSluchCounterRec(sluchList.size());

        //Создаем новую Map
        Map<ZapBoxing, List<Sluch>> map = new LinkedHashMap<>();

        //Проходимся по коллекции sluchList используя for-each
        sluchList.forEach(sluch -> map
                //computeIfAbsent метод при необходимости создаёт новое или возвращает существующее значение-список, для которого можно сразу же вызвать метод List:add
                .computeIfAbsent(new ZapBoxing(sluch), zapBoxing -> new ArrayList<>())
                .add(sluch));

        setLogs("Конец упаковки эндокринологических случаев в группы для zap");
        setLogsInConsole("Конец упаковки эндокринологических случаев в группы для zap");
        //Возвращаем map
        return map;
    }

    //Возвращаем коллекцию случаев с упакованными услугами, на основании входного ключа-класса
    public List<Zap> getZapList() {
        setLogs("Начало упаковки эндокринологических случаев в zap");
        setLogsInConsole("Начало упаковки эндокринологических случаев в zap");
        //Заполняем коллекцию sluchList случаями используя метод getSluchList()
        List<Sluch> sluchList = getSluchList();

        //Вызываем метод для упаковки услуг по ключу и записываем их коллекцию map
        Map<ZapBoxing, List<Sluch>> map = groupingSluchInZap(sluchList);

        //Создаем коллекцию Zap
        List<Zap> zapList = new ArrayList<>();

        //Проходимся по ключам Map
        for (Map.Entry<ZapBoxing, List<Sluch>> entry : map.entrySet()) {
            //Создаем новый ZAP, подавая на вход коллекцию из Sluch
            Zap zap = new Zap();

            List<Sluch> sluches = new ArrayList<>();
            //Проходимся по значениям Map по ключу
            for (Sluch sluch : entry.getValue()) {
                sluches.add(sluch);
            }
            //Добавляем список случаев в zap
            zap.setSluchList(sluches);

            //Добавляем zap в список ZAPов
            zapList.add(zap);

            //Для логов, чтобы выводить zap и его услуги
            /*try (FileWriter writer = new FileWriter("C:\\tmp\\log4.txt", false)) {
                for (Zap z : zapList) {
                    writer.write("ZAP: " + z.toString() + "\n");
                    for (Sluch sluch : z.getSluchList()) {
                        writer.write("\tSLUCH: " + sluch.toString() + "\n");
                        for (Usl usl : sluch.getUslList()) {
                            writer.write("\t\tUSL: " + usl.toString() + "\n");
                            writer.write("\t\t\tMR_USL_N: " + usl.getMrUslN().toString() + "\n");
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
        }

        setLogs("Конец упаковки эндокринологических случаев в zap");
        setLogsInConsole("Конец упаковки эндокринологических случаев в zap");
        //Возвращаем коллекцию случаев zapList
        return zapList;
    }
    //УПАКОВКА СЛУЧАЕВ в ZAP. КОНЕЦ

    //Создание сегментов и узлов для ASUM файла. НАЧАЛО
    public void createRecAsumFile(List<Zap> zapList) {
        setLogs("Начало создания ASUM файла по РЭЦ");
        setLogsInConsole("Начало создания ASUM файла по РЭЦ");

        File dir = new File(AppConstants.asumFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (FileOutputStream out = new FileOutputStream(AppConstants.asumFilePath + AppConstants.ASUM_FILE_NAME_REC + yearMonth + ".xml");
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out)) {
            writeXML(bufferedOutputStream, zapList);
        } catch (XMLStreamException | IOException e) {
            //throw new RuntimeException(e);
            Platform.runLater(() -> {
                mainController.getLogs().appendText(e.toString());
            });
        }


        //Для красивой печати
        /*try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //FileOutputStream out = new FileOutputStream("C:\\tmp\\text.xml");
            writeXML(out, zapList);

            String xml = new String(out.toByteArray(), StandardCharsets.UTF_8);

            String prettyPrintXML = formatXML(xml);

            Files.writeString(Paths.get("C:\\tmp\\text.xml"), prettyPrintXML, StandardCharsets.UTF_8);
        } catch (XMLStreamException | TransformerException | IOException e) {
            throw new RuntimeException(e);
        }*/

        setLogs("Конец создания ASUM файла по РЭЦ");
        setLogsInConsole("Конец создания ASUM файла по РЭЦ");
    }

    //Записываем данные в XML
    private void writeXML(BufferedOutputStream outputStream, List<Zap> zapList) throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(outputStream,"UTF-8");

        writer.writeStartDocument("utf-8", "1.0");

        writer.writeStartElement("zl_list");

        Zglv zglv = new Zglv("1", "AAAAAAAA", "1.14", UtilDate.getCurrentDate(), AppConstants.ASUM_FILE_NAME_REC + yearMonth);
        WriteAsumXmlSegments.writeZglv(writer, zglv);

        //Записываем ZAPы
        for (Zap zap : zapList) {
            WriteAsumXmlSegments.writeZap(writer, zap);
        }

        //Записываем врачей в xml
        for (Map.Entry<String, Vrachi> entry : vrachiMap.entrySet()) {
            WriteAsumXmlSegments.writeVrachi(writer, entry.getValue());
        }

        writer.writeEndElement();

        writer.writeEndDocument();

        writer.flush();
        writer.close();
    }

    private static String formatXML(String xml) throws TransformerException {

        // write data to xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();

        // alternative
        //Transformer transformer = SAXTransformerFactory.newInstance().newTransformer();

        // pretty print by indention
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        // add standalone="yes", add line break before the root element
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

      /*transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
              "-//W3C//DTD XHTML 1.0 Transitional//EN");

      transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
              "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");*/

        // ignore <?xml version="1.0" encoding="UTF-8"?>
        //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        StreamSource source = new StreamSource(new StringReader(xml));
        StringWriter output = new StringWriter();
        transformer.transform(source, new StreamResult(output));

        return output.toString();
    }

    //Метод создает врача
    private Vrachi getVrach(Cards cards) {
        Vrachi vrachi = new Vrachi();
        vrachi.setKod(cards.getCode_md() + "");
        vrachi.setFio(cards.getVr_fio());
        vrachi.setMcod(AppConstants.TFOMS_CODE_REC);

        Optional<Medspec> medspecOptional = medspecService.findByIdmsp(cards.getPrvs() + "");

        medspecOptional.ifPresent(medspec -> vrachi.setIdmsp(medspec.getIdmsp()));
        medspecOptional.ifPresent(medspec -> vrachi.setSpec(medspec.getMspname()));

        vrachi.setDost("false");
        vrachi.setType("1");

        return vrachi;
    }

}