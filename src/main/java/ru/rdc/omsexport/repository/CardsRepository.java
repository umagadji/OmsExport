package ru.rdc.omsexport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.cards_model.Cards;
import ru.rdc.omsexport.cards_model.CommentCountsDTO;

import java.util.List;

//Репозиторий для работы с таблицей cards_diagn
@Repository
public interface CardsRepository extends JpaRepository<Cards, Integer> {
    List<Cards> findAllByOtdInAndCorrect(List<Integer> list, boolean correct);

    //Для получения всех услуг ошибочных или нет.
    List<Cards> findAllByCorrect(boolean correct);

    //Можно писать такие запросы, предварительно создав итоговый объектс полями, которыми хотим получить в запросе
    @Query("select new ru.rdc.omsexport.cards_model.CommentCountsDTO(c.comment, count(c)) from Cards c where c.comment is not null group by c.comment")
    List<CommentCountsDTO> getCommentCounts();
}
