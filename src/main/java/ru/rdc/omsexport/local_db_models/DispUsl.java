package ru.rdc.omsexport.local_db_models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывает сущность disp_usl. Таблица будет хранить услуги по диспансеризациям. Приложение будет по этой таблице определять к какой диспансеризации относится услуга
@Entity
@Table(name = "disp_usl")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DispUsl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    @Column
    private String name_usl;
    @Column
    private String ksg;
    @Column
    private String usl_type;
}
