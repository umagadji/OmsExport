package ru.rdc.omsexport;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.rdc.omsexport.controllers.MainController;
import ru.rdc.omsexport.utils.AppInfo;

import java.io.IOException;
import java.util.Objects;

@Component
public class StageInitializer implements ApplicationListener<OmsExport.StageReadyEvent> {

    @Value("classpath:/fxml/main.fxml")
    private Resource resource;
    private final String applicationTitle;
    private final ApplicationContext applicationContext;

    public StageInitializer(
            @Value("Программа для расчета экономико-статистических показателей") String applicationTitle,
            ApplicationContext applicationContext) {
        this.applicationTitle = applicationTitle;
        this.applicationContext = applicationContext;
        //Вызываем метод, чтобы при открытии приложения версия приложения подтягивалась
        AppInfo.getAppInfo();
    }

    @Override
    public void onApplicationEvent(OmsExport.StageReadyEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(resource.getURL());
            loader.setControllerFactory(aClass -> applicationContext.getBean(aClass));
            Parent parent = loader.load();
            Stage stage = event.getStage();
            stage.setScene(new Scene(parent, 900, 600));
            stage.setTitle(applicationTitle + " " + AppInfo.getVersion());
            stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/icon.png"))));

            MainController mainController = loader.getController();
            mainController.setMainStage(stage);

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
