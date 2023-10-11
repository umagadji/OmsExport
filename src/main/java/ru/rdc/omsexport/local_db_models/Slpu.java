package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "s_lpu")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
