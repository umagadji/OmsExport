package ru.rdc.omsexport.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

//Утилитный класс для отображения диалоговых окон
public class AlertDialogUtils {
    //Отображает диалоговое окно с ошибкой
    public static void showErrorAlert(String title, String headerText, String contentText) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            alert.setContentText(contentText);
            alert.show();
        });
    }

    //Отображает диалоговое окно с информацией
    public static void showInfoAlert(String title, String headerText, String contentText) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            alert.setContentText(contentText);
            alert.show();
        });
    }
}
