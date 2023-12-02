package ru.rdc.omsexport.ds_pkg.ds_models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывающий одну запись из входного файла
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RowDS {
    private String num;
    private String invoicekeyid;
    private String patientkeyid;
    private String rootvisitid;
    private String visitid;
    private String patservid;
    private String agrid;
    private String fam;
    private String im;
    private String ot;
    private String is_male;
    private String kol_usl;
    private String prvs;
    private String birthdate;
    private String address;
    private String spolis;
    private String npolis;
    private String snpol;
    private String vpolis;
    private String smo;
    private String smo_nam;
    private String smo_ogrn;
    private String smo_ok;
    private String novor;
    private String fam_n;
    private String im_n;
    private String ot_n;
    private String is_male_n;
    private String dat_rojd_n;
    private String dat_in;
    private String dat_out;
    private String doctor_code;
    private String doctor;
    private String mr;
    private String n_otd;
    private String otd_name;
    private String inogor;
    private String mkb_code;
    private String mkb_code_s;
    private String mkb_code_p;
    private String char_zab;
    private String tarif;
    private String coeff;
    private String met_pr_kod;
    private String doctype;
    private String docser;
    private String docnum;
    private String docorg;
    private String docdate;
    private String snils;
    private String usl_ksg;
    private String usl;
    private String usl_note;
    private String sumv;
    private String ibnumber;
    private String gosp;
    private String ishod;
    private String result_gosp;
    private String vidpom;
    private String npr_mo;
    private String npr_lpu;
    private String codeotd;
    private String profil;
    private String profil_kd;
    private String postuplenie;
    private String zakonch;
    private String kd;
    private String date_napr;
    private String zno;
    private String vr_spec;
    private String vr_spec_name;
    private String kslp_code;
    private String kslp_text;
    private String kslp_koeff;
    private String crit_code;
    private String crit_text;
    private String operac_code;
    private String operac_text;
}
