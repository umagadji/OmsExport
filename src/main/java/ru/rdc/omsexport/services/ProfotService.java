package ru.rdc.omsexport.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.local_db_models.Profot;
import ru.rdc.omsexport.repository.ProfotRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProfotService {
    private final ProfotRepository repository;

    @Autowired
    public ProfotService(ProfotRepository repository) {
        this.repository = repository;
    }

    public Profot findByIdpr(int idpr) {
        return repository.findByIdpr(idpr);
    }

    @Transactional
    public void save(Profot item) {
        repository.save(item);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    public void saveAll(List<Profot> list) {
        repository.saveAll(list);
    }
}