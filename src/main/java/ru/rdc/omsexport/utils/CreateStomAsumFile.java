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
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

//Класс для создания ASUM файл для Стоматологии
@Service
public class CreateStomAsumFile {
    Map<String, Vrachi> vrachiMap = new HashMap<>(); //Будет хранить всех уникальных врачей
    private static String yearMonth = ""; //Хранит год и месяц для названия файла
    private boolean isChkPredUslDate = false; //Проверка установлен ли чекбокс chkPredUslDate
    private MainController mainController;
    private final SmoService smoService;
    private final SpTarifExtendedService spTarifExtendedService;
    private final MkbExtendedService mkbExtendedService;
    private final CardsService cardsService;
    private final SlpuService slpuService;
    private final MedspecService medspecService;

    @Autowired
    public CreateStomAsumFile(
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

    public void setChkPredUslDate(boolean chkPredUslDate) {
        isChkPredUslDate = chkPredUslDate;
    }

    public boolean isChkPredUslDate() {
        return isChkPredUslDate;
    }

    //Метод для вывода логов в TextArea в main.fxml
    private void setLogs(String message) {
        //Изменение любых данных в интерфейсе JavaFX нужно делать в отдельном потоке
        Platform.runLater(() -> mainController.getLogs().appendText(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(LocalDateTime.now()) + " " + message + "\n"));
    }

    //Метод для вывода логов в консоль
    private void setLogsInConsole(String message) {
        System.out.println(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(LocalDateTime.now()) + " " + message);
    }

    private void setCounterStom(int all, int checked) {
        Platform.runLater(() -> mainController.getCounterKpStom().setText("СТОМ: " + all + " / " + checked));
    }

    private void setSluchCounterStom(int counter) {
        Platform.runLater(() -> mainController.getSluchCounterKPStom().setText("/ Случаев: " + counter));
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

    //Чтение стоматологических входных таблиц Cards. Используется таблица для поликлиники
    public List<Cards> readCardsStomDBF() {
        setLogs("Начало чтения стоматологических входящих таблиц выгрузки (DBF).");
        setLogsInConsole("Начало чтения стоматологических входящих таблиц выгрузки (DBF).");

        setLogs("Расчет для стоматологии выполняется");
        setLogsInConsole("Расчет для стоматологии выполняется");
        List<Cards> stomList = new ArrayList<>(ReadCardsDBF.readCardsDBF(AppConstants.cardsDBFPath + "cards_kp.dbf", "866"));

        setLogs("Конец чтения стоматологических входящих таблиц выгрузки (DBF).");
        setLogsInConsole("Конец чтения стоматологических входящих таблиц выгрузки (DBF).");

        //Возвращаем коллекцию stomList
        return stomList;
    }

    //Метод будет каждую стоматологическую услугу из cards отмечать годными для подачи на оплату и устанавливать доп. параметры такие как comment, t_type, is_onkl
    //Передаем List<String> checks параметры, чтобы знать для каких отделений делать расчет.
    //Проверяем стоматологическе услуги
    public void selectCorrectCardsStom() {
        //Содержит проверенные услуги
        List<Cards> allCardsListStom = readCardsStomDBF();

        //Для каждой услуги из allCardsStomList выполняем проверки на допустимость подачи на оплату
        setLogs("Начало проверки корректности стоматологических услуг.");
        setLogsInConsole("Начало проверки корректности стоматологических услуг.");
        int counter = 0; //Счетчик проверки услуг
        for (Cards card : allCardsListStom) {

            //Выводим кол-во проверенных услуг
            counter++;
            setCounterStom(allCardsListStom.size(),counter);

            //Для упрощения всем стом. услугам ставим mcod = 0501YD
            card.setMcod(AppConstants.TFOMS_CODE_KPSTOM);

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

            //Получаем optional из таблицы s_lpu по mcod. lpu_shnm - это mcod (подразделение, которое направило)
            Optional<Slpu> slpuOptional = slpuService.findSlpuByMcod(card.getLpu_shnm().trim());
            //Получаем optional из таблицы smo
            Optional<Smo> smoOptional = smoService.findBySmocod(card.getSmocod().trim());
            //Создаем Optional для дальнейшей проверки услуги
            Optional<SpTarifExtended> spTarifNewOptional = spTarifExtendedService.findByKsg(card.getCode_usl().trim());
            //Получаем МКБ из таблицы mkb_extended на основании первичного диагноза из услуги
            Optional<MkbExtended> mkbExtendedOptionalMkbCodeP = mkbExtendedService.findByLcod(card.getMkb_code_p().trim());
            //Получаем в optional основной диагноз МКБ
            Optional<MkbExtended> mkbExtendedOptionalForMkbCode = mkbExtendedService.findByLcod(card.getMkb_code());
            //Получаем из таблицы medspec - специальность по полю prvs из входной таблицы cards
            Optional<Medspec> medspecOptional = medspecService.findByIdmsp(Integer.toString(card.getPrvs()).trim());

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

            //Если раздел прейскуранта не стоматология
            if (!card.getMetPrKod().trim().equals(AppConstants.ARIADNA_USL_RAZDEL_STOMATOLOGY)) {
                card.setCorrect(false);
                card.setComment("Некорректный раздел медуслуги в Ариадне " + card.getMetPrKod() + " для услуги " + card.getCode_usl() + " - СТОМАТОЛОГИЯ");
                setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", услуга " + card.getCode_usl() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                        + " Некорректный раздел медуслуги в Ариадне " + card.getMetPrKod() + " - СТОМАТОЛОГИЯ");
                setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", услуга " + card.getCode_usl() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                        + " Некорректный раздел медуслуги в Ариадне " + card.getMetPrKod() + " - СТОМАТОЛОГИЯ");
            }

            //Если услуга не исключается, то проверяем на наличие smocod в таблице smo.dbf
            if (card.isCorrect()) {
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

                if (card.getCode_usl().trim().length() > AppConstants.CODE_USL_MAX_LEN) {
                    card.setCorrect(false);
                    card.setComment("Некорректный код медуслуги ТФОМС " + card.getCode_usl());
                    setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Некорректный код медуслуги ТФОМС " + card.getCode_usl());
                    setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Некорректный код медуслуги ТФОМС " + card.getCode_usl());
                } else {
                    if (spTarifNewOptional.isEmpty()) {
                        card.setCorrect(false);
                        card.setComment("Не удалось определить код медуслуги ТФОМС code_usl: " + card.getCode_usl());
                        setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Не удалось определить код медуслуги ТФОМС " + card.getCode_usl());
                        setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Не удалось определить код медуслуги ТФОМС " + card.getCode_usl());
                    } else {
                        //Если профиль услуги <= 0
                        if (spTarifNewOptional.get().getIdpr() <= 0) {
                            card.setCorrect(false);
                            card.setComment("Неверный профиль у исследования в sp_tarif: " + card.getCode_usl());
                            setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                    + " Неверный профиль у исследования в sp_tarif");
                            setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                    + " Неверный профиль у исследования в sp_tarif");
                        } else {
                            //Иначе, если стоимость услуги отличается от той что в sp_tarif
                            if (card.getTarif() != spTarifNewOptional.get().getPrice()) {
                                card.setTarif(spTarifNewOptional.get().getPrice());
                            }
                            //Добавляем в cards_stom информацию о t_type из sp_tarif_extended
                            card.setT_type(spTarifNewOptional.get().getT_type());
                            //Добавляем в cards профиль для дальнейшей группировки услуг по профилям
                            card.setProfil(spTarifNewOptional.get().getIdpr());
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
                            + " Некорректный код МКБ mkb_code " + card.getMkb_code());
                    setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Некорректный код МКБ mkb_code " + card.getMkb_code());
                } else { //Иначе, если длина не равна 0
                    // 2018-11-06 для диагностики диагноз должен предоставляться направившим ЛПУ и именно он и является основным - Асият Магомедовна - из программы Дениса
                    if (card.getMkb_code_p() != null && card.getMkb_code_p().trim().length() > 0) {
                        //Если на основании первичного диагноза из услуги код МКБ не найден в таблице mkb_extended
                        if (mkbExtendedOptionalMkbCodeP.isEmpty()) {
                            card.setCorrect(false);
                            card.setComment("Код МКБ mkb_code_p не найден в mkb.dbf = " + card.getMkb_code_p());
                            setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                    + " Код МКБ mkb_code_p не найден в mkb.dbf = " + card.getMkb_code_p());
                            setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                    + " Код МКБ mkb_code_p не найден в mkb.dbf = " + card.getMkb_code_p());
                        } else {
                            //Добавляем в cards_stom информацию о is_onkl из mkb_extended
                            card.set_onkl(mkbExtendedOptionalMkbCodeP.get().is_onk());
                            //Если есть нормальный первичный код, то его делаем также основным
                            card.setMkb_code(mkbExtendedOptionalMkbCodeP.get().getLcod());
                        }
                    } else { //Иначе проверяем наличие основного МКБ на таблице mkb.dbf
                        //Если основной диагноз не найден в mkb_extended
                        if (mkbExtendedOptionalForMkbCode.isEmpty()) {
                            card.setCorrect(false);
                            card.setComment("Код МКБ mkb_code не найден в mkb.dbf = " + card.getMkb_code());
                            setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                    + " Код МКБ mkb_code не найден в mkb.dbf = " + card.getMkb_code());
                            setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                    + " Код МКБ mkb_code не найден в mkb.dbf = " + card.getMkb_code());
                        } else {
                            //Добавляем в cards_stom информацию о is_onkl из mkb_extended
                            card.set_onkl(mkbExtendedOptionalForMkbCode.get().is_onk());
                        }
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
                            + " Некорректное направившее ЛПУ " + card.getLpu_shnm());
                    setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Некорректное направившее ЛПУ " + card.getLpu_shnm());
                }

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
                    card.setComment("Не определён врач code_md = " + card.getCode_md());
                    setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Не определён врач code_md = " + card.getCode_md());
                    setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Не определён врач code_md = " + card.getCode_md());
                } else {
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

            //Если услуга корректная
            if (card.isCorrect()) {
                //Если установлена галочка chkPredUslDate. Нужна только для предварительных файлов
                if (isChkPredUslDate()) {
                    if (card.getDate_in().isAfter(LocalDate.now())) {
                        setLogsInConsole("Услуга " + card.getCode_usl() + " была выполнена позднее чем текущая дата, для нее были изменены даты выполнения: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp() + ", DATE_IN " + card.getDate_in());
                        setLogs("Услуга " + card.getCode_usl() + " была выполнена позднее чем текущая дата, для нее были изменены даты выполнения: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp() + ", DATE_IN " + card.getDate_in());
                        card.setDate_in(LocalDate.now());
                        card.setDate_out(LocalDate.now());
                    }
                }
            }

            //ДОБАВЛЕНИЕ СВОЙСТВ ДЛЯ CARDS. МОЖНО ДОБАВЛЯТЬ ЛЮБЫЕ СВОЙСТВА, ЧТОБЫ НАПРИМЕР ДАЛЕЕ ДЕЛАТЬ РАЗБИВКУ ПО НИМ
            if (spTarifNewOptional.isPresent()) {
                //Добавлено 03.10.2023 в связи с обновлениями ТФОМС. Теперь для каждой услуги проверяется соответствие услуги способу оплаты
                card.setUsl_idsp(spTarifNewOptional.get().getUsl_idsp());
            }

        }
        setLogs("Конец проверки корректности стоматологических услуг.");
        setLogsInConsole("Конец проверки корректности стоматологических услуг.");

        //ПРОВЕРКА НАЛИЧИЯ ИССЛЕДОВАНИЙ С ТАКИМ ЖЕ КОДОМ МЕДУСЛУГИ НА ТАКОГО ПАЦИЕНТА В ЭТОТ ДЕНЬ. НАЧАЛО
        //ДЕЛАЕМ В ОТДЕЛЬНОМ ЦИКЛЕ
        setLogs("Проверка наличия у одного пациента одинаковых услуг в один день. Начало");
        setLogsInConsole("Проверка наличия у одного пациента одинаковых услуг в один день. Начало");
        //Сортируем коллекцию allCardsListStom перед началом проверки по snPol, фамилии, имени и отчеству, дате обследования
        //Перенес сортировку по коду услуги выше т.к. были случаи когда на два пациента один полис и все их услуги не отсекались при дублировании
        allCardsListStom.sort(Comparator.comparing(Cards::getSnPol)
                .thenComparing(Cards::getCode_usl)
                .thenComparing(Cards::getDate_in)
                .thenComparing(Cards::getFam)
                .thenComparing(Cards::getIm)
                .thenComparing(Cards::getOt));

        //Проходимся по все отсортированной коллекции
        for (int i = 0; i < allCardsListStom.size(); i++) {
            //Если услуга ранее попала в список разрешенных на оплату
            if (allCardsListStom.get(i).isCorrect()) {
                //Получаем Optional для sp_tarif_extended для проверяемой услуги
                Optional<SpTarifExtended> spTarifExtendedOptional = spTarifExtendedService.findByKsg(allCardsListStom.get(i).getCode_usl());

                //Если услуга вернулась в optional и может быть оказана несколько раз (т.е. для нее есть кратность)
                if (spTarifExtendedOptional.isPresent() && spTarifExtendedOptional.get().getKr_mul() == 1) {
                    //Получаем текущую услугу
                    Cards current = allCardsListStom.get(i);
                    //Получаем LocalDate для текущей услуги
                    LocalDate currentCardsDate = current.getDate_in();
                    //Первая полученная, текущая услуга будет true
                    current.setCorrect(true);
                    //Пока закомментируем current.setKol_usl(1); т.к. всегда бывает 1
                    //current.setKol_usl(1);
                    //Второй цикл начинается уже со следующего элемента коллекции allCardsListStom
                    for (int j = i + 1; j < allCardsListStom.size(); j++) {
                        //Получаем следующий элемент
                        Cards next = allCardsListStom.get(j);
                        //Получаем LocalDate для следующей услуги
                        LocalDate nextCardsDate = next.getDate_in();
                        //Если полис, фио и дата услуги, код услуги следующего пациента не совпадает с текущим, значит данные не относятся к текущему и прерываем цикл
                        //Отдельно по новорожденным условия не нужно ставить, т.к. условие ниже учитывает услуги матери и новорожденного и корректно отработает
                        if (!next.getSnPol().equals(current.getSnPol()) || !next.getFam_n().equals(current.getFam_n()) || !next.isNovor() == current.isNovor() ||
                                !next.getIm_n().equals(current.getIm_n()) || !next.getOt_n().equals(current.getOt_n()) || !nextCardsDate.equals(currentCardsDate) ||
                                !next.getCode_usl().equals(current.getCode_usl())) {
                            break;
                        }
                        //У текущей услуги увеличиваем количество услуг на 1...
                        current.setKol_usl(current.getKol_usl() + 1);

                        //2023-04-05 - решил сразу ставить макисимальную кратность, если пациенту сделано больше максимальной кратности из таблицы usl_kratnost_multi, чтобы не было в МЭК дублей по кратности
                        if (current.getKol_usl() >= spTarifExtendedOptional.get().getMax_krat()) {
                            current.setKol_usl(spTarifExtendedOptional.get().getMax_krat());
                        }
                        //Следующий исключаем из оплаты
                        next.setCorrect(false);
                        next.setComment("У пациента исследование с таким кодом в такой день уже есть");
                        setLogs("У пациента " + next.getSnPol() + " исследование с таким кодом " + next.getCode_usl().trim() + " в такой день уже есть");
                        setLogsInConsole("У пациента " + next.getSnPol() + " исследование с таким кодом " + next.getCode_usl().trim() + " в такой день уже есть");
                        //Увеличивается индекс во внешнем цикле, чтобы пропустить уже обработанные дубликаты.
                        i = j;
                    }
                } else {
                    //Если вернулась услуга и kr_mil = 0, т.е. нельзя подавать несколько раз
                    if (spTarifExtendedOptional.isPresent() && spTarifExtendedOptional.get().getKr_mul() == 0) {
                        //Получаем текущую услугу
                        Cards current = allCardsListStom.get(i);
                        //Получаем LocalDate для текущей услуги
                        LocalDate currentCardsDate = current.getDate_in();
                        //Первая полученная, текущая услуга будет true
                        current.setCorrect(true);
                        //Второй цикл начинается уже со следующего элемента коллекции allCardsListStom
                        for (int j = i + 1; j < allCardsListStom.size(); j++) {
                            //Получаем следующий элемент
                            Cards next = allCardsListStom.get(j);
                            //Получаем LocalDate для следующей услуги
                            LocalDate nextCardsDate = next.getDate_in();
                            //Если полис, фио и дата услуги, код услуги следующего пациента не совпадает с текущим, значит данные не относятся к текущему и прерываем цикл
                            //Отдельно по новорожденным условия не нужно ставить, т.к. условие ниже учитывает услуги матери и новорожденного и корректно отработает
                            if (!next.getSnPol().equals(current.getSnPol()) || !next.getFam_n().equals(current.getFam_n()) || !next.isNovor() == current.isNovor() ||
                                    !next.getIm_n().equals(current.getIm_n()) || !next.getOt_n().equals(current.getOt_n()) || !nextCardsDate.equals(currentCardsDate) ||
                                    !next.getCode_usl().equals(current.getCode_usl())) {
                                break;
                            }
                            //Следующий исключаем из оплаты
                            next.setCorrect(false);
                            next.setComment("Услуга будет отсечена, т.к. не разрешена для подачи в один день несколько раз ");
                            setLogs("Услуга " + next.getCode_usl().trim() + " пациента " + next.getSnPol() + " будет отсечена, т.к. не разрешена для подачи в один день несколько раз");
                            setLogsInConsole("Услуга " + next.getCode_usl().trim() + " пациента " + next.getSnPol() + " будет отсечена, т.к. не разрешена для подачи в один день несколько раз");
                            i = j;
                        }
                    }
                }
            }
        }
        setLogs("Проверка наличия у одного пациента одинаковых услуг в один день. Конец");
        setLogsInConsole("Проверка наличия у одного пациента одинаковых услуг в один день. Конец");
        //ПРОВЕРКА НАЛИЧИЯ ИССЛЕДОВАНИЙ С ТАКИМ ЖЕ КОДОМ МЕДУСЛУГИ НА ТАКОГО ПАЦИЕНТА В ЭТОТ ДЕНЬ. КОНЕЦ

        //Сохраняем коллекцию allCardsListStom в БД
        cardsService.saveAllCards(allCardsListStom);
    }
    record KeyForBoxing(long visitid) {
        public KeyForBoxing(Cards cards) {
            this(cards.getVisitid());
        }
    }

    //Метод упаковывает услуги пациента в разные случаи по входящему ключу-классу KeyForBoxing
    public Map<CreateStomAsumFile.KeyForBoxing, List<Cards>> groupingUslInSluch(List<Cards> cardsList) {
        setLogs("Начало упаковки стоматологических услуг в группы");
        setLogsInConsole("Начало упаковки стоматологических услуг в группы");
        //Создаем новую Map
        Map<CreateStomAsumFile.KeyForBoxing, List<Cards>> map = new LinkedHashMap<>();

        //Проходимся по коллекции cardsList используя for-each
        //computeIfAbsent метод при необходимости создаёт новое или возвращает существующее значение-список, для которого можно сразу же вызвать метод List:add
        cardsList.forEach(cards -> map
                .computeIfAbsent(new CreateStomAsumFile.KeyForBoxing(cards)
                        , keyForBoxing -> new ArrayList<>())
                .add(cards));

        setLogs("Конец упаковки стоматологических услуг в группы");
        setLogsInConsole("Конец упаковки стоматологических услуг в группы");

        //Возвращаем map
        return map;
    }

    //Возвращаем коллекцию случаев с упакованными услугами, на основании входного ключа-класса
    public List<Sluch> getSluchList() {
        setLogs("Начало упаковки стоматологических услуг в случаи");
        setLogsInConsole("Начало стоматологических упаковки услуг в случаи");

        //Выбираем только диагностические услуги
        List<Cards> cardsList = cardsService.getCardsKpStomList();

        //Вызываем метод для упаковки услуг по ключу и записываем их коллекцию map
        Map<CreateStomAsumFile.KeyForBoxing, List<Cards>> map = groupingUslInSluch(cardsList);

        //Создаем коллекцию для случаев, куда будем добавлять все случаи всех пациентов.
        List<Sluch> sluchList = new ArrayList<>();

        //Проходимся по ключам Map
        for (Map.Entry<CreateStomAsumFile.KeyForBoxing, List<Cards>> entry : map.entrySet()) {

            //Создаем коллекцию для хранения услуг
            List<Usl> uslList = new ArrayList<>();
            //Проходимся по значениям Map по ключу
            for (Cards cards : entry.getValue()) {
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

        setLogs("Конец упаковки стоматологических услуг в случаи");
        setLogsInConsole("Конец стоматологических упаковки услуг в случаи");
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
    public Map<CreateStomAsumFile.ZapBoxing, List<Sluch>> groupingSluchInZap(List<Sluch> sluchList) {
        setLogs("Начало упаковки стоматологических случаев в группы для zap");
        setLogsInConsole("Начало упаковки стоматологических случаев в группы для zap");

        setLogs("Кол-во случаев по стоматологии: " + sluchList.size());
        setLogsInConsole("Кол-во случаев по стоматологии: " + sluchList.size());

        setSluchCounterStom(sluchList.size());

        //Создаем новую Map
        Map<CreateStomAsumFile.ZapBoxing, List<Sluch>> map = new LinkedHashMap<>();

        //Проходимся по коллекции sluchList используя for-each
        //computeIfAbsent метод при необходимости создаёт новое или возвращает существующее значение-список, для которого можно сразу же вызвать метод List:add
        sluchList.forEach(sluch -> map
                .computeIfAbsent(new CreateStomAsumFile.ZapBoxing(sluch), zapBoxing -> new ArrayList<>())
                .add(sluch));

        setLogs("Конец упаковки стоматологических случаев в группы для zap");
        setLogsInConsole("Конец упаковки стоматологических случаев в группы для zap");
        //Возвращаем map
        return map;
    }

    //Возвращаем коллекцию случаев с упакованными услугами, на основании входного ключа-класса
    public List<Zap> getZapList() {
        setLogs("Начало упаковки стоматологических случаев в zap");
        setLogsInConsole("Начало упаковки стоматологических случаев в zap");
        //Заполняем коллекцию sluchList случаями используя метод getSluchList()
        List<Sluch> sluchList = getSluchList();

        //Вызываем метод для упаковки услуг по ключу и записываем их коллекцию map
        Map<CreateStomAsumFile.ZapBoxing, List<Sluch>> map = groupingSluchInZap(sluchList);

        //Создаем коллекцию Zap
        List<Zap> zapList = new ArrayList<>();

        //Проходимся по ключам Map
        for (Map.Entry<CreateStomAsumFile.ZapBoxing, List<Sluch>> entry : map.entrySet()) {
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
        }

        setLogs("Конец упаковки стоматологических случаев в zap");
        setLogsInConsole("Конец упаковки стоматологических случаев в zap");
        //Возвращаем коллекцию случаев zapList
        return zapList;
    }
    //УПАКОВКА СЛУЧАЕВ в ZAP. КОНЕЦ

    //Создание сегментов и узлов для ASUM файла. НАЧАЛО
    public void createStomAsumFile(List<Zap> zapList) {
        setLogs("Начало создания ASUM файла по стоматологии");
        setLogsInConsole("Начало создания ASUM файла по стоматологии");

        File dir = new File(AppConstants.asumFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (FileOutputStream out = new FileOutputStream(AppConstants.asumFilePath + AppConstants.ASUM_FILE_NAME_STOM + yearMonth + ".xml");
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out)) {
            writeXML(bufferedOutputStream, zapList);
        } catch (XMLStreamException | IOException e) {
            Platform.runLater(() -> {
                mainController.getLogs().appendText(e.toString());
            });
        }

        setLogs("Конец создания ASUM файла по стоматологии");
        setLogsInConsole("Конец создания ASUM файла по стоматологии");
    }

    //Записываем данные в XML
    private void writeXML(BufferedOutputStream outputStream, List<Zap> zapList) throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XMLStreamWriter writer = factory.createXMLStreamWriter(byteArrayOutputStream, "UTF-8");

        writer.writeStartDocument("utf-8", "1.0");

        writer.writeStartElement("zl_list");

        Zglv zglv = new Zglv("1", "AAAAAAAA", "1.14", UtilDate.getCurrentDate(), AppConstants.ASUM_FILE_NAME_STOM + yearMonth);
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

        // Преобразование в форматированный XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        Source xmlInput = new StreamSource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        Result xmlOutput = new StreamResult(outputStream);
        try {
            transformer.transform(xmlInput, xmlOutput);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    //Метод создает врача
    private Vrachi getVrach(Cards cards) {
        Vrachi vrachi = new Vrachi();
        vrachi.setKod(cards.getCode_md() + "");
        vrachi.setFio(cards.getVr_fio());
        vrachi.setMcod(AppConstants.TFOMS_CODE_KPSTOM);

        Optional<Medspec> medspecOptional = medspecService.findByIdmsp(cards.getPrvs() + "");

        medspecOptional.ifPresent(medspec -> vrachi.setIdmsp(medspec.getIdmsp()));
        medspecOptional.ifPresent(medspec -> vrachi.setSpec(medspec.getMspname()));

        vrachi.setDost("false");
        vrachi.setType("1");

        return vrachi;
    }
}
