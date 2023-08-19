package ru.rdc.omsexport.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.local_db_models.UslKratnostMulti;
import ru.rdc.omsexport.repository.UslKratnostMultiRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UslKratnostMultiService {
    private final UslKratnostMultiRepository repository;

    @Autowired
    public UslKratnostMultiService(UslKratnostMultiRepository repository) {
        this.repository = repository;
    }

    public UslKratnostMulti getUslKratnostMultiByKsg(String ksg) {
        return repository.getUslKratnostMultiByKsg(ksg);
    }

    @Transactional
    public void save(UslKratnostMulti item) {
        repository.save(item);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    public void saveAll(List<UslKratnostMulti> list) {
        repository.saveAll(list);
    }
}
