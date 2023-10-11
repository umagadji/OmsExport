package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "medspec")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
