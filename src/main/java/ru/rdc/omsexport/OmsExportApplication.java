package ru.rdc.omsexport;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OmsExportApplication {

    //Начало выполнения программы
    public static void main(String[] args) {
        Application.launch(FomsRaschetRDC.class, args);
    }

}
