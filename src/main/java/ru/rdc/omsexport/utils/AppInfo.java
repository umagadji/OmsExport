package ru.rdc.omsexport.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

//Утилитный класс для вывода информации о приложении
@Configuration
public class AppInfo {
    private static String version;

    public AppInfo(@Value("${info.app.version}") String version) {
        this.version = version;
        System.out.println(version);
    }

    public static void getAppInfo() {
    }

    public static String getVersion() {
        return version;
    }
}
