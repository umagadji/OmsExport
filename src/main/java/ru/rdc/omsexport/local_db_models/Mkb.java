package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "mkb")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mkb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String lcod;
    @Column
    private String name;
    @Column
    private String pol;
    @Column
    private int pol_m;
    @Column
    private String deti;
    @Column
    private String baz;
    @Column
    private int baz_m;
    @Column
    private int terr;
    @Column
    private int terr_m;

    @Override
    public String toString() {
        return "Mkb{" +
                "id=" + id +
                ", lcod='" + lcod + '\'' +
                ", name='" + name + '\'' +
                ", pol='" + pol + '\'' +
                ", pol_m=" + pol_m +
                ", deti='" + deti + '\'' +
                ", baz='" + baz + '\'' +
                ", baz_m=" + baz_m +
                ", terr=" + terr +
                ", terr_m=" + terr_m +
                '}';
    }
}
