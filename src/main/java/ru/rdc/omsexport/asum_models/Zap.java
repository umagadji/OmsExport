package ru.rdc.omsexport.asum_models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//Описывает тег ZAP
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Zap {
    private String id;
    private String idlist;
    private String pr_nov;

    private Pacient pacient;
    private List<Sluch> sluchList;

}