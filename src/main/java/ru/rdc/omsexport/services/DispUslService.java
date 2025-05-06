package ru.rdc.omsexport.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdc.omsexport.local_db_models.DispUsl;
import ru.rdc.omsexport.local_db_models.UslIdsp;
import ru.rdc.omsexport.repository.DispUslRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DispUslService {
    private final DispUslRepository repository;

    public DispUslService(DispUslRepository repository) {
        this.repository = repository;
    }

    public Optional<DispUsl> getDispUslByKsg(String ksg) {
        return repository.getDispUslByKsg(ksg);
    }

    @Transactional
    public void save(DispUsl item) {
        repository.save(item);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    public void saveAll(List<DispUsl> list) {
        repository.saveAll(list);
    }
}
