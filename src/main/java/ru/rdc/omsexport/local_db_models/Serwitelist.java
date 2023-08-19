package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "serwitelist")
public class Serwitelist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String ser;
    @Column
    private String nam_region;
    @Column
    private String tip;
    @Column
    private int lpu;
    @Column
    private int omsdoctype;

    public Serwitelist() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSer() {
        return ser;
    }

    public void setSer(String ser) {
        this.ser = ser;
    }

    public String getNam_region() {
        return nam_region;
    }

    public void setNam_region(String nam_region) {
        this.nam_region = nam_region;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getLpu() {
        return lpu;
    }

    public void setLpu(int lpu) {
        this.lpu = lpu;
    }

    public int getOmsdoctype() {
        return omsdoctype;
    }

    public void setOmsdoctype(int omsdoctype) {
        this.omsdoctype = omsdoctype;
    }

    @Override
    public String toString() {
        return "Serwitelist{" +
                "id=" + id +
                ", ser='" + ser + '\'' +
                ", nam_region='" + nam_region + '\'' +
                ", tip='" + tip + '\'' +
                ", lpu=" + lpu +
                ", omsdoctype=" + omsdoctype +
                '}';
    }
}
