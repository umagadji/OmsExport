package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывает сущность usl_idsp. Начиная с 09.2023 ТФОМС ввел проверку на соответствие услуги способу оплаты
@Entity
@Table(name = "usl_idsp")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UslIdsp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    @Column
    private String ksg;
    @Column
    private int vid_usl;
    @Column
    private String name_vid;
    @Column
    private String idsp_name;
    @Column
    private int idsp;
}
