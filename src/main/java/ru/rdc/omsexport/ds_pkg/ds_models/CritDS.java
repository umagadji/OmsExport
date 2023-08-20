package ru.rdc.omsexport.ds_pkg.ds_models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс критерии для тех КСГ, где они необходимы по группировщику
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CritDS {
    private String idusl;
    private String crit;
    private String cname;
}
