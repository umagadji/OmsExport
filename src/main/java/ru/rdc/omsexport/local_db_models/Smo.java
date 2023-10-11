package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "smo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Smo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String tf_okato;
    @Column
    private String smocod;
    @Column
    private String nam_smok;
    @Column
    private String ogrn;

    @Override
    public String toString() {
        return "Smo{" +
                "id=" + id +
                ", tf_okato='" + tf_okato + '\'' +
                ", smocod='" + smocod + '\'' +
                ", nam_smok='" + nam_smok + '\'' +
                ", ogrn='" + ogrn + '\'' +
                '}';
    }
}
