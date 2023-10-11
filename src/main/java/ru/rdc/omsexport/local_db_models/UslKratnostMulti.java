package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "usl_kr_multi")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UslKratnostMulti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String ksg;
    @Column
    private int max_krat;

    @Override
    public String toString() {
        return "UslKratnostMulti{" +
                "id=" + id +
                ", ksg='" + ksg + '\'' +
                ", max_krat='" + max_krat + '\'' +
                '}';
    }
}
