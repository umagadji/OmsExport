package ru.rdc.omsexport.asum_models;

import lombok.*;

//Класс описывает сегмент mr_usl_n для услуги
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MrUslN {
    private String idusl;
    private String mr_n;
    private String prvs_mr_n;
    private String code_md_m;
    private String fio_md;
}
