package ru.rdc.omsexport.asum_models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывает тег napr
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Napr {
    private String idusl;
    private String id;
    private String napr_date;
    private String napr_v;
    private String met_issl;
    private String napr_usl;
    private String napr_mo;
}
