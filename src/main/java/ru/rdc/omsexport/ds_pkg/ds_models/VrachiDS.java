package ru.rdc.omsexport.ds_pkg.ds_models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывающий врача
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VrachiDS {
    private String kod;
    private String fio;
    private String mcod;
    private String idmsp;
    private String spec;
    private String dost;
    private String type;
    private String vers_spec;
    private String ss;
}
