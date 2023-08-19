package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "onklcod")
public class Onklcod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String lcod;

    public Onklcod() {
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

    @Override
    public String toString() {
        return "Onklcod{" +
                "id=" + id +
                ", lcod='" + lcod + '\'' +
                '}';
    }
}
