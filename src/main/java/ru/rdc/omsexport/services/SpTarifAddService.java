package ru.rdc.omsexport.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.local_db_models.SpTarifAdd;
import ru.rdc.omsexport.repository.SpTarifAddRepository;

import java.util.List;

@Service
public class SpTarifAddService {
    private final SpTarifAddRepository repository;

    @Autowired
    public SpTarifAddService(SpTarifAddRepository repository) {
        this.repository = repository;
    }

    public SpTarifAdd findByKsg(String ksg) {
        return repository.findByKsg(ksg);
    }

    @Transactional
    public void saveAll(List<SpTarifAdd> list) {
        repository.saveAll(list);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

}
