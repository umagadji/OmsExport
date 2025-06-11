package ru.rdc.omsexport.asum_models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.rdc.omsexport.cards_model.Cards;

//Класс описывает услугу
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usl {
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
    private String n_zub;

    private MrUslN mrUslN;

    private Cards cards; //с 07.2023 добавил ссылку на cards, чтобы каждый usl имел ссылку на Cards, чтобы из usl получать cards и из cards все данные для sluch.

}