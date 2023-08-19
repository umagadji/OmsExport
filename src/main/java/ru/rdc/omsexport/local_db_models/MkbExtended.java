package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;

@Entity
@Table(name = "mkb_extended")
public class MkbExtended {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String lcod;
    @Column
    private int terr;
    @Column
    private boolean is_onk;

    public MkbExtended() {}

    public MkbExtended(String lcod, int terr, boolean is_onk) {
        this.lcod = lcod;
        this.terr = terr;
        this.is_onk = is_onk;
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

    public int getTerr() {
        return terr;
    }

    public void setTerr(int terr) {
        this.terr = terr;
    }

    public boolean isIs_onk() {
        return is_onk;
    }

    public void setIs_onk(boolean is_onk) {
        this.is_onk = is_onk;
    }

    @Override
    public String toString() {
        return "MkbExtended{" +
                "id=" + id +
                ", lcod='" + lcod + '\'' +
                ", terr=" + terr +
                ", is_onk=" + is_onk +
                '}';
    }
}
