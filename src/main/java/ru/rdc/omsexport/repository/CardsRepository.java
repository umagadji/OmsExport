package ru.rdc.omsexport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.rdc.omsexport.cards_model.Cards;
import ru.rdc.omsexport.cards_model.CommentCountsDTO;

import java.util.List;

//Репозиторий для работы с таблицей cards_diagn
@Repository
public interface CardsRepository extends JpaRepository<Cards, Integer> {
    List<Cards> findAllByOtdInAndCorrect(List<Integer> list, boolean correct);

    List<Cards> findAllByOtdInAndCorrectAndMetPrKod(List<Integer> list, boolean correct, String met_pr_kod);

    //Для получения всех услуг ошибочных или нет.
    List<Cards> findAllByCorrect(boolean correct);

    //Можно писать такие запросы, предварительно создав итоговый объект с полями, которыми хотим получить в запросе
    @Query("select new ru.rdc.omsexport.cards_model.CommentCountsDTO(c.comment, count(c)) from Cards c where c.comment is not null group by c.comment")
    List<CommentCountsDTO> getCommentCounts();

    //Новый метод от 30.04.2025 для отбора услуг с комментариями по диспансеризации
    @Query("SELECT c FROM Cards c WHERE c.dispcorrect = true and c.otd IN :otdList and c.comment IN :comments")
    List<Cards> findByDispUslCommentsIn(@Param("otdList") List<Integer> list, @Param("comments") List<String> comments);
}
