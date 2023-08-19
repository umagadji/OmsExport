package ru.rdc.omsexport.cards_model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

//Класс описывает входные DBF таблицы cards для всех отделений
@Entity
@Table(name = "cards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Cards implements Comparable<Cards> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private long n_tal;
    @Column
    private String spolis;
    @Column
    private String npolis;
    @Column
    private int vpolis;
    @Column
    private String smocod;
    @Column
    private String smo_nm;
    @Column
    private String fam;
    @Column
    private String im;
    @Column
    private String ot;
    @Column
    private boolean is_male;
    @Column (columnDefinition = "DATE")
    private LocalDate dat_rojd;
    @Column
    private boolean novor;
    @Column
    private String fam_n;
    @Column
    private String im_n;
    @Column
    private String ot_n;
    @Column
    private boolean is_male_n;
    @Column
    private LocalDate dat_rojd_n;
    @Column
    private boolean inogor;
    @Column
    private String mr;
    @Column
    private int doctype;
    @Column
    private String docser;
    @Column
    private String docnum;
    @Column
    private String snils;
    @Column
    private String adres;
    @Column
    private String smo_terr;
    @Column
    private LocalDate date_in;
    @Column
    private LocalDate date_out;
    @Column
    private int otd;
    @Column
    private long n_cab;
    @Column
    private String cab_name;
    @Column
    private String met_pr_kod;
    @Column
    private int n_met;
    @Column
    private String met_name;
    @Column
    private long nom_reg;
    @Column
    private long lpu;
    @Column
    private String lpu_name;
    @Column
    private String lpu_shnm;
    @Column
    private String lpu_n_ln;
    @Column
    private long rslt;
    @Column
    private long ishod;
    @Column
    private String code_usl;
    @Column
    private String mkb_code;
    @Column
    private double tarif;
    @Column
    private double coeff;
    @Column
    private double kol_usl;
    @Column
    private String code_md;
    @Column
    private String vr_fio;
    @Column
    private int prvs;
    @Column
    private String vr_spnm;
    @Column
    private String mkb_code_s;
    @Column
    private String met_cmnt;
    @Column
    private long n_mkp;
    @Column
    private String mkb_code_p;
    @Column
    private int char_zab;

    //Поле будет хранить признак включения или исключения услуги из оплаты. correct = true - означает что услуга корректна и можно на оплату подавать
    private boolean correct = true;

    //Комментарии
    private String comment;

    //Добавил сюда два поля t_type и is_onkl чтобы они участвовали в группировке услуг в случаи
    @Column
    private int t_type;
    @Column
    private boolean is_onkl;
    @Column
    private boolean muvr;
    @Column
    private int profil;
    @Column
    private String mcod; //Для упрощения поиска при выполнении запросов в таблице cards. Больше ни для чего

    public Cards(int id, String spolis, String npolis, boolean novor, String fam_n, String im_n, String ot_n) {
        this.id = id;
        this.spolis = spolis;
        this.npolis = npolis;
        this.novor = novor;
        this.fam_n = fam_n;
        this.im_n = im_n;
        this.ot_n = ot_n;
    }

    //Отдельное поле SNPOL для серии и полиса. Нужно для группировки услуг в случаи и сравнений
    public String getSnPol() {
        return this.spolis + this.npolis;
    }

    //ДЛЯ СВОЕГО СПОСОБА НАЛИЧИЯ ИССЛЕДОВАНИЙ С ТАКИМ ЖЕ КОДОМ МЕДУСЛУГИ НА ТАКОГО ПАЦИЕНТА В ЭТОТ ДЕНЬ
    /*@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        CardsDiagn cardsDiagn = (CardsDiagn) obj;

        LocalDate currentDate = new java.sql.Date(getDate_in().getTime()).toLocalDate();
        LocalDate objDate = new java.sql.Date(cardsDiagn.getDate_in().getTime()).toLocalDate();

        return getSnPol().equals(cardsDiagn.getSnPol())
                && isNovor() == cardsDiagn.isNovor()
                && getFam_n().equals(cardsDiagn.getFam_n())
                && getIm_n().equals(cardsDiagn.getIm_n())
                && getOt_n().equals(cardsDiagn.getOt_n())
                && getCode_usl().equals(cardsDiagn.getCode_usl())
                && currentDate.equals(objDate);
    }*/

    @Override
    public int compareTo(Cards o) {
        return this.date_in.compareTo(o.date_in);
    }
}
