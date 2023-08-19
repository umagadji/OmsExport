package ru.rdc.omsexport.asum_models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс описывает сегмент cons
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cons {
    //idusl - id случая, несмотря на название
    private String idusl;
    private String id;
    private String pr_cons;
    private String dt_cons;
}
