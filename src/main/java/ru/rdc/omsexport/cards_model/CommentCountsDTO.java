package ru.rdc.omsexport.cards_model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Класс для создания отчетов по типам ошибок из PostgreSQL
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentCountsDTO {
    private String comment;
    private long count;
}
