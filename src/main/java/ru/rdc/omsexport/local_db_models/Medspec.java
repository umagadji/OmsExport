package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "medspec")
public class Medspec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String idmsp;
    @Column
    private String mspname;
    @Column
    private int q_prof;
    @Column
    private boolean dost;
    @Column
    private int idmspn;

    public Medspec() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdmsp() {
        return idmsp;
    }

    public void setIdmsp(String idmsp) {
        this.idmsp = idmsp;
    }

    public String getMspname() {
        return mspname;
    }

    public void setMspname(String mspname) {
        this.mspname = mspname;
    }

    public int getQ_prof() {
        return q_prof;
    }

    public void setQ_prof(int q_prof) {
        this.q_prof = q_prof;
    }

    public boolean isDost() {
        return dost;
    }

    public void setDost(boolean dost) {
        this.dost = dost;
    }

    public int getIdmspn() {
        return idmspn;
    }

    public void setIdmspn(int idmspn) {
        this.idmspn = idmspn;
    }

    @Override
    public String toString() {
        return "Medspec{" +
                "id=" + id +
                ", idmsp='" + idmsp + '\'' +
                ", mspname='" + mspname + '\'' +
                ", q_prof=" + q_prof +
                ", dost=" + dost +
                ", idmspn=" + idmspn +
                '}';
    }
}
