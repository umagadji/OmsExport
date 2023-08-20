package ru.rdc.omsexport.ds_pkg.ds_utils;

import javafx.application.Platform;
import org.springframework.stereotype.Service;
import ru.rdc.omsexport.asum_models.Zglv;
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.controllers.MainController;
import ru.rdc.omsexport.ds_pkg.ds_models.*;
import ru.rdc.omsexport.ds_pkg.local_db_models.ItemKsg;
import ru.rdc.omsexport.utils.UtilDate;
import ru.rdc.omsexport.utils.XMLFormatter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CreateDsAsumFile {
    private MainController mainController;

    private static String yearMonth = ""; //Хранит год и месяц для названия файла
    private final SimpleDateFormat formatterMain = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final String infoMSG = "ИНФОРМАЦИЯ: ";
    private final String errorMSG = "ОШИБКА: СЛУЧАЙ НЕ БУДЕТ ДОБАВЛЕН ПО ПРИЧИНЕ: ";
    private int allUslCounter = 0; //Счетчик всех услуг во входном файле стационара

    private Map<String, VrachiDS> vrachiDSMap = new HashMap<>();
    private List<RowDS> correctRowDSList = new ArrayList<>();

    private List<ItemKsg> itemKsgList = new ArrayList<>(); //Коллекция хранит КСГ из локальной БД

    public CreateDsAsumFile () {
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

    private void setCounterDS(int all, int checked) {
        Platform.runLater(()-> mainController.getCounterDs().setText("ДС: " + all + " / Случаев: " + checked));
    }

    //Метод проверяет все записи Rows на корректность и возвращает список корректных записей
    public List<RowDS> selectCorrectRows(String inputFile) {
        setLogs("Начало чтения стационарных услуг из XML");
        setLogsInConsole("Начало чтения стационарных услуг из XML");

        //Коллекция хранит записи из исходного файла
        List<RowDS> rowsList = ReadXmlDs.getRowsForXMLDS(inputFile);

        allUslCounter = rowsList.size();

        for (RowDS rowDS : rowsList) {
            //Проверяем rowsList.get(i) на корректность. Если все корректно, то создаем
            if (isCorrectIshod(rowDS) && isCorrectResultGosp(rowDS) && isCorrectProfile(rowDS)
                    && isCorrectProfileKD(rowDS) && isCorrectKD_Z(rowDS) && isCorrectMKBCode(rowDS) && isCorrectFedUsl(rowDS)) {
                //Добавляем в коллекцию корректных записей текущую запись
                correctRowDSList.add(rowDS);
                //Добавляем в мапу уникальных врачей
                vrachiDSMap.put(rowDS.getDoctor_code(), CreateAsumElementsDS.getVrachDS(rowDS));
            }
        }

        setLogs("Конец чтения стационарных услуг из XML");
        setLogsInConsole("Конец чтения стационарных услуг из XML");

        return correctRowDSList;
    }

    //Ключ по которому будем группировать стационарные случаи
    record DSBoxing(String snpol, String npr_lpu, String profil, String usl_ksg) {
        public DSBoxing(RowDS rowDS) {
            this(rowDS.getSnpol(), rowDS.getNpr_lpu(), rowDS.getProfil(), rowDS.getUsl_ksg());
        }
    }

    //Группируем RowsDS в случаи по ключу DSBoxing
    public Map<DSBoxing, List<RowDS>> groupingRowsInSluch(List<RowDS> rowDSList) {
        setLogs("Начало упаковки стационарных услуг в группы");
        setLogsInConsole("Начало упаковки стационарных услуг в группы");

        Map<DSBoxing, List<RowDS>> map = new LinkedHashMap<>();

        rowDSList.forEach(rowDS -> map
                .computeIfAbsent(new DSBoxing(rowDS), dsBoxing -> new ArrayList<>())
                .add(rowDS));

        setLogs("Конец упаковки стационарных услуг в группы");
        setLogsInConsole("Конец упаковки стационарных услуг в группы");
        return map;
    }

    //Возвращает коллекцию упакованных услуг в случаи
    private List<SluchDS> getSluchListDS() {
        setLogs("Начало упаковки стационарных услуг в случаи");
        setLogsInConsole("Начало стационарных упаковки услуг в случаи");

        Map<DSBoxing, List<RowDS>> map = groupingRowsInSluch(correctRowDSList);

        List<SluchDS> sluchDSList = new ArrayList<>();

        for (Map.Entry<DSBoxing, List<RowDS>> entry : map.entrySet()) {
            for (RowDS rowDS : entry.getValue()) {
                SluchDS sluchDS = CreateAsumElementsDS.createSluch(rowDS);

                UslDS usl = CreateAsumElementsDS.createUsl(rowDS, sluchDS);

                //Создаем КСЛП
                CreateAsumElementsDS.createSlKoef(rowDS, usl);
                //Создаем критерий
                CreateAsumElementsDS.createCrit(rowDS, usl);
                //Создаем операции
                CreateAsumElementsDS.createHrrgd(rowDS, usl);

                //Добавляем услугу к случаю
                sluchDS.setUslDS(usl);

                sluchDSList.add(sluchDS);

                //Выводим в логи информацию о добавленном случае пациента
                setLogs(infoMSG + "Добавлен случай для пациента "
                        + sluchDS.getFam() + " " + sluchDS.getIm() + " " + sluchDS.getOt()
                        + " " + sluchDS.getSpolis() + sluchDS.getNpolis());
                setLogsInConsole(infoMSG + "Добавлен случай для пациента "
                        + sluchDS.getFam() + " " + sluchDS.getIm() + " " + sluchDS.getOt()
                        + " " + sluchDS.getSpolis() + sluchDS.getNpolis());
            }
        }

        setLogs("Конец упаковки стационарных услуг в случаи");
        setLogsInConsole("Конец стационарных упаковки услуг в случаи");

        return sluchDSList;
    }

    //Ключ для группировки случаев по ZAP-Ы
    record ZapBoxingDS(String snpol) {
        public ZapBoxingDS(SluchDS sluchDS) {
            this(sluchDS.getSnPol());
        }
    }

    //Список ZAPов со случаями
    public Map<ZapBoxingDS, List<SluchDS>> groupingSluchListInZapDS(List<SluchDS> sluchDSList) {

        setLogs("Начало упаковки стационарных случаев в группы для zap");
        setLogsInConsole("Начало упаковки стационарных случаев в группы для zap");

        Map<ZapBoxingDS, List<SluchDS>> map = new LinkedHashMap<>();

        sluchDSList.forEach(sluchDS -> map
                .computeIfAbsent(new ZapBoxingDS(sluchDS), zapBoxingDS -> new ArrayList<>())
                .add(sluchDS));

        setCounterDS(allUslCounter, sluchDSList.size());

        setLogs("Конец упаковки стационарных случаев в группы для zap");
        setLogsInConsole("Конец упаковки стационарных случаев в группы для zap");

        return map;
    }

    //Получаем список ZAPов
    public List<ZapDS> getZapListDS() {
        setLogs("Начало упаковки стационарных случаев в zap");
        setLogsInConsole("Начало упаковки стационарных случаев в zap");

        List<SluchDS> sluchDSList = getSluchListDS();

        Map<ZapBoxingDS, List<SluchDS>> map = groupingSluchListInZapDS(sluchDSList);

        List<ZapDS> zapDSList = new ArrayList<>();

        for (Map.Entry<ZapBoxingDS, List<SluchDS>> entry : map.entrySet()) {
            ZapDS zapDS = new ZapDS();

            List<SluchDS> sluchesDS = new ArrayList<>(entry.getValue());

            zapDS.setSluchList(sluchesDS);

            zapDSList.add(zapDS);
        }

        setLogs("Конец упаковки стационарных случаев в zap");
        setLogsInConsole("Конец упаковки стационарных случаев в zap");

        return zapDSList;
    }

    public void createDSAsumFile(List<ZapDS> zapDSList) {
        setLogs("Начало создания ASUM файла по стационару");
        setLogsInConsole("Начало создания ASUM файла по стационару");
        /*try (FileOutputStream out = new FileOutputStream(AppConstants.asumFilePath + AppConstants.ASUM_FILE_NAME_RDC + yearMonth + ".xml");
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out)) {
            writeXML(bufferedOutputStream, zapDSList);
        } catch (XMLStreamException | IOException e) {
            //throw new RuntimeException(e);
            Platform.runLater(() -> {
                mainController.getLogs().appendText(e.toString());
            });
        }*/

        File dir = new File(AppConstants.asumFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //Для красивой печати
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            writeXML(out, zapDSList);

            String xml = out.toString(StandardCharsets.UTF_8);

            String prettyPrintXML = XMLFormatter.formatXML(xml);

            Files.writeString(Paths.get(AppConstants.asumFilePath + AppConstants.ASUM_FILE_NAME_DS + yearMonth + ".xml"), prettyPrintXML, StandardCharsets.UTF_8);
        } catch (XMLStreamException | TransformerException | IOException e) {
            throw new RuntimeException(e);
        }

        setLogs("Конец создания ASUM файла по стационару");
        setLogsInConsole("Конец создания ASUM файла по стационару");
    }

    //Записываем данные в XML
    private void writeXML(ByteArrayOutputStream outputStream/*BufferedOutputStream outputStream*/, List<ZapDS> zapList) throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        //XMLStreamWriter writer = factory.createXMLStreamWriter(outputStream, "UTF-8");
        XMLStreamWriter writer = factory.createXMLStreamWriter(outputStream, "UTF-8");
        writer.writeStartDocument("utf-8", "1.0");

        writer.writeStartElement("zl_list");

        Zglv zglv = new Zglv("1", "AAAAAAAA", "1.14", UtilDate.getCurrentDate(), AppConstants.ASUM_FILE_NAME_DS + yearMonth);
        WriteAsumXmlSegmentsDS.writeZglv(writer, zglv);

        //Записываем ZAPы
        for (ZapDS zap : zapList) {
            WriteAsumXmlSegmentsDS.writeZap(writer, zap);
        }

        //Записываем врачей в xml
        for (Map.Entry<String, VrachiDS> entry : vrachiDSMap.entrySet()) {
            WriteAsumXmlSegmentsDS.writeVrachi(writer, entry.getValue());
        }

        writer.writeEndElement();

        writer.writeEndDocument();

        writer.flush();
        writer.close();
    }

    //Метод проверяет что для выбранного КСГ корректно проставлена федеральная услуга по группировщику
    private boolean isCorrectFedUsl(RowDS rowDS) {
        if ( (rowDS.getUsl_ksg().trim().equals("3721ds37.00101") || rowDS.getUsl_ksg().trim().equals("3721ds37.00201") )
                && ( !rowDS.getOperac_code().trim().equals("B05.023.001") && !rowDS.getOperac_code().trim().equals("B05.024.003") ) ) {
            setLogs(errorMSG + "Для данного КСГ указана неверная услуга (операция)"
                    + " пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " - полис: " + rowDS.getSnpol());
            setLogsInConsole(errorMSG + "Для данного КСГ указана неверная услуга (операция)"
                    + " пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " - полис: " + rowDS.getSnpol());
            return false;
        } else if ( (rowDS.getUsl_ksg().trim().equals("3721ds37.00301") || rowDS.getUsl_ksg().trim().equals("3721ds37.00401") )
                && ( !rowDS.getOperac_code().trim().equals("B05.023.002.002") && !rowDS.getOperac_code().trim().equals("B05.050.003")
                && !rowDS.getOperac_code().trim().equals("B05.050.005") ) ) {
            setLogs(errorMSG + "Для данного КСГ указана неверная услуга (операция)"
                    + " пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " - полис: " + rowDS.getSnpol());
            setLogsInConsole(errorMSG + "Для данного КСГ указана неверная услуга (операция)"
                    + " пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " - полис: " + rowDS.getSnpol());
            return false;
        } else if ( (rowDS.getUsl_ksg().trim().equals("3721ds37.00701") || rowDS.getUsl_ksg().trim().equals("3721ds37.00801") )
                && ( !rowDS.getOperac_code().trim().equals("B05.014.002") && !rowDS.getOperac_code().trim().equals("B05.015.002")
                && !rowDS.getOperac_code().trim().equals("B05.037.001") && !rowDS.getOperac_code().trim().equals("B05.040.001")
                && !rowDS.getOperac_code().trim().equals("B05.069.003") ) ) {
            setLogs(errorMSG + "Для данного КСГ указана неверная услуга (операция)"
                    + " пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " - полис: " + rowDS.getSnpol());
            setLogsInConsole(errorMSG + "Для данного КСГ указана неверная услуга (операция)"
                    + " пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " - полис: " + rowDS.getSnpol());
            return false;
        } else if ( (rowDS.getUsl_ksg().trim().equals("1521ds15.00201") || rowDS.getUsl_ksg().trim().equals("1521ds15.00301") )
                && ( !rowDS.getOperac_code().trim().equals("A25.24.001.002") ) ) {
            setLogs(errorMSG + "Для данного КСГ указана неверная услуга (операция)"
                    + " пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " - полис: " + rowDS.getSnpol());
            setLogsInConsole(errorMSG + "Для данного КСГ указана неверная услуга (операция)"
                    + " пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " - полис: " + rowDS.getSnpol());
            return false;
        } else {
            return true;
        }
    }

    //Метод проверяет наличие МКБ кода и его длину
    private boolean isCorrectMKBCode(RowDS rowDS) {
        if (!rowDS.getMkb_code().trim().equals("") && rowDS.getMkb_code().trim().length() <= 5) {
            return true;
        } else {
            setLogs(errorMSG + "МКБ код отсутствует, либо его длина больше 5 символов"
                    + " пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " - полис: " + rowDS.getSnpol());
            setLogsInConsole(errorMSG + "МКБ код отсутствует, либо его длина больше 5 символов"
                    + " пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " - полис: " + rowDS.getSnpol());
            return false;
        }
    }

    //Метод проверяет входную строку Row на правильность количества койко-дней
    private boolean isCorrectKD_Z(RowDS rowDS) {
        LocalDate localDateIn = null;
        int daysLech = 0;
        try {
            localDateIn = formatterMain.parse(rowDS.getDat_in()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localDateOut = formatterMain.parse(rowDS.getDat_out()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Period period = Period.between(localDateIn, localDateOut);

            //Количество дней лечения.
            // Прибавляем 1, потому что если лег 16.01.2023, а выписался 25.01.2023, то лечение 10 дней, а разность выдает 9
            daysLech = period.getDays() + 1;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //Если количество койко-дней не больше чем длительность лечения и не равна 0
        if ( (Integer.parseInt(rowDS.getKd().trim()) <= daysLech) && Integer.parseInt(rowDS.getKd().trim()) != 0) {
            return true;
        } else {
            setLogs(errorMSG + "кол-во к/д для данного случая больше длительности лечения "
                    + " койко-дни " + rowDS.getKd() + " длительность лечения " + daysLech
                    + " пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " полис: " + rowDS.getSnpol());
            setLogsInConsole(errorMSG + "кол-во к/д для данного случая больше длительности лечения "
                    + " койко-дни " + rowDS.getKd() + " длительность лечения " + daysLech
                    + " пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " полис: " + rowDS.getSnpol());
            return false;
        }
    }

    //Метод проверяет входную строку Row на правильность исхода
    private boolean isCorrectIshod(RowDS rowDS) {
        if ( (rowDS.getUsl_ksg().equals("1521ds15.00201") ||  rowDS.getUsl_ksg().equals("1521ds15.00301")) && rowDS.getIshod().equals("203")) {
            return true;
        } else if (rowDS.getIshod().equals("202")) {
            return true;
        } else {
            setLogs(errorMSG + "для КСГ "
                    + rowDS.getUsl_ksg() + " " + rowDS.getUsl_ksg() + " неверный исход " + rowDS.getIshod() + " случай не будет добавлен "
                    + "пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " полис: " + rowDS.getSnpol());
            setLogsInConsole(errorMSG + "для КСГ "
                    + rowDS.getUsl_ksg() + " " + rowDS.getUsl_ksg() + " неверный исход " + rowDS.getIshod() + " случай не будет добавлен "
                    + "пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " полис: " + rowDS.getSnpol());
            return false;
        }
    }

    //Метод проверяет входную строку Row на правильность результата госпитализации
    private boolean isCorrectResultGosp(RowDS rowDS) {
        if (rowDS.getResult_gosp().equals("201")) {
            return true;
        } else {
            setLogs(errorMSG + "для КСГ "
                    + rowDS.getUsl_ksg() + " " + rowDS.getUsl_ksg() + " неверный результат госпитализации " + rowDS.getResult_gosp() + " случай не будет добавлен "
                    + "пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " полис: " + rowDS.getSnpol());
            setLogsInConsole(errorMSG + "для КСГ "
                    + rowDS.getUsl_ksg() + " " + rowDS.getUsl_ksg() + " неверный результат госпитализации " + rowDS.getResult_gosp() + " случай не будет добавлен "
                    + "пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " полис: " + rowDS.getSnpol());
            return false;
        }
    }

    //Метод проверяет входную строку Row на правильность профиля
    private boolean isCorrectProfile(RowDS rowDS) {
        if (rowDS.getN_otd().equals("051") && rowDS.getProfil().equals("122")) {
            return true;
        } else if (rowDS.getN_otd().equals("271") && rowDS.getProfil().equals("53")) {
            return true;
        } else if (rowDS.getN_otd().equals("131") && rowDS.getProfil().equals("158")) {
            return true;
        } else {
            setLogs(errorMSG + "для КСГ "
                    + rowDS.getUsl_ksg() + " " + rowDS.getUsl_ksg() + " неверный профиль " + rowDS.getProfil() + " случай не будет добавлен "
                    + "пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " полис: " + rowDS.getSnpol());
            setLogsInConsole(errorMSG + "для КСГ "
                    + rowDS.getUsl_ksg() + " " + rowDS.getUsl_ksg() + " неверный профиль " + rowDS.getProfil() + " случай не будет добавлен "
                    + "пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " полис: " + rowDS.getSnpol());
            return false;
        }
    }

    //Метод проверяет входную строку Row на правильность профиля койки
    private boolean isCorrectProfileKD(RowDS rowDS) {
        //Если в ROW КСГ совпадает с тем что есть в локальной БД КСГ, то в Optional будет что-то, иначе не будет
        //Нужно чтобы, проверять правильность профиля койко-дней только для тех, услуг, которые есть в локальной БД
        Optional<ItemKsg> ksgOptional = itemKsgList.stream().filter(itemKsg -> itemKsg.getKsg().trim().equals(rowDS.getUsl_ksg().trim())).findFirst();

        //Если в Optional есть что-то
        if (ksgOptional.isPresent()) {
            //Делаем проверки
            if ( (rowDS.getUsl_ksg().equals("3721ds37.00101") || rowDS.getUsl_ksg().equals("3721ds37.00201")) && rowDS.getProfil_kd().equals("31") ) {
                return true;
            } else if ( (rowDS.getUsl_ksg().equals("3721ds37.00301") || rowDS.getUsl_ksg().equals("3721ds37.00401")) && rowDS.getProfil_kd().equals("32") ) {
                return true;
            } else if ( (rowDS.getUsl_ksg().equals("3721ds37.00701") || rowDS.getUsl_ksg().equals("3721ds37.00801")) && rowDS.getProfil_kd().equals("30") ) {
                return  true;
            } else if ( (
                    //Для ДС РЭЦ могут быть не только КСГ сахарный диабет, но и другие
                    rowDS.getUsl_ksg().equals("3521ds35.00101")
                            || rowDS.getUsl_ksg().equals("2921ds29.00401")
                            || rowDS.getUsl_ksg().equals("3521ds35.00201")
            ) && rowDS.getProfil_kd().equals("85")) {
                return  true;
            } else if ( (
                    rowDS.getUsl_ksg().equals("1321ds13.00101")
                            || rowDS.getUsl_ksg().equals("1521ds15.00101")
                            || rowDS.getUsl_ksg().equals("1621ds16.00101")
                            || rowDS.getUsl_ksg().equals("2321ds23.00101")
                            || rowDS.getUsl_ksg().equals("1521ds15.00201")
                            || rowDS.getUsl_ksg().equals("1521ds15.00301")
            ) && rowDS.getProfil_kd().equals("34") ) {
                return true;
            } else if ( (rowDS.getUsl_ksg().equals("3721ds37.01501") || rowDS.getUsl_ksg().equals("3721ds37.01601")) && rowDS.getProfil_kd().equals("30") ) {
                return true;
            } else {
                setLogs(errorMSG + "для КСГ "
                        + rowDS.getUsl_ksg() + " " + rowDS.getUsl_ksg() + " неверный профиль койки " + rowDS.getProfil_kd() + " случай не будет добавлен "
                        + "пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " полис: " + rowDS.getSnpol());
                setLogsInConsole(errorMSG + "для КСГ "
                        + rowDS.getUsl_ksg() + " " + rowDS.getUsl_ksg() + " неверный профиль койки " + rowDS.getProfil_kd() + " случай не будет добавлен "
                        + "пациент: " + rowDS.getFam() + " " + rowDS.getIm() + " " + rowDS.getOt() + " полис: " + rowDS.getSnpol());
            }
        } else {
            //Иначе неизвестный КСГ
            setLogs(errorMSG + "Неизвестный КСГ: " + rowDS.getUsl_ksg().trim());
            setLogsInConsole(errorMSG + "Неизвестный КСГ: " + rowDS.getUsl_ksg().trim());
        }

        return false;
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

    //Метод заполняет список КСГ из локальной базы в программе
    public List<ItemKsg> getKsgList() {
        itemKsgList = ReadKsgForXML.getKSGForLocalDB(AppConstants.localDBFPath + "ksg.xml");
        return itemKsgList;
    }
}
