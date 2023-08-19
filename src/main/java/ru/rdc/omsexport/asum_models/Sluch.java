package ru.rdc.omsexport.asum_models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//Класс описывает случай пациента
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sluch {
    private String id;
    private String idcase;
    private String mcod;
    private String glpu;
    private String spolis;
    private String npolis;
    private String novor;
    private String smo;
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
    private String usl_ok;
    private String vidpom;
    private String npr_mo;
    private String order;
    private String t_order;
    private String podr;
    private String profil;
    private String det;
    private String nhistory;
    private String date_1;
    private String date_2;
    private String ds0;
    private String ds1;
    private String ds2;
    private String code_mes1;
    private String code_mes2;
    private String rslt;
    private String ishod;
    private String prvs;
    private String iddokt;
    private String os_sluch;
    private String idsp;
    private String ed_col;
    private String kolusl;
    private String tarif;
    private String sumv;
    private String oplata;
    private String sump;
    private String refreason;
    private String sank_mek;
    private String sank_mee;
    private String sank_ekmp;
    private String comentz;
    private String uid;
    private String inogor;
    private String pr_nov;
    private String otmonth;
    private String otyear;
    private String disp;
    private String vid_hmp;
    private String metod_hmp;
    private String ds3;
    private String vnov_m;
    private String rslt_d;
    private String vers_spec;
    private String dopsch;
    private String tal_d;
    private String tal_p;
    private String vbr;
    private String p_otk;
    private String nrisoms;
    private String ds1_pr;
    private String ds4;
    private String nazn;
    private String naz_sp;
    private String naz_v;
    private String naz_pmp;
    private String naz_pk;
    private String pr_d_n;
    private String npr_date;
    private String tal_num;
    private String ds2_pr;
    private String pr_ds2_n;
    private String c_zab;
    private String mse;
    private String vb_p;
    private String naz_usl;
    private String napr_date;
    private String napr_mo;
    private String ds_onk;
    private String npr_lpu;
    private String npr_usl_ok;
    private String wei;

    private int profil_k; //Профиль койки
    private int ed_kol_ds; //Количество койко-дней для стационаров

    //СВОИ ПОЛЯ
    boolean isOnkl = false;

    private List<Usl> uslList; //Случай содержит в себе несколько услуг
    private Pacient pacient;

    //Случай содержит сегменты окно
    private Cons cons;
    private Napr napr;
    private OnkSl onkSl;

    public String getSnPol() {
        return spolis + npolis;
    }

}