package ru.rdc.omsexport.local_dbf_readers;

import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.controllers.MainController;
import ru.rdc.omsexport.local_db_models.*;
import ru.rdc.omsexport.services.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

//Метод для записи считанных таблиц локальной БД в Postgres
@Service
public class WriteLocalDBTablesInDatabase {
    private final SmoService smoService;
    private final SlpuService slpuService;
    private final SpTarifService spTarifService;
    private final OnklcodService onklcodService;
    private final MedspecService medspecService;
    private final MkbService mkbService;
    private final ProfotService profotService;
    private final UslKratnostMultiService uslKratnostMultiService;
    private final UslIdspService uslIdspService;
    private final SpTarifExtendedService spTarifExtendedService;
    private final MkbExtendedService mkbExtendedService;
    private final CardsService cardsService;

    private MainController mainController;

    @Autowired
    public WriteLocalDBTablesInDatabase(
            SmoService smoService,
            SlpuService slpuService,
            SpTarifService spTarifService,
            OnklcodService onklcodService,
            MedspecService medspecService,
            MkbService mkbService,
            ProfotService profotService,
            UslKratnostMultiService uslKratnostMultiService,
            UslIdspService uslIdspService,
            SpTarifExtendedService spTarifExtendedService,
            MkbExtendedService mkbExtendedService,
            CardsService cardsService) {
        this.smoService = smoService;
        this.slpuService = slpuService;
        this.spTarifService = spTarifService;
        this.onklcodService = onklcodService;
        this.medspecService = medspecService;
        this.mkbService = mkbService;
        this.profotService = profotService;
        this.uslKratnostMultiService = uslKratnostMultiService;
        this.uslIdspService = uslIdspService;
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

    //Очищает все таблицы. Нужно для повторного запуска программу по кнопке "Запустить" из фронта
    public void deleteAllData() {
        medspecService.deleteAll();
        mkbService.deleteAll();
        mkbExtendedService.deleteAll();
        onklcodService.deleteAll();
        profotService.deleteAll();
        slpuService.deleteAll();
        smoService.deleteAll();
        spTarifService.deleteAll();
        spTarifExtendedService.deleteAll();
        uslKratnostMultiService.deleteAll();
        uslIdspService.deleteAll();
        cardsService.deleteAll();
    }

    //Очищает все локальной БД таблицы. Нужно для повторного запуска программу по кнопке "Запустить" из фронта
    public void deleteAllDataForLocalDB() {
        medspecService.deleteAll();
        mkbService.deleteAll();
        mkbExtendedService.deleteAll();
        onklcodService.deleteAll();
        profotService.deleteAll();
        slpuService.deleteAll();
        smoService.deleteAll();
        spTarifService.deleteAll();
        spTarifExtendedService.deleteAll();
        uslKratnostMultiService.deleteAll();
        uslIdspService.deleteAll();
    }

    //Метод читает данные из локальных таблиц DBF и сохраняет в БД
    public void readDBFTables() {
        setLogs("Начало чтения локальных таблиц БД (DBF)");
        setLogsInConsole("Начало чтения локальных таблиц БД (DBF)");

        List<Medspec> medspecList = MedspecDBFReader.readDbf( AppConstants.localDBFPath + "medspec.dbf", "Cp1251");
        medspecService.saveAll(medspecList);

        List<Mkb> mkbList = MkbDBFReader.readDbf(AppConstants.localDBFPath + "mkb.dbf", "Cp1251");
        mkbService.saveAll(mkbList);

        List<Onklcod> onklcodList = OnklcodDBFReader.readDbf(AppConstants.localDBFPath + "onklcod.dbf", "Cp1251");
        onklcodService.saveAll(onklcodList);

        List<Profot> profotList = ProfotDBFReader.readDbf(AppConstants.localDBFPath + "profot.dbf", "866");
        profotService.saveAll(profotList);

        List<Slpu> slpuList = SlpuDBFReader.readDbf(AppConstants.localDBFPath + "s_lpu.dbf", "Cp1251");
        slpuService.saveAll(slpuList);

        List<Smo> smoList = SmoDBFReader.readDbf(AppConstants.localDBFPath + "smo.dbf", "866");
        smoService.saveAll(smoList);

        List<SpTarif> spTarifList = SpTarifDBFReader.readDbf(AppConstants.localDBFPath + "sp_tarif.dbf", "Cp1251");
        spTarifService.saveAll(spTarifList);

        List<UslIdsp> uslIdspList = UslIdspDBFReader.readDbf(AppConstants.localDBFPath + "usl_idsp.dbf", "Cp1251");
        uslIdspService.saveAll(uslIdspList);

        List<UslKratnostMulti> uslKratnostMultiList = UslKratnostMultiDBFReader.readDbf(AppConstants.localDBFPath + "usl_kr_multi.dbf", "Cp1251");
        uslKratnostMultiService.saveAll(uslKratnostMultiList);

        //Сохраняет все объединенные записи в новую таблицу sp_tarif_extended
        spTarifExtendedService.saveSpTarifExtendedList();

        //Метод сохраняет в БД данные из таблиц mkb и onklcod в новую таблицу mkb_extended
        mkbExtendedService.saveMkbExtendedList();

        setLogs("Конец чтения локальных таблиц БД (DBF)");
        setLogsInConsole("Конец чтения локальных таблиц БД (DBF)");
    }

}
