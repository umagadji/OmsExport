package ru.rdc.omsexport.ds_pkg.ds_models;

import java.util.List;

//Описывает тег ZAP
public class ZapDS {
    private String id;
    private String idlist;
    private String pr_nov;

    private PacientDS pacient;

    private List<SluchDS> sluchList;

    public ZapDS() {}

    public List<SluchDS> getSluchList() {
        return sluchList;
    }

    public void setSluchList(List<SluchDS> sluchList) {
        this.sluchList = sluchList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdlist() {
        return idlist;
    }

    public void setIdlist(String idlist) {
        this.idlist = idlist;
    }

    public String getPr_nov() {
        return pr_nov;
    }

    public void setPr_nov(String pr_nov) {
        this.pr_nov = pr_nov;
    }

    public PacientDS getPacient() {
        return pacient;
    }

    public void setPacient(PacientDS pacient) {
        this.pacient = pacient;
    }
}
