package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "onklcod")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Onklcod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String lcod;

    @Override
    public String toString() {
        return "Onklcod{" +
                "id=" + id +
                ", lcod='" + lcod + '\'' +
                '}';
    }
}
