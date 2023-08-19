package ru.rdc.omsexport.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.local_db_models.MkbExtended;
import ru.rdc.omsexport.repository.MkbExtendedRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MkbExtendedService {
    private final MkbExtendedRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public MkbExtendedService(MkbExtendedRepository repository) {
        this.repository = repository;
    }

    public Optional<MkbExtended> findByLcod(String lcod) {
        return repository.findByLcod(lcod);
    }

    //Метод объединяет несколько таблиц в таблицу, описанную в классе MkbExtended
    @Transactional //@Transactional - эта аннотация нужна чтобы метод мог сохранять данные в БД. Также можно добавить эту аннотацию прямо на класс и не добавлять на каждый метод
    public void saveMkbExtendedList() {
        //Здесь прямо в запросе создаем DTO new MkbExtended указывая для конструктора из класса MkbExtended выбираемые из запроса поля
        //Также что не было ошибки Could not interpret path expression 'Mkb.lcod' в запросе нужно добавлять алиасы таблицам
        TypedQuery<MkbExtended> query = entityManager.createQuery(
                "select new MkbExtended (mkb.lcod, mkb.terr, case when mkb.lcod = onkl.lcod then true else false end)" +
                        "from Mkb mkb left join Onklcod onkl on mkb.lcod = onkl.lcod where mkb.terr = 1", MkbExtended.class);

        List<MkbExtended> list = query.getResultList();
        repository.saveAll(list);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }
}
