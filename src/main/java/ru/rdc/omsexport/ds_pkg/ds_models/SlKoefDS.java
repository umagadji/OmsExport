package ru.rdc.omsexport.ds_pkg.ds_models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывает КСЛП
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlKoefDS {
    private String idusl;
    private String idsl;
    private String z_sl;
    private String id;
    private String name_sl;
    private String cost12;
}
