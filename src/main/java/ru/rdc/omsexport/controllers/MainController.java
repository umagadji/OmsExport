package ru.rdc.omsexport.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import ru.rdc.omsexport.asum_models.Vrachi;
import ru.rdc.omsexport.asum_models.Zap;
import ru.rdc.omsexport.asum_models.Zglv;
import ru.rdc.omsexport.constants.AppConstants;
import ru.rdc.omsexport.ds_pkg.ds_utils.CreateDsAsumFile;
import ru.rdc.omsexport.local_dbf_readers.WriteLocalDBTablesInDatabase;
import ru.rdc.omsexport.mek.utils.CreateReportService;
import ru.rdc.omsexport.utils.*;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@Scope("singleton")
public class MainController implements Initializable {
    @FXML
    private CheckBox chk1, chk2, chk3, chk4, chk5, chk6, chk7, chkStac, chk7stom, chkAll;
    @FXML
    private DatePicker dateStart, dateEnd;
    @FXML
    private TextArea logs;
    @FXML
    private Label counterRdc, counterRec, counterKp, counterDs, txtDsFileInfo, sluchCounterRDC, sluchCounterREC, sluchCounterKP, sluchCounterKPStom, counterKpStom;

    //Чекбокс для того, чтобы выставить даты услуг, выполненных позднее текущей привести к текущей дате. Добавил 22.10.2023 чтобы не получать новый ФЛК для предварительных файлов. Будет использоваться только для диагностики
    @FXML
    private CheckBox chkPredUslDate; // Значение поля не должно быть больше DSCHET, для сегмента DATE_Z_2 - Дата окончания лечения
    @FXML
    private CheckBox chkSaveExports; //Для включения или отключения возможности сохранять данные из cards

    private Stage mainStage;

    //Файл по стационару
    private String inputFileDS = "";

    private final WriteLocalDBTablesInDatabase writeLocalDBTablesInDatabase;
    private final CreateDiagnAsumFile createDiagnAsumFile;
    private final CreateRecAsumFile createRecAsumFile;
    private final CreateKpAsumFile createKpAsumFile;
    private final CreateDsAsumFile createDsAsumFile;
    private final CreateStomAsumFile createStomAsumFile;
    private final ReportsClass reportsClass;

    //Объявляем сервис, для работы с МЭК и таблицей услуг с планами и внедряем ее в основной контроллер
    private final CreateReportService createReportService;

