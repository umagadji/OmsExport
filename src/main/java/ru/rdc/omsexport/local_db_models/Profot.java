package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "profot")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Profot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private int idpr;
    @Column
    private String prname;
    @Column (columnDefinition = "DATE")
    private LocalDate datebeg;
    @Column (columnDefinition = "DATE")
    private LocalDate dateend;
    @Column
    private boolean activ;

    @Override
    public String toString() {
        return "Profot{" +
                "id=" + id +
                ", idpr=" + idpr +
                ", prname='" + prname + '\'' +
                ", datebeg=" + datebeg +
                ", dateend=" + dateend +
                ", activ=" + activ +
                '}';
    }
}
