package ru.rdc.omsexport.mek.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

//Класс представляет собой объект Err, который получаем из XML с сайта ТФОМС
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "err_extended")
//Класс, описывающий расширенную таблицу состоящую из полей таблиц err и plan
public class ErrExtended {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String codeUsl;
    @Column
    private String errorCode;
    @Column
    private String fio;
    @Column
    private LocalDate birthDate;
    @Column
    private String npolis;
    @Column
    private String refreason;
    @Column
    private String s_com;
    @Column
    private String diagnosis;
    @Column
    private String nameMO;
    @Column
    private String docCode;
    @Column
    private double sumvUsl;
    @Column
    private double sankSum;
    @Column
    private String nhistory;
    @Column
    private LocalDate date_in;
    @Column
    private LocalDate date_out;
    @Column
    private String idstrax;
    @Column
    private String type;
    @Column
    private String error;
    @Column
    private boolean inogor;
    @Column
    private String smo;

    public ErrExtended(String codeUsl, String errorCode, String fio, LocalDate birthDate,
                       String npolis, String refreason, String s_com, String diagnosis, String nameMO,
                       String docCode, double sumvUsl, double sankSum, String nhistory, LocalDate date_in,
                       LocalDate date_out, String idstrax, String type, String error, boolean inogor, String smo) {
        this.codeUsl = codeUsl;
        this.errorCode = errorCode;
        this.fio = fio;
        this.birthDate = birthDate;
        this.npolis = npolis;
        this.refreason = refreason;
        this.s_com = s_com;
        this.diagnosis = diagnosis;
        this.nameMO = nameMO;
        this.docCode = docCode;
        this.sumvUsl = sumvUsl;
        this.sankSum = sankSum;
        this.nhistory = nhistory;
        this.date_in = date_in;
        this.date_out = date_out;
        this.idstrax = idstrax;
        this.type = type;
        this.error = error;
        this.inogor = inogor;
        this.smo = smo;
    }

    @Override
    public String toString() {
        return "Err{" +
                "codeUsl='" + codeUsl + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", fio='" + fio + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", npolis='" + npolis + '\'' +
                ", refreason='" + refreason + '\'' +
                ", s_com='" + s_com + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", divisionMO='" + nameMO + '\'' +
                ", docCode='" + docCode + '\'' +
                ", sumvUsl='" + sumvUsl + '\'' +
                ", sankSum='" + sankSum + '\'' +
                ", nhistory='" + nhistory + '\'' +
                ", date_in='" + date_in + '\'' +
                ", date_out='" + date_out + '\'' +
                ", idstrax='" + idstrax + '\'' +
                ", inogor='" + inogor + '\'' +
                ", smo='" + smo + '\'' +
                '}';
    }
}
