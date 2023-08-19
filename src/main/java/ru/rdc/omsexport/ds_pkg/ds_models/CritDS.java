package ru.rdc.omsexport.ds_pkg.ds_models;

//Класс критерии для тех КСГ, где они необходимы по группировщику
public class CritDS {
    private String idusl;
    private String crit;
    private String cname;

    public CritDS() {
    }

    public String getIdusl() {
        return idusl;
    }

    public void setIdusl(String idusl) {
        this.idusl = idusl;
    }

    public String getCrit() {
        return crit;
    }

    public void setCrit(String crit) {
        this.crit = crit;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    @Override
    public String toString() {
        return "Crit{" +
                "idusl='" + idusl + '\'' +
                ", crit='" + crit + '\'' +
                ", cname='" + cname + '\'' +
                '}';
    }
}
