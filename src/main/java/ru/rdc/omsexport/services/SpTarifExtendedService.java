package ru.rdc.omsexport.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.local_db_models.SpTarifExtended;
import ru.rdc.omsexport.local_db_models.UslIdsp;
import ru.rdc.omsexport.repository.SpTarifExtendedRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SpTarifExtendedService {

    private final SpTarifExtendedRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public SpTarifExtendedService(SpTarifExtendedRepository repository) {
        this.repository = repository;
    }

    //Метод сохраняет все записи из списка List<SpTarifNew>
    /*public void saveAll() {
        repository.saveAll(getSpTarifNewList());
    }*/

    public List<SpTarifExtended> findAllByKsg(String ksg) {
        return repository.findAllByKsg(ksg);
    }

    public Optional<SpTarifExtended> findByKsg(String ksg) {
        return repository.findByKsg(ksg);
    }

    //Метод объединяет несколько таблиц в таблицу, описанную в классе SpTarifExtended
    @Transactional
    public void saveSpTarifExtendedList() {
        //Вариант, к которому пришел сам
        /*TypedQuery<Object[]> query = entityManager.createQuery(
                "select spt.name_issl, spt.ksg, spt.price, spt.type, spt.idpr, spt.kol_usl, spt.t_type, sptadd.ex_7_2, pf.prname " +
                        "from SpTarif spt " +
                        "INNER JOIN SpTarifAdd sptadd ON spt.type=1 AND (spt.kol_usl=1 OR spt.kol_usl=1.92) AND spt.ksg=sptadd.ksg " +
                        "INNER JOIN Profot pf ON spt.idpr = pf.idpr " +
                        "LEFT JOIN UslKratnostMulti ukrmulti ON spt.ksg = ukrmulti.ksg", Object[].class);

        List<SpTarifNew> list = new ArrayList<>();

        for (Object[] results : query.getResultList()) {
            SpTarifNew spTarifNew = new SpTarifNew();
            spTarifNew.setName_issl((String) results[0]);
            spTarifNew.setKsg((String) results[1]);
            spTarifNew.setPrice((Double) results[2]);
            spTarifNew.setType((int) results[3]);
            spTarifNew.setIdpr((int) results[4]);
            spTarifNew.setKol_usl((double) results[5]);
            spTarifNew.setT_type((int) results[6]);
            spTarifNew.setEx_7_2((Boolean) results[7]);
            spTarifNew.setPrname((String) results[8]);

            list.add(spTarifNew);

            System.out.println(spTarifNew);
        }
        repository.saveAll(list);*/

        //Вариант, который подсказали
        //В Spring есть возможность формировать DTO прямо в запросе.
        //SELECT new com.example.app.dto.MyDto(s.id, os.id) FROM Service s JOIN OtherService os ON os.srvCode = s.srvCode
        //case when spt.t_type in (71,72,73,7,75,76,77,78,79) then false else true end - если t_type равны этим значениям, то это не МУРы, иначе МУРы. False означает что не МУРы
        //2023-05-18 добавил в case t_type 29,39, чтобы посещения и обращения были НЕ МУР, а также услуги школы диабета
        //2023-10-03 - добавил новое поле idsp из таблицы usl_idsp т.к. с 09.2023 ТФОМС ввел проверку на соответствие услуги и способа оплаты
        //2023-10-10 - удалил использование таблицы sp_tarif_add, т.к. поле ex_7_2 не используется
        TypedQuery<SpTarifExtended> query = entityManager.createQuery(
        "select " +
                "new SpTarifExtended (spt.name_issl, spt.ksg, spt.price, spt.type, spt.idpr, spt.kol_usl, spt.t_type, pf.prname, case when ukrmulti.ksg is null then 0 else 1 end, case when ukrmulti.ksg is null then 0 else ukrmulti.max_krat end, case when spt.t_type in (29,39,71,72,73,74,75,76,77,78,79) or spt.ksg in('65000','65001') then false else true end, case when uslidsp.ksg is null then 0 else uslidsp.idsp end)" +
                "from SpTarif spt " +
                "INNER JOIN Profot pf ON spt.idpr = pf.idpr " +
                "LEFT JOIN UslKratnostMulti ukrmulti ON spt.ksg = ukrmulti.ksg " +
                "LEFT JOIN UslIdsp uslidsp ON spt.ksg = uslidsp.ksg", SpTarifExtended.class);

        List<SpTarifExtended> list = query.getResultList();
        repository.saveAll(list);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }
}
