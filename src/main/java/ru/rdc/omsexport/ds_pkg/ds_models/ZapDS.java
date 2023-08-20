package ru.rdc.omsexport.ds_pkg.ds_models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//Описывает тег ZAP
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZapDS {
    private String id;
    private String idlist;
    private String pr_nov;

    private PacientDS pacient;
    private List<SluchDS> sluchList;
}
