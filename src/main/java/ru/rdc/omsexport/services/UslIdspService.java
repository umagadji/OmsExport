package ru.rdc.omsexport.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.local_db_models.UslIdsp;
import ru.rdc.omsexport.repository.UslIdspRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UslIdspService {
    private final UslIdspRepository repository;

    @Autowired
    public UslIdspService(UslIdspRepository repository) {
        this.repository = repository;
    }

    public UslIdsp getUslIdspByKsg(String ksg) {
        return repository.getUslIdspByKsg(ksg);
    }

    @Transactional
    public void save(UslIdsp item) {
        repository.save(item);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    public void saveAll(List<UslIdsp> list) {
        repository.saveAll(list);
    }
}
