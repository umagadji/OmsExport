package ru.rdc.omsexport.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UtilDate {
    public static String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(LocalDate.now());
    }
}
