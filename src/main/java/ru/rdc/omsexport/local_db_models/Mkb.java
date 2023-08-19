package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "mkb")
public class Mkb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String lcod;
    @Column
    private String name;
    @Column
    private String pol;
    @Column
    private int pol_m;
    @Column
    private String deti;
    @Column
    private String baz;
    @Column
    private int baz_m;
    @Column
    private int terr;
    @Column
    private int terr_m;

    public Mkb() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLcod() {
        return lcod;
    }

    public void setLcod(String lcod) {
        this.lcod = lcod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public int getPol_m() {
        return pol_m;
    }

    public void setPol_m(int pol_m) {
        this.pol_m = pol_m;
    }

    public String getDeti() {
        return deti;
    }

    public void setDeti(String deti) {
        this.deti = deti;
    }

    public String getBaz() {
        return baz;
    }

    public void setBaz(String baz) {
        this.baz = baz;
    }

    public int getBaz_m() {
        return baz_m;
    }

    public void setBaz_m(int baz_m) {
        this.baz_m = baz_m;
    }

    public int getTerr() {
        return terr;
    }

    public void setTerr(int terr) {
        this.terr = terr;
    }

    public int getTerr_m() {
        return terr_m;
    }

    public void setTerr_m(int terr_m) {
        this.terr_m = terr_m;
    }

    @Override
    public String toString() {
        return "Mkb{" +
                "id=" + id +
                ", lcod='" + lcod + '\'' +
                ", name='" + name + '\'' +
                ", pol='" + pol + '\'' +
                ", pol_m=" + pol_m +
                ", deti='" + deti + '\'' +
                ", baz='" + baz + '\'' +
                ", baz_m=" + baz_m +
                ", terr=" + terr +
                ", terr_m=" + terr_m +
                '}';
    }
}
