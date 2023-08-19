package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "s_lpu")
public class Slpu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String mcod;
    @Column
    private String glpu;
    @Column
    private int type;
    @Column
    private int idsp;
    @Column
    private int idump;
    @Column
    private String name;

    public Slpu() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMcod() {
        return mcod;
    }

    public void setMcod(String mcod) {
        this.mcod = mcod;
    }

    public String getGlpu() {
        return glpu;
    }

    public void setGlpu(String glpu) {
        this.glpu = glpu;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIdsp() {
        return idsp;
    }

    public void setIdsp(int idsp) {
        this.idsp = idsp;
    }

    public int getIdump() {
        return idump;
    }

    public void setIdump(int idump) {
        this.idump = idump;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Slpu{" +
                "id=" + id +
                ", mcod='" + mcod + '\'' +
                ", glpu='" + glpu + '\'' +
                ", type=" + type +
                ", idsp=" + idsp +
                ", idump=" + idump +
                ", name='" + name + '\'' +
                '}';
    }
}
