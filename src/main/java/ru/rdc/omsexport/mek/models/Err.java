package ru.rdc.omsexport.mek.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс представляет собой объект Err, который получаем из XML с сайта ТФОМС
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "err")
//Класс описывающий элементы Err из XML с портала ТФОМС
public class Err {
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
    private String birthDate;
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
    private String sumvUsl;
    @Column
    private String sankSum;
    @Column
    private String nhistory;
    @Column
    private String date_in;
    @Column
    private String date_out;
    @Column
    private String idstrax;

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
                '}';
    }
}
