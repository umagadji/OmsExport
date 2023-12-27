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
                "select" +
                        " new ErrExtended (err.codeUsl,err.errorCode,err.fio,err.birthDate," +
                        "err.npolis,err.refreason,err.s_com,err.diagnosis,err.nameMO," +
                        "err.docCode,err.sumvUsl,err.sankSum,err.nhistory,err.date_in," +
                        "err.date_out,err.idstrax,plan.type, " +
                        "case " +
                        "when err.s_com like 'Пересечение%' then 'Пересечение' " +
                        "when err.s_com like 'Код направившей медицинской организации не соответствует коду МО прикрепления%' then 'Код направившей медицинской организации не соответствует коду МО прикрепления' " +
                        "when err.s_com like 'Страховая медицинская организация не соответствует%' then 'Страховая медицинская организация не соответствует' " +
                        "when err.s_com like 'Нет прикрепления в иногороднем случае с основным диагнозом содержащим букву%' then 'Нет прикрепления в иногороднем случае с основным диагнозом содержащим букву' " +
                        "else err.s_com end, err.inogor, err.smo)" +
                        " from Err err left join Plan plan on err.codeUsl = plan.code_usl", ErrExtended.class);

        List<ErrExtended> list = query.getResultList();
        repository.saveAll(list);
    }

    public List<ErrExtended> getAllErrExtendedList() {
        return repository.findAll();
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
