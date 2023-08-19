package ru.rdc.omsexport.ds_pkg.ds_models;

//Класс описывает КСЛП
public class SlKoefDS {
    private String idusl;
    private String idsl;
    private String z_sl;
    private String id;
    private String name_sl;
    private String cost12;

    public SlKoefDS() {
    }

    public String getIdusl() {
        return idusl;
    }

    public void setIdusl(String idusl) {
        this.idusl = idusl;
    }

    public String getIdsl() {
        return idsl;
    }

    public void setIdsl(String idsl) {
        this.idsl = idsl;
    }

    public String getZ_sl() {
        return z_sl;
    }

    public void setZ_sl(String z_sl) {
        this.z_sl = z_sl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_sl() {
        return name_sl;
    }

    public void setName_sl(String name_sl) {
        this.name_sl = name_sl;
    }

    public String getCost12() {
        return cost12;
    }

    public void setCost12(String cost12) {
        this.cost12 = cost12;
    }

    @Override
    public String toString() {
        return "SlKoef{" +
                "idusl='" + idusl + '\'' +
                ", idsl='" + idsl + '\'' +
                ", z_sl='" + z_sl + '\'' +
                ", id='" + id + '\'' +
                ", name_sl='" + name_sl + '\'' +
                ", cost12='" + cost12 + '\'' +
                '}';
    }
}
