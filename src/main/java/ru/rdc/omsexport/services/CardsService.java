package ru.rdc.omsexport.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.cards_model.Cards;
import ru.rdc.omsexport.cards_model.CommentCountsDTO;
import ru.rdc.omsexport.repository.CardsRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Сервис для работы с таблицей cards_diagn
@Service
public class CardsService {
    private final CardsRepository repository;

    public CardsService(CardsRepository repository) {
        this.repository = repository;
    }

    //Метод сохраняет коллекцию с cardsdiagn в БД
    @Transactional
    public void saveAllCards(List<Cards> cardsList) {
        repository.saveAll(cardsList);
    }

    //Метод загружает из БД все записи cards
    public List<Cards> getCardsList() {
        return repository.findAll();
    }

    //Получаем диагностические исследования
    public List<Cards> getCardsDiagnList() {
        return repository.findAllByOtdInAndCorrect(new ArrayList<>(Arrays.asList(1,2,3,4,5)), true);
    }

    //Получаем эндокринологические исследования
    public List<Cards> getCardsRecList() {
        return repository.findAllByOtdInAndCorrect(new ArrayList<>(List.of(6)), true);
    }

    //Получаем поликлинические исследования
    public List<Cards> getCardsKpList() {
        return repository.findAllByOtdInAndCorrect(new ArrayList<>(List.of(7)), true);
    }

    //Получаем стоматологические исследования
    public List<Cards> getCardsKpStomList() {
        return repository.findAllByOtdInAndCorrectAndMetPrKod(new ArrayList<>(List.of(7)), true, "10.2.4");
    }

    public List<Cards> findAllByCorrect(boolean correct) {
        return repository.findAllByCorrect(correct);
    }

    //Получаем Список ошибок и количество услуг по каждой ошибке
    @Transactional(readOnly = true)
    public List<CommentCountsDTO> getCommentsCounter() {
        return repository.getCommentCounts();
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }
}
