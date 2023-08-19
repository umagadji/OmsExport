package ru.rdc.omsexport.ds_pkg.ds_models;

//Класс для описания операций (фед. услуг) для некоторых КСГ
public class HrrgdDS {
    private String idusl;
    private String date_o;
    private String hkod;
    private String name_o;
    private String notksg;
    private String price;
    private String idvidvme;

    public HrrgdDS() {
    }

    public String getIdusl() {
        return idusl;
    }

    public String getHkod() {
        return hkod;
    }

    public void setHkod(String hkod) {
        this.hkod = hkod;
    }

    public void setIdusl(String idusl) {
        this.idusl = idusl;
    }

    public String getDate_o() {
        return date_o;
    }

    public void setDate_o(String date_o) {
        this.date_o = date_o;
    }

    public String getName_o() {
        return name_o;
    }

    public void setName_o(String name_o) {
        this.name_o = name_o;
    }

    public String getNotksg() {
        return notksg;
    }

    public void setNotksg(String notksg) {
        this.notksg = notksg;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIdvidvme() {
        return idvidvme;
    }

    public void setIdvidvme(String idvidvme) {
        this.idvidvme = idvidvme;
    }
}