    @Autowired
    public MainController(
            WriteLocalDBTablesInDatabase writeLocalDBTablesInDatabase,
            CreateDiagnAsumFile createDiagnAsumFile,
            CreateRecAsumFile createRecAsumFile,
            CreateKpAsumFile createKpAsumFile,
            CreateDsAsumFile createDsAsumFile,
            CreateStomAsumFile createStomAsumFile,
            ReportsClass reportsClass,
            ApplicationContext applicationContext, CreateReportService createReportService) {
        this.writeLocalDBTablesInDatabase = writeLocalDBTablesInDatabase;
        this.createDiagnAsumFile = createDiagnAsumFile;
        this.createRecAsumFile = createRecAsumFile;
        this.createKpAsumFile = createKpAsumFile;
        this.createDsAsumFile = createDsAsumFile;
        this.createStomAsumFile = createStomAsumFile;
        this.reportsClass = reportsClass;
        this.createReportService = createReportService;

        //При внедрении бинследующих бинов им также устанавливаем текущий контроллер, чтобы эти класс-бины имел доступ к компонентам этого контроллера
        writeLocalDBTablesInDatabase.setMainController(this);
        createDiagnAsumFile.setMainController(this);
        createRecAsumFile.setMainController(this);
        createKpAsumFile.setMainController(this);
        createDsAsumFile.setMainController(this);
        createStomAsumFile.setMainController(this);
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public TextArea getLogs() {
        return logs;
    }

    public Label getCounterRdc() {
        return counterRdc;
    }

    public Label getCounterDs() {
        return counterDs;
    }

    public Label getCounterRec() {
        return counterRec;
    }

    public Label getCounterKp() {
        return counterKp;
    }

    public Label getCounterKpStom() {
        return counterKpStom;
    }

    public Label getSluchCounterRDC() {
        return sluchCounterRDC;
    }

    public Label getSluchCounterREC() {
        return sluchCounterREC;
    }

    public Label getSluchCounterKP() {
        return sluchCounterKP;
    }

    public Label getSluchCounterKPStom() {
        return sluchCounterKPStom;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Получаем первую и последнюю дату текущего месяца
        LocalDate currentDate = LocalDate.now();
        LocalDate bgnDate = currentDate.withDayOfMonth(1);
        LocalDate endDate = bgnDate.plusDays(bgnDate.lengthOfMonth() - 1);

        //Устанавливаем в поля дат даты
        dateStart.setValue(bgnDate);
        dateEnd.setValue(endDate);
    }

    public void onStart(ActionEvent actionEvent) {
        logs.setText(null);
        List<String> otdList = getOtdList();

        if (chkPredUslDate.isSelected()) {
            createDiagnAsumFile.setChkPredUslDate(true);
        } else {
            createDiagnAsumFile.setChkPredUslDate(false);
        }

        if (otdList.size() != 0) {

            //09/07/2025 Убрал условие т.к. внес изменения в класс CreateDiagnAsumFile в связи с аналогичными изменениями для РЭЦ
            /*if (otdList.contains("2") && !otdList.contains("7")) {
                String contentText = "Для выполнения расчета по эндоскопии, также необходимо выбрать поликлинику, для учета исследований по ларингоскопии";
                AlertDialogUtils.showErrorAlert("Ошибка", null,contentText);
            } else */if (otdList.contains("ds") && inputFileDS.equals("")) {
                AlertDialogUtils.showErrorAlert("Ошибка", null, "Не выбран файл по стационару");
            } else if (createDsAsumFile.getKsgList() == null) {
                //Если нет локальной таблицы КСГ
                AlertDialogUtils.showErrorAlert("Ошибка", null, "Нет локальной таблицы с КСГ");
            } else if (dateStart.getValue().getMonth() != dateEnd.getValue().getMonth()) {
                AlertDialogUtils.showErrorAlert("Ошибка", null, "Период дат должен быть в пределах одного месяца");
            } else {
                writeLocalDBTablesInDatabase.deleteAllData();
                writeLocalDBTablesInDatabase.readDBFTables();

                new Thread(() -> {
                    if (otdList.contains("1") || otdList.contains("2") || otdList.contains("3") || otdList.contains("4") || otdList.contains("5")) {
                        createDiagnAsumFile.setPeriod(dateStart.getValue(), dateEnd.getValue());
                        createDiagnAsumFile.selectCorrectCardsDiagn(otdList);
                        createDiagnAsumFile.createDiagnAsumFile(createDiagnAsumFile.getZapList());
                    }

                    if (otdList.contains("6")) {
                        createRecAsumFile.setPeriod(dateStart.getValue(), dateEnd.getValue());
                        createRecAsumFile.selectCorrectCardsRec();
                        createRecAsumFile.createRecAsumFile(createRecAsumFile.getZapList());
                    }

                    if (otdList.contains("7")) {
                        createKpAsumFile.setPeriod(dateStart.getValue(), dateEnd.getValue());
                        createKpAsumFile.selectCorrectCardsKp();
                        createKpAsumFile.createKpAsumFile(createKpAsumFile.getZapList());
                    }

                    if (otdList.contains("7stom")) {
                        createStomAsumFile.setPeriod(dateStart.getValue(), dateEnd.getValue());
                        createStomAsumFile.selectCorrectCardsStom();
                        createStomAsumFile.createStomAsumFile(createStomAsumFile.getZapList());
                    }

                    if (otdList.contains("ds")) {
                        createDsAsumFile.setPeriod(dateStart.getValue(), dateEnd.getValue());
                        createDsAsumFile.selectCorrectRows(inputFileDS);
                        createDsAsumFile.createDSAsumFile(createDsAsumFile.getZapListDS());
                    }

                    AlertDialogUtils.showInfoAlert("Информация", null, "ASUM файлы успешно созданы в " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));

                    //Добавлено 08.05.2024. После окончания создания Asum файлов автоматически создается xlsx файл с данными из cards
                    if (chkSaveExports.isSelected()) {
                        reportsClass.getAllUslAutomatic(mainStage);
                    }

                }).start();

            }
        } else {
            AlertDialogUtils.showErrorAlert("Ошибка", null, "Выберите хотя бы одно отделение");
        }

    }

