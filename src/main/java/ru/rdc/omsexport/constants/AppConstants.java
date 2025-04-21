package ru.rdc.omsexport.constants;

import java.util.Arrays;
import java.util.List;

public class AppConstants {
    //Константа код СМО дагестанского МАКС-М
    public static final String SMO_CODE_MAKS_RD = "05501";
    //Путь к локальной БД sp_tarif, medspec и т.д.
    public static final String localDBFPath = "LocalDBF\\";
    //Путь к таблицам cards
    public static final String cardsDBFPath = "Cards\\";
    //Путь к выходным файлам ASUM
    public static final String asumFilePath = "ASUM\\";
    //Путь к папке с логами
    public static final String logsFilePath = "Logs\\";
    //Путь к папке с отчетами
    public static final String reportsFilePath = "Reports\\";
    //Путь к папке с таблицей по плану
    public static final String planFilePath = "Plan\\";
    //Путь к папке с таблицей по Output - будет содержать xlsx файлы содержащие в себе данные из cards.
    // Если вдруг понадобится поднять узнать отсечения
    public static final String outputFilePath = "Output\\";
    //Имя файла для РДЦ
    public static final String ASUM_FILE_NAME_RDC = "ASUM050130";
    //Имя файла для РЭЦ
    public static final String ASUM_FILE_NAME_REC = "ASUM050131";
    //Имя файла для КП
    public static final String ASUM_FILE_NAME_KP = "ASUM050132";
    //Имя файла для Стационаров
    public static final String ASUM_FILE_NAME_DS = "ASUM05011D";
    //Имя файла для Стационаров
    public static final String ASUM_FILE_NAME_STOM = "ASUM0501YD";
    //Длина кода СМО
    public static final int SMO_CODE_LENGTH = 5;
    //Длина кода медуслуги стандартная = 5
    public static final int CODE_USL_STANDART_LEN = 5;
    //Длина кода медуслуги максимальная = 7
    public static final int CODE_USL_MAX_LEN = 7;
    //Длина кода ЛПУ у ТФОМС
    public static final int CODE_LPU_LENGTH = 6;
    //Тип полиса старого образца
    public static final int TYPE_POLIS_OLD = 1;
    //Тип полиса временного свидетельства
    public static final int TYPE_POLIS_VREMEN = 2;
    //Тип полиса единого образца
    public static final int TYPE_POLIS_ENP = 3;
    //Тип полиса единого образца
    public static final int TYPE_POLIS_ERROR = 0;

    //Кол-во услуг ОППЗ
    public static final double KOLVO_USL_OPPZ = 1.00;

    //Исход заболевания для отделений
    public static final String SLUCH_ISHOD_30 = "601"; //Без перемен
    public static final String SLUCH_ISHOD_31 = "304"; //Без перемен
    public static final String SLUCH_ISHOD_32 = "304"; //Без перемен
    public static final String SLUCH_ISHOD_YD = "502"; //Улучшение

    //Результат обращения для отделений
    public static final String SLUCH_RSLT_30 = "604"; //Направлен на консультацию
    public static final String SLUCH_RSLT_31 = "308"; //Направлен на консультацию
    public static final String SLUCH_RSLT_32 = "308"; //Направлен на консультацию
    public static final String SLUCH_RSLT_YD = "501"; //Лечение завершено

    //usl_ok для услуг
    public static final String SLUCH_USL_OK_30 = "6";
    public static final String SLUCH_USL_OK_31 = "3";
    public static final String SLUCH_USL_OK_32 = "3";
    public static final String SLUCH_USL_OK_YD = "5";

    //Цель посещения для услуг
    public static final String USL_PCEL_30 = "2.6";
    public static final String USL_PCEL_31 = "1";
    public static final String USL_PCEL_32 = "1";
    public static final String USL_PCEL_YD = "3"; //Стоматология

    //Коэффициент для услуг
    public static final String USL_KOEFK_30 = "0.00";
    public static final String USL_KOEFK_31 = "1.00";
    public static final String USL_KOEFK_32 = "1.00";
    public static final String USL_KOEFK_YD = "0.00"; //Стоматология

    //Форма оказания помощи
    public static final String SLUSH_FOR_POM = "3";

    //Разделы прейскуранта в МИС Ариадна для КП и РЭЦ
    public static final String ARIADNA_USL_RAZDEL_OPPZ = "10.2.1"; //Обращения по поводу заболевания
    public static final String ARIADNA_USL_RAZDEL_PROFPR = "10.2.2"; //Проф. приемы
    public static final String ARIADNA_USL_RAZDEL_SCHOOL = "10.2.3"; //Школа диабета
    public static final String ARIADNA_USL_RAZDEL_STOMATOLOGY = "10.2.4"; //Стоматология
    public static final String ARIADNA_USL_RAZDEL_TELEMEDICINE = "10.2.5"; //Телемедицинские консультации
    public static final String ARIADNA_USL_RAZDEL_REPRODCT_ZDOROVIE = "10.2.6"; //Консультации по репродуктивной диспансеризации

    //Характер заболевания в МИС
    public static final int ARIADNA_CHAR_ZAB_OSTROE = 1; //Острое
    public static final int ARIADNA_CHAR_ZAB_VPERVYE = 2; //Впервые установленное
    public static final int ARIADNA_CHAR_ZAB_RANEE = 7; //Ранее установленное

    //Характер заболевания в ASUM
    public static final int ASUM_CHAR_ZAB_OSTROE = 1;
    public static final int ASUM_CHAR_ZAB_VPERVYE = 2;
    public static final int ASUM_CHAR_ZAB_RANEE = 3;

    //Коды ТФОМС подразделений
    public static final String TFOMS_CODE_RDC = "050130"; //Диагностика
    public static final String TFOMS_CODE_REC = "050131"; //РЭЦ
    public static final String TFOMS_CODE_KP = "050132"; //Поликлиника
    public static final String TFOMS_CODE_KPSTOM = "0501YD"; //Стоматология

    //Код Glpu РДЦ
    public static final String GLPU_RDC = "050130"; //Glpu РДЦ

    //Виды помощи
    public static final String VID_POM_12 = "12";
    public static final String VID_POM_13 = "13";

    //Разрешенные услуги эндоскопии на которые можно направлять от КП (108) и РЭЦ (219)
    public static final List<String> endoskUsl = Arrays.asList("56001", "56596", "56002", "56597"
            , "56571", "56708", "56005", "56600", "56007", "56602", "56003", "56598", "56821", "56823", "56882", "56893");
}
