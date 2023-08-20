package ru.rdc.omsexport.ds_pkg.ds_models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс для описания операций (фед. услуг) для некоторых КСГ
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HrrgdDS {
    private String idusl;
    private String date_o;
    private String hkod;
    private String name_o;
    private String notksg;
    private String price;
    private String idvidvme;
}
