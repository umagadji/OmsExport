package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "sp_tarif_add")
public class SpTarifAdd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column(unique = true)
    private String ksg;
    @Column
    private int type;
    @Column
    private boolean ex_7_2;

    /*@OneToOne(mappedBy = "spTarifAdd")
    private SpTarif spTarif;*/

    /*@OneToOne
    @JoinColumn(name = "ksg_code", referencedColumnName = "ksgcode")
    private KsgCode ksgCode;*/

    public SpTarifAdd() {
    }

    /*public KsgCode getKsgCode() {
        return ksgCode;
    }

    public void setKsgCode(KsgCode ksgCode) {
        this.ksgCode = ksgCode;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKsg() {
        return ksg;
    }

    public void setKsg(String ksg) {
        this.ksg = ksg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isEx_7_2() {
        return ex_7_2;
    }

    public void setEx_7_2(boolean ex_7_2) {
        this.ex_7_2 = ex_7_2;
    }

    @Override
    public String toString() {
        return "SpTarifAdd{" +
                "id=" + id +
                ", ksg='" + ksg + '\'' +
                ", type=" + type +
                ", ex_7_2=" + ex_7_2 +
                '}';
    }
}
