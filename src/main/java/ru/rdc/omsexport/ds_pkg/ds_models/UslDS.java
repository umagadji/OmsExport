package ru.rdc.omsexport.ds_pkg.ds_models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывает услугу
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UslDS {
    private String idstrax;
    private String idusl;
    private String idserv;
    private String spolis;
    private String npolis;
    private String glpu;
    private String mcod;
    private String podr;
    private String profil;
    private String det;
    private String date_in;
    private String date_out;
    private String ds;
    private String code_usl;
    private String ed_col;
    private String kol_usl;
    private String tarif;
    private String sumv_usl;
    private String zak;
    private String stand;
    private String prvs;
    private String code_md;
    private String comentu;
    private String uid;
    private String dopsch;
    private String dir2;
    private String gr_zdorov;
    private String student;
    private String vid_vme;
    private String koefk;
    private String pouh;
    private String otkaz2;
    private String nazna4;
    private String p_per;
    private String npl;
    private String idsh;
    private String idsh2;
    private String dn;
    private String ds_onk;
    private String p_cel;
    private String profil_k;
    private String idsl;
    private String muvr;
    private String muvr_lpu;
    private String date_usl;

    //Содержит в себе врача. Кажется было сделано временно, но это не точно
    private VrachiDS vrachiDS;

    private CritDS critDS;
    private HrrgdDS hrrgdDS;
    private SlKoefDS slKoefDS;
}
