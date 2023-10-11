package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывает сущность таблицы для чтения из DBF и записи в БД Postgres
@Entity
@Table(name = "sp_tarif")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpTarif {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String name_issl;
    @Column
    private String ksg;
    @Column
    private double price;
    @Column
    private int type;
    @Column
    private int idpr;
    @Column
    private int idump;
    @Column
    private double kol_usl;
    @Column
    private int t_type;
    @Column
    private int idvmp;
    @Column
    private String idpc;

    @Override
    public String toString() {
        return "SpTarif{" +
                "id=" + id +
                ", name_issl='" + name_issl + '\'' +
                ", ksg='" + ksg + '\'' +
                ", price=" + price +
                ", type=" + type +
                ", idpr=" + idpr +
                ", idump=" + idump +
                ", kol_usl=" + kol_usl +
                ", t_type=" + t_type +
                ", idvmp=" + idvmp +
                ", idpc='" + idpc + '\'' +
                '}';
    }
}
