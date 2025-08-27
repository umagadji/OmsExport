package ru.rdc.omsexport.ds_pkg.ds_models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывает пациента
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacientDS {
    private String vpolis;
    private String spolis;
    private String npolis;
    private String snpol;
    private String smo;
    private String smo_ogrn;
    private String smo_ok;
    private String smo_nam;
    private String novor;
    private String inogor;
    private String fam;
    private String im;
    private String ot;
    private String w;
    private String dr;
    private String fam_p;
    private String im_p;
    private String ot_p;
    private String w_p;
    private String dr_p;
    private String mr;
    private String doctype;
    private String docser;
    private String docnum;
    private String snils;
    private String okatog;
    private String okatop;
    private String comentz;
    private String adres;
    private String recid;
    private String inv;
    private String mse;
    private String docdate;
    private String docorg;
    private String soc; //Поле будет хранить соц. статус из таблицы patcategory
}
