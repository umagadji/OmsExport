package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "usl_kr_multi")
public class UslKratnostMulti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String ksg;
    @Column
    private int max_krat;

    public UslKratnostMulti() {
    }

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

    public int getMax_krat() {
        return max_krat;
    }

    public void setMax_krat(int max_krat) {
        this.max_krat = max_krat;
    }

    @Override
    public String toString() {
        return "UslKratnostMulti{" +
                "id=" + id +
                ", ksg='" + ksg + '\'' +
                ", max_krat='" + max_krat + '\'' +
                '}';
    }
}
