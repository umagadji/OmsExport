package ru.rdc.omsexport.mek.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "plan")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//Класс описывает таблицу plan с услугами, входящими в объемы
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code_usl;
    private String name_issl;
    private String type;
}
