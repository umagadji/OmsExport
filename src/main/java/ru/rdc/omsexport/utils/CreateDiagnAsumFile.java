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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

//Класс для создания ASUM файл для диагностики
@Service
public class CreateDiagnAsumFile {
    Map<String, Vrachi> vrachiMap = new HashMap<>(); //Будет хранить всех уникальных врачей
    private static String yearMonth = ""; //Хранит год и месяц для названия файла
    private MainController mainController;
    private final SmoService smoService;
    private final SpTarifExtendedService spTarifExtendedService;
    private final MkbExtendedService mkbExtendedService;
    private final CardsService cardsService;
    private final SlpuService slpuService;
    private final MedspecService medspecService;

    @Autowired
    public CreateDiagnAsumFile(
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
        Platform.runLater(() -> mainController.getLogs().appendText(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(LocalDateTime.now()) + " " + message + "\n"));
    }

    //Метод для вывода логов в консоль
    private void setLogsInConsole(String message) {
        System.out.println(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(LocalDateTime.now()) + " " + message);
    }

    private void setCounterRDC(int all, int checked) {
        Platform.runLater(() -> mainController.getCounterRdc().setText("РДЦ: " + all + " / " + checked));
    }

    private void setSluchCounterRdc(int counter) {
        Platform.runLater(() -> mainController.getSluchCounterRDC().setText("/ Случаев: " + counter));
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

    //Передаем List<String> checks параметры, чтобы знать для каких отделений делать расчет.
    //Значения параметров приходят из контроллера MainController
    //Чтение диагностических входных таблиц Cards
    public List<Cards> readCardsDiagnDBF(List<String> list) {
        //Коллекция будет содержать услуги по диагностике
        List<Cards> diagnList = new ArrayList<>();

        if (list.size() > 0) {
            setLogs("Начало чтения диагностических входящих таблиц выгрузки (DBF).");
            setLogsInConsole("Начало чтения диагностических входящих таблиц выгрузки (DBF).");

            if (list.contains("1")) {
                setLogs("Расчет для ОФД выполняется");
                setLogsInConsole("Расчет для ОФД выполняется");
                diagnList.addAll(ReadCardsDBF.readCardsDBF(AppConstants.cardsDBFPath + "cards_fd.dbf", "866"));
            }

            //Для эндоскопии необходимо, чтобы также расчет выполнялся по КП, чтобы услуги ларингоскопии из КП добавить в эндоскопию
            if (list.contains("2") && list.contains("7")) {
                setLogs("Расчет для эндоскопии выполняется");
                setLogsInConsole("Расчет для эндоскопии выполняется");
                diagnList.addAll(ReadCardsDBF.readCardsDBF(AppConstants.cardsDBFPath + "cards_endosk.dbf", "866"));

                //Читаем dbf поликлиники и берем оттуда услугу 56882 (ларингоскопия)
                List<Cards> cardsPolList = ReadCardsDBF.readCardsDBF(AppConstants.cardsDBFPath + "cards_kp.dbf", "866")
                        .stream().filter(cardsDiagn -> cardsDiagn.getCode_usl().equals("56882")).toList();

                //Заменяем номер отделения на эндоскопию
                cardsPolList.forEach(cardsDiagn -> cardsDiagn.setOtd(2));

                //Добавляем в коллекцию с диагностическими услугами
                diagnList.addAll(cardsPolList);
            }

            if (list.contains("3")) {
                setLogs("Расчет для УЗИ выполняется");
                setLogsInConsole("Расчет для УЗИ выполняется");
                diagnList.addAll(ReadCardsDBF.readCardsDBF(AppConstants.cardsDBFPath + "cards_uzi.dbf", "866"));
            }

            if (list.contains("4")) {
                setLogs("Расчет для рентген выполняется");
                setLogsInConsole("Расчет для рентген выполняется");
                diagnList.addAll(ReadCardsDBF.readCardsDBF(AppConstants.cardsDBFPath + "cards_rentgen.dbf", "866"));
            }

            if (list.contains("5")) {
                setLogs("Расчет для лаборатории выполняется");
                setLogsInConsole("Расчет для лаборатории выполняется");
                diagnList.addAll(ReadCardsDBF.readCardsDBF(AppConstants.cardsDBFPath + "cards_lab.dbf", "866"));
            }

            setLogs("Конец чтения диагностических входящих таблиц выгрузки (DBF).");
            setLogsInConsole("Конец чтения диагностических входящих таблиц выгрузки (DBF).");
        }

        //Возвращаем коллекцию diagnList
        return diagnList;
    }

    //Метод будет каждую диагностическую услугу из cards отмечать годными для подачи на оплату и устанавливать доп. параметры такие как comment, t_type, is_onkl
    //Передаем List<String> checks параметры, чтобы знать для каких отделений делать расчет.
    //Проверяем диагностические услуги
    public void selectCorrectCardsDiagn(List<String> checks) {
        //Содержит проверенные услуги
        List<Cards> allCardsListDiagnDiagn = readCardsDiagnDBF(checks);

        //Для каждой услуги из allCardsDiagnList выполняем проверки на допустимость подачи на оплату
        setLogs("Начало проверки корректности диагностических услуг.");
        setLogsInConsole("Начало проверки корректности диагностических услуг.");
        int counter = 0; //Счетчик проверки услуг
        for (Cards card : allCardsListDiagnDiagn) {

            //Выводим кол-во проверенных услуг
            counter++;
            setCounterRDC(allCardsListDiagnDiagn.size(),counter);

            //Для упрощения всем диаг. услугам ставим mcod = 050130
            card.setMcod(AppConstants.TFOMS_CODE_RDC);

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

            if ((card.getProfil() != 15 || card.getProfil() != 34 || card.getProfil() != 67) && (card.getMkb_code().trim().equals("Z01.7"))) {
                card.setMkb_code("Z03.8");
            }

            if ((card.getProfil() != 15 || card.getProfil() != 34 || card.getProfil() != 67) && (card.getMkb_code_p().trim().equals("Z01.7"))) {
                card.setMkb_code_p("Z03.8");
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
                if (card.getCode_usl().trim().length() == 6 || card.getCode_usl().trim().length() == 7) {
                    //setLogs("ИНФОМАЦИЯ : длина услуги " + card.getCode_usl().trim().length() + " знаков " + "SNPOL " + card.getSnPol() + " N_OTD " + card.getN_otd() + " N_MKP " + card.getN_mkp());
                }

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
                            //Добавляем в cards_diagn информацию о t_type из sp_tarif_extended
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
                            //Добавляем в cards_diagn информацию о is_onkl из mkb_extended
                            card.set_onkl(mkbExtendedOptionalMkbCodeP.get().isIs_onk());
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
                            //Добавляем в cards_diagn информацию о is_onkl из mkb_extended
                            card.set_onkl(mkbExtendedOptionalForMkbCode.get().isIs_onk());
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

                //Проверяем по направившим ЛПУ КП РДЦ и РЭЦ РДЦ
                if (card.getLpu() == 108 || card.getLpu() == 219) {
                    // 2021-04-08 разрешено подавать направленных из РЭЦ, КП на некоторые медуслуги Эндоскопии
                    boolean endoskUsl = AppConstants.endoskUsl.stream().anyMatch(s -> s.equals(card.getCode_usl().trim()));
                    if (endoskUsl) {
                        setLogs("DEBUG 2021-04-08 разрешено подавать направленных из РЭЦ, КП на некоторые медуслуги Эндоскопии: "
                                + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp());
                        setLogsInConsole("DEBUG 2021-04-08 разрешено подавать направленных из РЭЦ, КП на некоторые медуслуги Эндоскопии: "
                                + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp());
                    } else {
                        card.setCorrect(false);
                        card.setComment("Направленные от ЛПУ 108, 219 не подаются на оплату (за искл. некоторых медуслуг Эндоскопии)");
                        setLogs("ИСКЛЮЧЕНИЕ ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Направленные от ЛПУ 108, 219 не подаются на оплату (за искл. некоторых медуслуг Эндоскопии)");
                        setLogsInConsole("ИСКЛЮЧЕНИЕ ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Направленные от ЛПУ 108, 219 не подаются на оплату (за искл. некоторых медуслуг Эндоскопии)");
                    }
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

            //ПРОВЕРКА СВОЙСТВ НОВОРОЖДЕННОГО. НАЧАЛО
            //Если не исключается из оплаты и это услуга новорожденного
            if (card.isCorrect() && card.isNovor()) {
                if (card.getFam_n() == null || card.getIm_n() == null || card.getFam_n().trim().length() == 0 || card.getIm_n().trim().length() == 0 || card.getDat_rojd_n() == null) {
                    card.setCorrect(false);
                    card.setComment("Для новорожденного указаны не все необходимые данные: фамилия, имя, дата рождения, пол");
                    setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Для новорожденного указаны не все необходимые данные: фамилия, имя, дата рождения, пол");
                    setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                            + " Для новорожденного указаны не все необходимые данные: фамилия, имя, дата рождения, пол");
                } else { //Иначе, если поля указаны
                    //long diffInMilles = DAYS.between(card.getDate_in(), card.getDat_rojd_n());
                    //Вычисляем возраст новорожденного на дату обследования в днях
                    long age_novor = Duration.between(card.getDat_rojd_n().atStartOfDay(), card.getDate_in().atStartOfDay()).toDays();
                    //Если возраст больше 90 дней
                    if (age_novor > 90) {
                        card.setCorrect(false);
                        card.setComment("Возраст новорожденного на момент выполнения обследования превышает 90 дней " + age_novor);
                        setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Возраст новорожденного на момент выполнения обследования превышает 90 дней");
                        setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                + " Возраст новорожденного на момент выполнения обследования превышает 90 дней");
                    } else {
                        // Определить тип полиса, предъявленного за новорожденного
                        int vpolis = determineVpolis(card.getSpolis().trim(), card.getNpolis().trim());
                        //Если вычисленный тип полиса равен ошибочному
                        if (vpolis == AppConstants.TYPE_POLIS_ERROR) {
                            card.setCorrect(false);
                            card.setComment("Не удалось определить тип предъявленного за новорожденного полиса " + card.getVpolis());
                            setLogs("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                    + " Не удалось определить тип предъявленного за новорожденного полиса");
                            setLogsInConsole("ОШИБКА ИСХОДНЫХ ДАННЫХ: " + "SNPol " + card.getSnPol() + ", N_OTD " + card.getOtd() + ", N_MKP " + card.getN_mkp()
                                    + " Не удалось определить тип предъявленного за новорожденного полиса");
                        } else {
                            //Иначе устанавливаем тип полиса равный вычисленному
                            card.setVpolis(vpolis);
                        }
                    }
                }
            }
            //ПРОВЕРКА СВОЙСТВ НОВОРОЖДЕННОГО. КОНЕЦ

            //ДОБАВЛЕНИЕ СВОЙСТВ ДЛЯ CARDS. МОЖНО ДОБАВЛЯТЬ ЛЮБЫЕ СВОЙСТВА, ЧТОБЫ НАПРИМЕР ДАЛЕЕ ДЕЛАТЬ РАЗБИВКУ ПО НИМ
            //ДОБАВЛЕНИЕ ПРИЗНАКА МУР. НАЧАЛО
            if (!card.isInogor()) { //Если не иногородний

                System.out.println(card + " ************************");

                if (spTarifNewOptional.isPresent()) { //Если услуга есть в optional
                    if (spTarifNewOptional.get().isMuvr()) { //Если она относится к МУР
                        card.setMuvr(true); //тогда ставим МУР
                    }

                    if (!spTarifNewOptional.get().isMuvr()) { //Если она не МУР
                        //Проверяем направившее МО (подразделение по mcod), если у нее idump != 3, то это тоже МУР, остальное все НЕ МУР
                        if (slpuOptional.isPresent() && slpuOptional.get().getIdump() != 3) {
                            card.setMuvr(true); //тогда ставим МУР
                        }
                    }

                    //Добавлено 03.10.2023 в связи с обновлениями ТФОМС. Теперь для каждой услуги проверяется соответствие услуги способу оплаты
                    card.setUsl_idsp(spTarifNewOptional.get().getUsl_idsp());
                }
            }
            //ДОБАВЛЕНИЕ ПРИЗНАКА МУР. КОНЕЦ
        }
        setLogs("Конец проверки корректности диагностических услуг.");
        setLogsInConsole("Конец проверки корректности диагностических услуг.");
        //ПРОВЕРКА НАЛИЧИЯ ИССЛЕДОВАНИЙ С ТАКИМ ЖЕ КОДОМ МЕДУСЛУГИ НА ТАКОГО ПАЦИЕНТА В ЭТОТ ДЕНЬ. НАЧАЛО
        //ДЕЛАЕМ В ОТДЕЛЬНОМ ЦИКЛЕ
        setLogs("Проверка наличия у одного пациента одинаковых услуг в один день. Начало");
        setLogsInConsole("Проверка наличия у одного пациента одинаковых услуг в один день. Начало");
        //Сортируем коллекцию allCardsListDiagn перед началом проверки по snPol, фамилии, имени и отчеству, дате обследования
        //Перенес сортировку по коду услуги выше т.к. были случаи когда на два пациента один полис и все их услуги не отсекались при дублировании
        allCardsListDiagnDiagn.sort(Comparator.comparing(Cards::getSnPol)
                .thenComparing(Cards::getCode_usl)
                .thenComparing(Cards::getDate_in)
                .thenComparing(Cards::getFam)
                .thenComparing(Cards::getIm)
                .thenComparing(Cards::getOt)
                .thenComparing(Cards::is_onkl));

        //Проходимся по все отсортированной коллекции
        for (int i = 0; i < allCardsListDiagnDiagn.size(); i++) {
            //Если услуга ранее попала в список разрешенных на оплату
            if (allCardsListDiagnDiagn.get(i).isCorrect()) {
                //Получаем Optional для sp_tarif_extended для проверяемой услуги
                Optional<SpTarifExtended> spTarifExtendedOptional = spTarifExtendedService.findByKsg(allCardsListDiagnDiagn.get(i).getCode_usl());

                //Если услуга вернулась в optional и может быть оказана несколько раз (т.е. для нее есть кратность)
                if (spTarifExtendedOptional.isPresent() && spTarifExtendedOptional.get().getKr_mul() == 1) {
                    //Получаем текущую услугу
                    Cards current = allCardsListDiagnDiagn.get(i);
                    //Получаем LocalDate для текущей услуги
                    LocalDate currentCardsDate = current.getDate_in();
                    //Первая полученная, текущая услуга будет true
                    current.setCorrect(true);
                    //Пока закомментируем current.setKol_usl(1); т.к. всегда бывает 1
                    //current.setKol_usl(1);
                    //Второй цикл начинается уже со следующего элемента коллекции allCardsListDiagn
                    for (int j = i + 1; j < allCardsListDiagnDiagn.size(); j++) {
                        //Получаем следующий элемент
                        Cards next = allCardsListDiagnDiagn.get(j);
                        //Получаем LocalDate для следующей услуги
                        LocalDate nextCardsDate = next.getDate_in();
                        //Если полис, фио и дата услуги, код услуги следующего пациента не совпадает с текущим, значит данные не относятся к текущему и прерываем цикл
                        //Отдельно по новорожденным условия не нужно ставить, т.к. условие ниже учитывает услуги матери и новорожденного и корректно отработает
                        //Добавляем проверку по is_onkl чтобы услуга не отсекалась, если она была у пациента с кодом онко и без. Т.е. в оба случая попала услуга
                        if (!next.getSnPol().equals(current.getSnPol()) || !next.getFam_n().equals(current.getFam_n()) || !next.isNovor() == current.isNovor() ||
                                !next.getIm_n().equals(current.getIm_n()) || !next.getOt_n().equals(current.getOt_n()) || !nextCardsDate.equals(currentCardsDate) ||
                                !next.getCode_usl().equals(current.getCode_usl()) || !next.is_onkl() == current.is_onkl() || !next.isMuvr() == current.isMuvr()) {
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
                        Cards current = allCardsListDiagnDiagn.get(i);
                        //Получаем LocalDate для текущей услуги
                        LocalDate currentCardsDate = current.getDate_in();
                        //Первая полученная, текущая услуга будет true
                        current.setCorrect(true);
                        //Второй цикл начинается уже со следующего элемента коллекции allCardsListDiagn
                        for (int j = i + 1; j < allCardsListDiagnDiagn.size(); j++) {
                            //Получаем следующий элемент
                            Cards next = allCardsListDiagnDiagn.get(j);
                            //Получаем LocalDate для следующей услуги
                            LocalDate nextCardsDate = next.getDate_in();
                            //Если полис, фио и дата услуги, код услуги следующего пациента не совпадает с текущим, значит данные не относятся к текущему и прерываем цикл
                            //Отдельно по новорожденным условия не нужно ставить, т.к. условие ниже учитывает услуги матери и новорожденного и корректно отработает
                            //Добавляем проверку по is_onkl чтобы услуга не отсекалась, если она была у пациента с кодом онко и без. Т.е. в оба случая попала услуга
                            //Также добавил сравнение muvr
                            if (!next.getSnPol().equals(current.getSnPol()) || !next.getFam_n().equals(current.getFam_n()) || !next.isNovor() == current.isNovor() ||
                                    !next.getIm_n().equals(current.getIm_n()) || !next.getOt_n().equals(current.getOt_n()) || !nextCardsDate.equals(currentCardsDate) ||
                                    !next.getCode_usl().equals(current.getCode_usl()) || !next.is_onkl() == current.is_onkl() || !next.isMuvr() == current.isMuvr()) {
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

        //Сохраняем коллекцию allCardsListDiagn в БД
        cardsService.saveAllCards(allCardsListDiagnDiagn);
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
    /*record KeyForBoxing(String snpol, String lpu_shnm, int prvs, int t_type, boolean is_onkl) {
        public KeyForBoxing(Cards cards) {
            this(cards.getSnPol(), cards.getLpu_shnm(), cards.getPrvs(), cards.getT_type(), cards.isIs_onkl());
        }
    }*/

    //Пробуем разделять по muvr, вместо t_type
    //Для объединения диагностических услуг по полям в конструкторе String snpol, String lpu_shnm, int profil, boolean muvr, boolean is_onkl
    //Ранее по ошибке группировал по prvs вместо profil
    //Добавил 13.04.2023 также разбивку по полям fam_n, im_n, ot_n, чтобы для новорожденных также формировались случаи. Но такого на практике не было
    //Добавил 14.07.2023 разбивку по полю inogor, чтобы случаи пациента делились по этому признаку. т.к. с 07.2023 изменился способ оплаты idsp, чтобы корректной проставновки его и реализации признака muvr из программы
    //Добавил 03.10.2023 разбивку также по полю usl_idsp, т.к. с 09.2023 ТФОМС ввел проверку на соответствие услуги способу оплаты
    record KeyForBoxing(String snpol, String lpu_shnm, int profil, boolean muvr, boolean is_onkl, boolean inogor, int usl_idsp) {
        public KeyForBoxing(Cards cards) {
            this(cards.getSnPol(), cards.getLpu_shnm(), cards.getProfil(), cards.isMuvr(), cards.is_onkl(), cards.isInogor(), cards.getUsl_idsp());
        }
    }

    //Метод группирует список диагностических услуг Cards по датам в зависимости от того входит или нет услугу в интервал в days дней
    public Map<LocalDate, List<Cards>> groupCardsDiagnByDays(List<Cards> items, int days) {
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
                        i -> keyDate[0] = DAYS.between(keyDate[0], i.getDate_in()) < days ? keyDate[0] : i.getDate_in(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    //Метод упаковывает услуги пациента в разные случаи по входящему ключу-классу KeyForBoxing
    public Map<KeyForBoxing, List<Cards>> groupingUslInSluch(List<Cards> cardsList) {
        setLogs("Начало упаковки диагностических услуг в группы");
        setLogsInConsole("Начало упаковки диагностических услуг в группы");
        //Создаем новую Map
        Map<KeyForBoxing, List<Cards>> map = new LinkedHashMap<>();

        //Проходимся по коллекции cardsList используя for-each
        //computeIfAbsent метод при необходимости создаёт новое или возвращает существующее значение-список, для которого можно сразу же вызвать метод List:add
        cardsList.forEach(cards -> map
                .computeIfAbsent(new KeyForBoxing(cards)
                        , keyForBoxing -> new ArrayList<>())
                .add(cards));

        setLogs("Конец упаковки диагностических услуг в группы");
        setLogsInConsole("Конец упаковки диагностических услуг в группы");
        //Возвращаем map
        return map;
    }

    //Возвращаем коллекцию случаев с упакованными услугами, на основании входного ключа-класса
    public List<Sluch> getSluchList() {
        setLogs("Начало упаковки диагностических услуг в случаи");
        setLogsInConsole("Начало диагностических упаковки услуг в случаи");
        //Выбираем из БД только те услуги, для которых в поле correct стоит true. Т.е. разрешенные для оплаты
        //List<Cards> cardsList = cardsService.getCardsDiagnList().stream().filter(Cards::isCorrect).toList();
        //Выбираем только диагностические услуги
        List<Cards> cardsList = cardsService.getCardsDiagnList();

        //При получении списка случаев заодно и создаем set с уникальными врачами
        //vrachiList = getVrachiList(cardsDiagnList);

        //Вызываем метод для упаковки услуг по ключу и записываем их коллекцию map
        Map<KeyForBoxing, List<Cards>> map = groupingUslInSluch(cardsList);

        //Создаем коллекцию для случаев, куда будем добавлять все случаи всех пациентов.
        List<Sluch> sluchList = new ArrayList<>();

        //Проходимся по ключам Map
        for (Map.Entry<KeyForBoxing, List<Cards>> entry : map.entrySet()) {
            //Для каждого внешнего ключа KeyForBoxing берем его значение (т.е. List<Cards>) и помещаем его в Map<LocalDate, List<Cards>>
            //Таким образом делим List<Cards> для каждого ключа KeyForBoxing на группы по датам в промежутке 14 дней
            //Тут уже несколько групп услуг разделенных на группы в пределах 14 дней
            Map<LocalDate, List<Cards>> localDateListMap = groupCardsDiagnByDays(entry.getValue(), 14);

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

                //System.out.println(dateListEntry.getValue());
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

        setLogs("Конец упаковки диагностических услуг в случаи");
        setLogsInConsole("Конец диагностических упаковки услуг в случаи");
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
        setLogs("Начало упаковки диагностических случаев в группы для zap");
        setLogsInConsole("Начало упаковки диагностических случаев в группы для zap");

        setLogs("Кол-во случаев по диагностике: " + sluchList.size());
        setLogsInConsole("Кол-во случаев по диагностике: " + sluchList.size());

        setSluchCounterRdc(sluchList.size());

        //Создаем новую Map
        Map<ZapBoxing, List<Sluch>> map = new LinkedHashMap<>();

        //Проходимся по коллекции sluchList используя for-each
        //computeIfAbsent метод при необходимости создаёт новое или возвращает существующее значение-список, для которого можно сразу же вызвать метод List:add
        sluchList.forEach(sluch -> map
                .computeIfAbsent(new ZapBoxing(sluch), zapBoxing -> new ArrayList<>())
                .add(sluch));

        setLogs("Конец упаковки диагностических случаев в группы для zap");
        setLogsInConsole("Конец упаковки диагностических случаев в группы для zap");
        //Возвращаем map
        return map;
    }

    //Возвращаем коллекцию случаев с упакованными услугами, на основании входного ключа-класса
    public List<Zap> getZapList() {
        setLogs("Начало упаковки диагностических случаев в zap");
        setLogsInConsole("Начало упаковки диагностических случаев в zap");
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

        setLogs("Конец упаковки диагностических случаев в zap");
        setLogsInConsole("Конец упаковки диагностических случаев в zap");
        //Возвращаем коллекцию случаев zapList
        return zapList;
    }
    //УПАКОВКА СЛУЧАЕВ в ZAP. КОНЕЦ

    //Создание сегментов и узлов для ASUM файла. НАЧАЛО
    public void createDiagnAsumFile(List<Zap> zapList) {
        setLogs("Начало создания ASUM файла по диагностике");
        setLogsInConsole("Начало создания ASUM файла по диагностике");

        File dir = new File(AppConstants.asumFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (FileOutputStream out = new FileOutputStream(AppConstants.asumFilePath + AppConstants.ASUM_FILE_NAME_RDC + yearMonth + ".xml");
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

        setLogs("Конец создания ASUM файла по диагностике");
        setLogsInConsole("Конец создания ASUM файла по диагностике");
    }

    //Записываем данные в XML
    private void writeXML(BufferedOutputStream outputStream, List<Zap> zapList) throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        //XMLStreamWriter writer = factory.createXMLStreamWriter(outputStream, "UTF-8");
        XMLStreamWriter writer = factory.createXMLStreamWriter(outputStream, "UTF-8");
        writer.writeStartDocument("utf-8", "1.0");

        writer.writeStartElement("zl_list");

        Zglv zglv = new Zglv("1", "AAAAAAAA", "1.14", UtilDate.getCurrentDate(), AppConstants.ASUM_FILE_NAME_RDC + yearMonth);
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

    //Метод создает врача
    private Vrachi getVrach(Cards cards) {
        Vrachi vrachi = new Vrachi();
        vrachi.setKod(cards.getCode_md() + "");
        vrachi.setFio(cards.getVr_fio());
        vrachi.setMcod(AppConstants.TFOMS_CODE_RDC);

        Optional<Medspec> medspecOptional = medspecService.findByIdmsp(cards.getPrvs() + "");

        medspecOptional.ifPresent(medspec -> vrachi.setIdmsp(medspec.getIdmsp()));
        medspecOptional.ifPresent(medspec -> vrachi.setSpec(medspec.getMspname()));

        vrachi.setDost("false");
        vrachi.setType("1");

        return vrachi;
    }

}