    //Коллекция будет хранить список выбранных отделений по диагностике
    private List<String> getOtdList() {
        List<String> otdList = new ArrayList<>();

        if (chk1.isSelected()) {
            otdList.add("1");
        }

        if (chk2.isSelected()) {
            otdList.add("2");
        }

        if (chk3.isSelected()) {
            otdList.add("3");
        }

        if (chk4.isSelected()) {
            otdList.add("4");
        }

        if (chk5.isSelected()) {
            otdList.add("5");
        }

        if (chk6.isSelected()) {
            otdList.add("6");
        }

        if (chk7.isSelected()) {
            otdList.add("7");
        }

        if (chk7stom.isSelected()) {
            otdList.add("7stom"); //Стоматология
        }

        if (chkStac.isSelected()) {
            otdList.add("ds");
        }

        return otdList;
    }

    //Выбрать файл по стационарам
    public void onSelectDsFile(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            //Только XML файлы
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
            File selectedFile = fileChooser.showOpenDialog(mainStage);
            inputFileDS = selectedFile.getAbsolutePath();
            txtDsFileInfo.setText("Файл: " + inputFileDS);
        } catch (Exception e) {
            System.out.println("Файл не выбран");
        }
    }

    public void onClose(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void onLoadLocalDbDbf(ActionEvent actionEvent) {
        writeLocalDBTablesInDatabase.deleteAllDataForLocalDB();
        writeLocalDBTablesInDatabase.readDBFTables();
    }

    public void onAbout(ActionEvent actionEvent) {
        AlertDialogUtils.showInfoAlert("О программе"
                , null
                , "Программа для расчета экономико-статистических показателей"
                        + "\nВерсия: " + AppInfo.getVersion());
    }

    public void onSaveLog(ActionEvent actionEvent) {
        if (!logs.getText().trim().isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String fileName = "log-" + LocalDateTime.now().format(formatter) + ".txt";

            File dir = new File(AppConstants.logsFilePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try (FileWriter writer = new FileWriter(AppConstants.logsFilePath + fileName, false)) {
                writer.write(logs.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            AlertDialogUtils.showInfoAlert("Информация", null, "Данные успешно сохранены в папку: " + AppConstants.logsFilePath + fileName);
        } else {
            AlertDialogUtils.showInfoAlert("Информация", null, "Нет данных для записи в лог!");
        }
    }

    public void onLoadAllUslFromDatabase(ActionEvent actionEvent) {
        reportsClass.getAllUsl(mainStage);
    }

    public void onLoadIncorrectUslFromDatabase(ActionEvent actionEvent) {
        reportsClass.getIncorrectUslFromDatabase(mainStage);
    }

    public void onLoadCommentTypes(ActionEvent actionEvent) {
        reportsClass.getErrorsType(mainStage);
    }

    public void onExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void onWorkMEK(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mek.fxml"));
            Parent root = loader.load(); // Загрузка содержимого FXML-файла

            Stage mekStage = new Stage();
            mekStage.setTitle("Работа с МЭК");
            mekStage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/icon.png"))));
            mekStage.initOwner(mainStage);

            Scene scene = new Scene(root, 800, 500); // Установка размеров сцены
            mekStage.setScene(scene);

            MekController mekController = loader.getController();
            mekController.setThisStage(mekStage);
            //При открытии нового окна внедряем сервис для работы с МЭК и таблицей с услугами по планаам
            mekController.setCreateReportService(createReportService);

            mekStage.show();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    //При нажатии на все кнопки
    public void onAllSelected(ActionEvent actionEvent) {
        if (chkAll.isSelected()) {
            chk1.setSelected(true);
            chk2.setSelected(true);
            chk3.setSelected(true);
            chk4.setSelected(true);
            chk5.setSelected(true);
            chk6.setSelected(true);
            chk7.setSelected(true);
            chkStac.setSelected(true);
            chk7stom.setSelected(true);
        } else {
            chk1.setSelected(false);
            chk2.setSelected(false);
            chk3.setSelected(false);
            chk4.setSelected(false);
            chk5.setSelected(false);
            chk6.setSelected(false);
            chk7.setSelected(false);
            chkStac.setSelected(false);
            chk7stom.setSelected(false);
        }
    }
}
