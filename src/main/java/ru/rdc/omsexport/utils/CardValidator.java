package ru.rdc.omsexport.utils;

import ru.rdc.omsexport.cards_model.Cards;
import ru.rdc.omsexport.constants.AppConstants;

import java.util.function.Consumer;

//07.02.2024 создал класс валидатор, чтобы не дублировать проверки во всех утилитных классах для формирования ASUM
public class CardValidator {

    //Проверка основных проверок из Excel
    public static void validateExcelCard(Cards card) {
        //Если для пациента указано в отчестве БО, Б-О, Б.О.
        if (card.getOt().equals("БО") || card.getOt().equals("Б-О") || card.getOt().equals("Б.О.")) {
            card.setOt("");
        }

        //Если тип полиса пациента 5 и он единого образца
        if (card.getVpolis() == 5 && card.getNpolis().trim().length() == 16) {
            card.setVpolis(3);
        }

        if (card.getVpolis() == 5 && card.getNpolis().trim().length() != 16) {
            card.setVpolis(1);
        }

        //Если тип полиса пациента 6 и он единого образца
        if (card.getVpolis() == 6 && card.getNpolis().trim().length() == 16) {
            card.setVpolis(3);
        }

        //Если тип полиса пациента 4, он не новорожденный и полис единого образца
        if (card.getVpolis() == 4 && !card.isNovor() && card.getNpolis().trim().length() == 16) {
            card.setVpolis(3);
        }

        //Заменить тип полиса всем записям, где длина полиса = 16
        if (card.getVpolis() == 0 && card.getNpolis().trim().length() == 16) {
            card.setVpolis(3);
        }

        //Заменить тип полиса всем записям, где длина полиса = 9
        if (card.getVpolis() == 0 && card.getSpolis().trim().length() == 0 && card.getNpolis().trim().length() == 9) {
            card.setVpolis(2);
        }

        if (card.getVpolis() == 4 && card.getSpolis().trim().length() == 0 && card.getNpolis().trim().length() == 9) {
            card.setVpolis(2);
        }

        if (card.isCorrect()) {
            if (card.getAdres().trim().length() < 8) {
                card.setAdres("Дагестан Респ");
            }
        }

        //Для всех полисов с типом 2 и 3, серию делаем пустым
        if (card.getVpolis() == 2 || card.getVpolis() == 3) {
            card.setSpolis("");
        }

        if (card.getLpu_shnm().trim().equals("~~~") || card.getLpu() == 0) {
            card.setCorrect(false);
            card.setComment("Исключается из оплаты: lpu_shnm = ~~~");
        }

        if (card.getNpolis().trim().equals("")) {
            card.setCorrect(false);
            card.setComment("Ошибка полиса: Номер полиса пустой");
        }

        if (card.getNpolis().trim().length() != 16 && card.getVpolis() == 3) {
            card.setCorrect(false);
            card.setComment("Ошибка полиса: Неверный ЕНП");
        }

        if (card.getNpolis().trim().length() != 9 && (card.getVpolis() == 1 || card.getVpolis() == 2) && !card.isInogor()) {
            card.setCorrect(false);
            card.setComment("Ошибка полиса: Врем. свид. или старый полис не равен 9 символам");
        }

        if (card.getVpolis() == 4 && card.getNpolis().trim().length() != 16) {
            card.setCorrect(false);
            card.setComment("Ошибка полиса: Полис родителя неверной длины");
        }

        if (card.getVpolis() == 0 && card.getNpolis().trim().length() != 16) {
            card.setCorrect(false);
            card.setComment("Ошибка полиса: Тип полиса пустой и/или неверная длина полиса");
        }

        if (card.getVpolis() == 0) {
            card.setCorrect(false);
            card.setComment("Ошибка полиса: Тип полиса пустой");
        }
    }

    //Проверка кода диагноза для диагностических услуг
    public static void validateMKBDiagnCards(Cards card) {
        if (card.getProfil() != 15 && card.getProfil() != 34 && card.getProfil() != 67 && card.getMkb_code().trim().equals("Z01.7")) {
            card.setMkb_code("Z03.8");
        }

        if (card.getProfil() != 15 && card.getProfil() != 34 && card.getProfil() != 67 && card.getMkb_code_p().trim().equals("Z01.7")) {
            card.setMkb_code_p("Z03.8");
        }
    }
}
