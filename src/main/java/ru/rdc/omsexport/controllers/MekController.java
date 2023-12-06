package ru.rdc.omsexport.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import ru.rdc.omsexport.mek.models.Err;
import ru.rdc.omsexport.mek.utils.CreateReportService;
import ru.rdc.omsexport.mek.utils.ReadMekErrForXML;
import ru.rdc.omsexport.mek.utils.ReportsMek;
import ru.rdc.omsexport.utils.AlertDialogUtils;

import java.io.File;
import java.net.URL;
import java.util.*;

@Controller
public class MekController implements Initializable {

    private Stage thisStage;
    private ObservableList<Err> list;
    private ReportsMek reportsMek = new ReportsMek();

    @FXML
    private TableView<Err> tableMek;
    @FXML
    private TableColumn<Err, String> codeUsl;
    @FXML
    private TableColumn<Err, String> errorCode;
    @FXML
    private TableColumn<Err, String> fio;
    @FXML
    private TableColumn<Err, String> birthDate;
    @FXML
    private TableColumn<Err, String> npolis;
    @FXML
    private TableColumn<Err, String> refreason;
    @FXML
    private TableColumn<Err, String> s_com;
    @FXML
    private TableColumn<Err, String> diagnosis;
    @FXML
    private TableColumn<Err, String> nameMO;
    @FXML
    private TableColumn<Err, String> docCode;
    @FXML
    private TableColumn<Err, String> sumvUsl;
    @FXML
    private TableColumn<Err, String> sankSum;
    @FXML
    private TableColumn<Err, String> nhistory;
    @FXML
    private TableColumn<Err, String> date_in;
    @FXML
    private TableColumn<Err, String> date_out;
    @FXML
    private TableColumn<Err, String> idstrax;
    @FXML
    private Label txtCount;

    private String inputFileMEK = "";

    //Здесь также объявляем сервис для работы с МЭК и таблицей с услугами по планам
    private CreateReportService createReportService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setThisStage(Stage thisStage) {
        this.thisStage = thisStage;
    }

    //Через сеттер внедряем зависимость
    public void setCreateReportService(CreateReportService createReportService) {
        this.createReportService = createReportService;
    }

    //Метод позволяет считать XML фал с МЭК и вывести данные в таблицу.
    //Также в этом же время происходит запись в БД данных из таблицы plan.dbf и создается новая таблица err_extended (объединенная из err(это МЭК) и plan.dbf)
    public void onMekChoose(ActionEvent actionEvent) {
        //Перед выбором XML файла с МЭК удаляем все данные (относящиеся к МЭК) из БД
        createReportService.deleteDataFromAllTable();

        list = FXCollections.observableArrayList();

        try {
            FileChooser fileChooser = new FileChooser();
            //Только XML файлы
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
            File selectedFile = fileChooser.showOpenDialog(thisStage);
            inputFileMEK = selectedFile.getAbsolutePath();

            list = ReadMekErrForXML.getMek(inputFileMEK);

            // устанавливаем тип и значение которое должно хранится в колонке
            codeUsl.setCellValueFactory(new PropertyValueFactory<Err, String>("codeUsl"));
            errorCode.setCellValueFactory(new PropertyValueFactory<Err, String>("errorCode"));
            fio.setCellValueFactory(new PropertyValueFactory<Err, String>("fio"));
            birthDate.setCellValueFactory(new PropertyValueFactory<Err, String>("birthDate"));
            npolis.setCellValueFactory(new PropertyValueFactory<Err, String>("npolis"));
            refreason.setCellValueFactory(new PropertyValueFactory<Err, String>("refreason"));
            s_com.setCellValueFactory(new PropertyValueFactory<Err, String>("s_com"));
            diagnosis.setCellValueFactory(new PropertyValueFactory<Err, String>("diagnosis"));
            nameMO.setCellValueFactory(new PropertyValueFactory<Err, String>("nameMO"));
            docCode.setCellValueFactory(new PropertyValueFactory<Err, String>("docCode"));
            sumvUsl.setCellValueFactory(new PropertyValueFactory<Err, String>("sumvUsl"));
            sankSum.setCellValueFactory(new PropertyValueFactory<Err, String>("sankSum"));
            nhistory.setCellValueFactory(new PropertyValueFactory<Err, String>("nhistory"));
            date_in.setCellValueFactory(new PropertyValueFactory<Err, String>("date_in"));
            date_out.setCellValueFactory(new PropertyValueFactory<Err, String>("date_out"));
            idstrax.setCellValueFactory(new PropertyValueFactory<Err, String>("idstrax"));

            // заполняем таблицу данными
            tableMek.setItems(list);

            txtCount.setText("Количество записей: " + list.size());

            //Сохраняем данные из XML в таблицу err в БД
            createReportService.saveErrTableinDB(list);

        } catch (Exception e) {
            System.out.println("Файл не выбран");
        }

        //Сохраняем данные из plan.dbf в таблицу plan в БД
        createReportService.savePlanTable(createReportService.readPlanFiles());

        //На основании таблиц err и plan создается новая таблица err_extended, содержащая поля из err и plan. Нужно чтобы на ее основании делать отчеты.
        createReportService.saveErrExtentedTableInDB();

        AlertDialogUtils.showInfoAlert("Информация", null, "Данные МЭК успешно записаны в БД");
    }

    //Экспортирует весь МЭК так как он есть
    public void exportMEK(ActionEvent actionEvent) {
        reportsMek.createErrReport(list, thisStage);
    }

    public void exportMEKForTypes(ActionEvent actionEvent) {
    }

    public void exportMEKPervysh(ActionEvent actionEvent) {
    }
}
