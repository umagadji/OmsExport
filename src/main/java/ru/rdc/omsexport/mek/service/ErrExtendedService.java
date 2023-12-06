package ru.rdc.omsexport.mek.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.mek.models.ErrExtended;
import ru.rdc.omsexport.mek.repository.ErrExtendedRepository;

import java.util.List;

@Service
public class ErrExtendedService {
    private final ErrExtendedRepository repository;
    @PersistenceContext
    private EntityManager entityManager;

    public ErrExtendedService(ErrExtendedRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveErrExtendedList() {
        TypedQuery<ErrExtended> query = entityManager.createQuery(
                "select new ErrExtended (err.codeUsl,err.errorCode,err.fio,err.birthDate,err.npolis,err.refreason,err.s_com,err.diagnosis,err.nameMO,err.docCode,err.sumvUsl,err.sankSum,err.nhistory,err.date_in,err.date_out,err.idstrax,plan.type)" +
                        "from Err err left join Plan plan on err.codeUsl = plan.code_usl", ErrExtended.class);

        List<ErrExtended> list = query.getResultList();
        repository.saveAll(list);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
